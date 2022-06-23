package group.rohlik.product.service;

import group.rohlik.product.model.entity.ProductEntity;
import group.rohlik.product.service.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

public class ProductValidatorTest {

    private static final String INVALID_PRODUCT_MESSAGE = "Product %s is invalid!";

    @Mock
    private ProductValidator productValidator;
    @Captor
    private ArgumentCaptor<ProductEntity> productEntityArgumentCaptor;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateProduct_expectValid() {
        ProductEntity productEntity = getValidProductEntity();

        productValidator.validateProduct(productEntity);

        verify(productValidator).validateProduct(productEntityArgumentCaptor.capture());

        ProductEntity validated = productEntityArgumentCaptor.getValue();

        assertFalse(validated.getName().isBlank());
        assertFalse(validated.getStockQuantity() < 1);
        assertFalse(validated.getUnitPrice() <= 0);
    }

    private ProductEntity getValidProductEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId("1");
        productEntity.setName("apple");
        productEntity.setUnitPrice(5.0);
        productEntity.setStockQuantity(5);
        return productEntity;
    }

    private ProductEntity getInvalidProductEntity() {
        return ProductEntity.builder()
                .id("1")
                .name("")
                .unitPrice(0.0)
                .stockQuantity(0)
                .version(1)
                .build();
    }
}
