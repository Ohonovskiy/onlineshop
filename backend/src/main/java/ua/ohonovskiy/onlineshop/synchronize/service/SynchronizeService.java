package ua.ohonovskiy.onlineshop.synchronize.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ua.ohonovskiy.onlineshop.product.model.category.Category;
import ua.ohonovskiy.onlineshop.product.model.product.Product;
import ua.ohonovskiy.onlineshop.product.service.category.CategoryService;
import ua.ohonovskiy.onlineshop.product.service.product.ProductService;
import ua.ohonovskiy.onlineshop.product.service.productImage.ProductImageService;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.shop.service.ShopService;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.UserService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SynchronizeService {

    private final UserService userService;
    private final ShopService shopService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductImageService productImageService;

    public void setImportFileForCurrentUser(String xmlURL) {
        User user = userService.getCurrentUser();
        Shop shop = user.getShop();

        if (shop == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no associated shop");
        }

        shop.setImportFileURL(xmlURL);
        shopService.save(shop);
    }

    @Transactional
    public void synchronize() {
        for(User user : userService.getAllShopOwners()) {
            importProducts(user);
        }
    }

    @Async
    @Transactional
    public void importProductsAsync(User user) {
        importProducts(user);
    }

    @Transactional
    public void importProducts(User user) {
        Shop shop = user.getShop();

        if (shop == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no associated shop");
        }
        if (shop.getImportFileURL() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop has no import file URL");
        }

        String importFileUrl = shop.getImportFileURL();

        try (InputStream input = new URL(importFileUrl).openStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            // === 1. Створюємо всі категорії без parent ===
            NodeList categoryNodes = doc.getElementsByTagName("category");
            Map<Long, Category> categoryMap = new HashMap<>();

            for (int i = 0; i < categoryNodes.getLength(); i++) {
                Element categoryElement = (Element) categoryNodes.item(i);
                Long categoryId = Long.parseLong(categoryElement.getAttribute("id"));
                String name = categoryElement.getTextContent().trim();

                Category category = categoryService.getByName(name)
                        .orElseGet(() -> categoryService.save(new Category(name, null)));

                categoryMap.put(categoryId, category);
            }

            // === 2. Другий прохід — оновлюємо parent ===
            for (int i = 0; i < categoryNodes.getLength(); i++) {
                Element categoryElement = (Element) categoryNodes.item(i);
                Long categoryId = Long.parseLong(categoryElement.getAttribute("id"));
                String parentIdAttr = categoryElement.getAttribute("parentId");

                if (!parentIdAttr.isBlank()) {
                    Long parentId = Long.parseLong(parentIdAttr);
                    Category child = categoryMap.get(categoryId);
                    Category parent = categoryMap.get(parentId);

                    if (child != null && parent != null && child.getParent() == null) {
                        child.setParent(parent);
                        categoryService.save(child);
                    }
                }
            }

            // === 3. Імпорт товарів ===
            NodeList offerNodes = doc.getElementsByTagName("offer");
            for (int i = 0; i < offerNodes.getLength(); i++) {
                Element offer = (Element) offerNodes.item(i);

                String name = getText(offer, "name");
                String description = getText(offer, "description").replaceAll("<[^>]*>", "").trim();
                String priceStr = getText(offer, "price");
                String categoryIdStr = getText(offer, "categoryId");

                if (priceStr.isBlank() || categoryIdStr.isBlank()) continue;

                double price = Double.parseDouble(priceStr);
                Long categoryId = Long.parseLong(categoryIdStr);
                Category category = categoryMap.get(categoryId);

                Product product = Product.builder()
                        .name(name.length() > 500 ? name.substring(0, 500) : name)
                        .description(description)
                        .price(price)
                        .shop(shop)
                        .category(category)
                        .build();

                product = productService.save(product);

                // === Зображення ===
                NodeList picNodes = offer.getElementsByTagName("picture");
                List<String> imageUrls = new ArrayList<>();
                for (int j = 0; j < picNodes.getLength(); j++) {
                    imageUrls.add(picNodes.item(j).getTextContent().trim());
                }

                productImageService.importImages(product, imageUrls);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to import products: " + e.getClass().getSimpleName() + " - " + e.getMessage()
            );
        }
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "";
        Node node = nodes.item(0);
        return node != null ? node.getTextContent().trim() : "";
    }

    @Transactional
    public void makeImportForCurrentUserAsync() {
        importProductsAsync(userService.getCurrentUser());
    }
}
