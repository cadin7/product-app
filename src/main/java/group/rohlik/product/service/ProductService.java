package group.rohlik.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import group.rohlik.product.exceptions.ProductNotFoundException;
import group.rohlik.product.model.entity.ProductEntity;
import group.rohlik.product.repository.ProductRepository;
import group.rohlik.product.service.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String PRODUCT_NOT_FOUND = "Product with ID: %s was not found!";

    private final ObjectMapper mapper;
    private final ProductValidator validator;
    private final ProductRepository repository;

    public ProductEntity createProduct(ProductEntity productEntity) {
        validator.validateProduct(productEntity);

        return repository.save(productEntity);
    }

    public ProductEntity deleteProduct(String productId) {
        var productToDelete = repository.findById(productId);

        productToDelete.ifPresent(this::deleteExistingProduct);

        return productToDelete.orElseThrow(
                () -> new ProductNotFoundException(format(PRODUCT_NOT_FOUND, productId)));
    }

    @SneakyThrows
    public ProductEntity updateProduct(String productId, JsonPatch patch) {
        var dbProduct = getProductOrThrow(productId);
        var patchedProductJson = patch.apply(mapper.valueToTree(dbProduct));
        var patchedProduct = mapper.treeToValue(patchedProductJson, ProductEntity.class);

        validator.validateProduct(patchedProduct);
        copyProduct(patchedProduct, dbProduct);

        return repository.save(dbProduct);
    }

    public void updateProductQuantity(String productId, Integer quantity) {
        var productEntity = getProductOrThrow(productId);

        productEntity.setStockQuantity(productEntity.getStockQuantity() - quantity);

        repository.save(productEntity);
    }

    public ProductEntity getProduct(String productId) {
        return getProductOrThrow(productId);
    }

    private void deleteExistingProduct(ProductEntity productEntity) {
        log.info("Deleting product: " + productEntity);

        repository.delete(productEntity);
    }

    private ProductEntity getProductOrThrow(String productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(format(PRODUCT_NOT_FOUND, productId)));
    }

    private void copyProduct(ProductEntity newProduct, ProductEntity dbProduct) {
        log.info("Copying product: " + newProduct);

        dbProduct.setName(newProduct.getName());
        dbProduct.setStockQuantity(newProduct.getStockQuantity());
        dbProduct.setUnitPrice(newProduct.getUnitPrice());
    }
}
