package ua.ohonovskiy.onlineshop.synchronize.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.*;
import ua.ohonovskiy.onlineshop.product.category.model.Category;
import ua.ohonovskiy.onlineshop.product.category.service.CategoryService;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.product.productImage.model.ProductImage;
import ua.ohonovskiy.onlineshop.product.productImage.service.ProductImageService;
import ua.ohonovskiy.onlineshop.shop.model.Shop;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImportProcessorService {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ImportSubmitService importSubmitService;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void importProducts(Long importSubmitId) {

        var importSubmit = importSubmitService.getByIdWithShop(importSubmitId);
        Shop shop = importSubmit.getShop();

        try {
            if (shop == null) {
                importSubmitService.markFailed(importSubmitId, "User shop not found");
                return;
            }

            String importFileUrl = shop.getImportFileURL();
            if (importFileUrl == null || importFileUrl.isBlank()) {
                importSubmitService.markFailed(importSubmitId, "Shop import URL missing");
                return;
            }

            try (InputStream input = new URL(importFileUrl).openStream()) {

                Document doc = buildDoc(input);

                // ====== Categories (без N+1)
                NodeList categoryNodes = doc.getElementsByTagName("category");

                Map<Long, String> idToName = new HashMap<>();
                Map<Long, Long> idToParentId = new HashMap<>();
                Set<String> names = new HashSet<>();

                for (int i = 0; i < categoryNodes.getLength(); i++) {
                    Element e = (Element) categoryNodes.item(i);
                    Long id = Long.parseLong(e.getAttribute("id"));
                    String name = e.getTextContent().trim();

                    idToName.put(id, name);
                    names.add(name);

                    String parentAttr = e.getAttribute("parentId");
                    if (parentAttr != null && !parentAttr.isBlank()) {
                        idToParentId.put(id, Long.parseLong(parentAttr));
                    }
                }

                Map<String, Category> byName = categoryService.getOrCreateByNames(names);

                Map<Long, Category> categoryMap = new HashMap<>();
                for (var entry : idToName.entrySet()) {
                    categoryMap.put(entry.getKey(), byName.get(entry.getValue()));
                }

                for (var entry : idToParentId.entrySet()) {
                    Category child = categoryMap.get(entry.getKey());
                    Category parent = categoryMap.get(entry.getValue());
                    if (child != null && parent != null && child.getParent() == null) {
                        child.setParent(parent);
                    }
                }

                em.flush();
                em.clear();

                // ====== Offers batch
                NodeList offerNodes = doc.getElementsByTagName("offer");

                final int PRODUCT_BATCH = 500;
                final int IMAGE_BATCH = 1000;

                List<Product> productBuffer = new ArrayList<>(PRODUCT_BATCH);
                Map<String, List<String>> picturesByProductId = new HashMap<>();
                List<ProductImage> imageBuffer = new ArrayList<>(IMAGE_BATCH);

                for (int i = 0; i < offerNodes.getLength(); i++) {
                    Element offer = (Element) offerNodes.item(i);

                    String name = getText(offer, "name");
                    String article = getText(offer, "article");
                    String description = getText(offer, "description").replaceAll("<[^>]*>", "").trim();
                    String priceStr = getText(offer, "price");
                    String catStr = getText(offer, "categoryId");

                    if (priceStr.isBlank() || catStr.isBlank() || article.isBlank()) continue;

                    double price = Double.parseDouble(priceStr);
                    Long catId = Long.parseLong(catStr);
                    Category category = categoryMap.get(catId);

                    Product product = Product.builder()
                            .id(article)
                            .name(name.length() > 500 ? name.substring(0, 500) : name)
                            .description(description)
                            .price(price)
                            .category(category)
                            .shop(shop)
                            .build();

                    productBuffer.add(product);

                    NodeList pics = offer.getElementsByTagName("picture");
                    if (pics.getLength() > 0) {
                        List<String> urls = new ArrayList<>(pics.getLength());
                        for (int j = 0; j < pics.getLength(); j++) {
                            String u = pics.item(j).getTextContent().trim();
                            if (!u.isBlank()) urls.add(u);
                        }
                        if (!urls.isEmpty()) picturesByProductId.put(article, urls);
                    }

                    if (productBuffer.size() == PRODUCT_BATCH) {
                        persistBatch(productBuffer, picturesByProductId, imageBuffer, IMAGE_BATCH);
                        productBuffer.clear();
                        picturesByProductId.clear();
                    }
                }

                if (!productBuffer.isEmpty()) {
                    persistBatch(productBuffer, picturesByProductId, imageBuffer, IMAGE_BATCH);
                }

                importSubmitService.markDone(importSubmitId);
            }

        } catch (Exception e) {
            importSubmitService.markFailed(importSubmitId, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Import failed: " + e.getMessage()
            );
        }
    }

    private void persistBatch(List<Product> productBuffer,
                              Map<String, List<String>> picturesByProductId,
                              List<ProductImage> imageBuffer,
                              int imageBatchSize) {

        productService.saveAll(productBuffer);
        productService.flush();
        em.flush();
        em.clear();

        for (Product p : productBuffer) {
            List<String> urls = picturesByProductId.get(p.getId());
            if (urls == null) continue;

            for (String url : urls) {
                try {
                    imageBuffer.add(productImageService.downloadAndBuild(p, url));
                    if (imageBuffer.size() >= imageBatchSize) {
                        productImageService.saveAllBatch(imageBuffer);
                        imageBuffer.clear();
                        em.flush();
                        em.clear();
                    }
                } catch (Exception ignored) {}
            }
        }

        if (!imageBuffer.isEmpty()) {
            productImageService.saveAllBatch(imageBuffer);
            imageBuffer.clear();
            em.flush();
            em.clear();
        }
    }

    private Document buildDoc(InputStream input) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(input);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "";
        Node node = nodes.item(0);
        return node != null ? node.getTextContent().trim() : "";
    }
}

