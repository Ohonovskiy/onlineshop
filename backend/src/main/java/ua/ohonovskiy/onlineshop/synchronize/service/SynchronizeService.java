package ua.ohonovskiy.onlineshop.synchronize.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ua.ohonovskiy.onlineshop.product.category.model.Category;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.category.service.CategoryService;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.product.productImage.service.ProductImageService;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SynchronizeService {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ImportSubmitService importSubmitService;


    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void checkImportQueue() {

        Optional<ImportSubmit> next = importSubmitService
                .getEldestAccepted();

        if (next.isEmpty()) {
            return;
        }

        ImportSubmit task = next.get();

        // Блокуємо задачу для інших воркерів
        task.setImportStatus(ImportStatus.STATUS_PROCESSING);
        importSubmitService.save(task);

        // Запускаємо асинхронно
        importProductsAsync(task);
    }

    // ================== ASYNC IMPORT ==================

    @Async
    @Transactional
    public void importProductsAsync(ImportSubmit importSubmit) {
        importProducts(importSubmit);
    }

    // ================== IMPORT LOGIC ==================

    @Transactional
    public void importProducts(ImportSubmit importSubmit) {
        Shop shop = importSubmit.getShop();

        if (shop == null) {
            fail(importSubmit, "User shop not found");
        }

        String importFileUrl = shop.getImportFileURL();
        if (importFileUrl == null) {
            fail(importSubmit, "Shop import URL missing");
        }

        importSubmit.setImportStatus(ImportStatus.STATUS_PROCESSING);
        importSubmitService.save(importSubmit);

        try (InputStream input = new URL(importFileUrl).openStream()) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            // === 1. Категорії (базове створення)
            NodeList categoryNodes = doc.getElementsByTagName("category");
            Map<Long, Category> categoryMap = new HashMap<>();

            for (int i = 0; i < categoryNodes.getLength(); i++) {
                Element categoryElement = (Element) categoryNodes.item(i);
                Long id = Long.parseLong(categoryElement.getAttribute("id"));
                String name = categoryElement.getTextContent().trim();

                Category c = categoryService.getByName(name)
                        .orElseGet(() -> categoryService.save(new Category(name, null)));

                categoryMap.put(id, c);
            }

            // === 2. Категорії (встановлюємо parent)
            for (int i = 0; i < categoryNodes.getLength(); i++) {
                Element e = (Element) categoryNodes.item(i);
                Long id = Long.parseLong(e.getAttribute("id"));
                String parentAttr = e.getAttribute("parentId");

                if (!parentAttr.isBlank()) {
                    Long parentId = Long.parseLong(parentAttr);

                    Category child = categoryMap.get(id);
                    Category parent = categoryMap.get(parentId);

                    if (child != null && parent != null && child.getParent() == null) {
                        child.setParent(parent);
                        categoryService.save(child);
                    }
                }
            }

            // === 3. Товари
            NodeList offerNodes = doc.getElementsByTagName("offer");

            for (int i = 0; i < offerNodes.getLength(); i++) {
                Element offer = (Element) offerNodes.item(i);

                String name = getText(offer, "name");
                String article = getText(offer, "article");
                String description = getText(offer, "description").replaceAll("<[^>]*>", "").trim();
                String priceStr = getText(offer, "price");
                String catStr = getText(offer, "categoryId");

                if (priceStr.isBlank() || catStr.isBlank()) continue;

                double price = Double.parseDouble(priceStr);
                Long catId = Long.parseLong(catStr);

                Category category = categoryMap.get(catId);

                Product product = Product.builder()
                        .name(name.length() > 500 ? name.substring(0, 500) : name)
                        .description(description)
                        .price(price)
                        .id(article)
                        .category(category)
                        .build();

                product = productService.save(product);

                // Зображення
                NodeList pics = offer.getElementsByTagName("picture");
                List<String> imageUrls = new ArrayList<>();

                for (int j = 0; j < pics.getLength(); j++) {
                    imageUrls.add(pics.item(j).getTextContent().trim());
                }

                productImageService.importImages(product, imageUrls);
            }

            // SUCCESS
            importSubmit.setImportStatus(ImportStatus.STATUS_DONE);
            importSubmitService.save(importSubmit);

        } catch (Exception e) {
            fail(importSubmit, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Import failed: " + e.getMessage()
            );
        }
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "";
        Node node = nodes.item(0);
        return node != null ? node.getTextContent().trim() : "";
    }

    private void fail(ImportSubmit submit, String reason) {
        submit.setImportStatus(ImportStatus.STATUS_FAILED);
        importSubmitService.save(submit);
    }
}
