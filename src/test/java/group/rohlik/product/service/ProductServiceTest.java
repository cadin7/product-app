package group.rohlik.product.service;

import group.rohlik.product.exceptions.ProductNotFoundException;
import group.rohlik.product.model.entity.ProductEntity;
import group.rohlik.product.repository.ProductRepository;
import group.rohlik.product.service.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private static final String PRODUCT_NOT_FOUND = "Product with ID: %s was not found!";
    private static final String INVALID_PRODUCT_MESSAGE = "Product %s is invalid!";

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductValidator productValidator;
    @Captor
    private ArgumentCaptor<ProductEntity> entityArgumentCaptor;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_exceptSaveNewProduct() {
        ProductEntity productEntity = getValidProductEntity();

        when(productRepository.save(productEntity)).thenReturn(productEntity);

        ProductEntity resultEntity = productService.createProduct(productEntity);

        assertEquals(productEntity, resultEntity);
        verify(productRepository).save(productEntity);
        verify(productValidator).validateProduct(productEntity);
    }

    @Test
    void deleteProduct_exceptProductDeleted() {
        ProductEntity productEntity = getValidProductEntity();

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        ProductEntity resultEntity = productService.deleteProduct(productEntity.getId());

        assertEquals(productEntity, resultEntity);
        verify(productRepository).findById(productEntity.getId());
        verify(productRepository).delete(productEntity);
    }

    @Test
    void deleteProduct_exceptProductNotFoundException() {
        ProductEntity invalidProductEntity = getInvalidProductEntity();

        when(productRepository.findById(invalidProductEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(invalidProductEntity.getId()));

        String expectedMessage = format(PRODUCT_NOT_FOUND, invalidProductEntity.getId());

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void updateProductQuantity_expectUpdatedProduct() {
        ProductEntity productEntity = getValidProductEntity();

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        productService.updateProductQuantity(productEntity.getId(), 1);

        verify(productRepository).findById(productEntity.getId());
        verify(productRepository).save(entityArgumentCaptor.capture());

        ProductEntity savedEntity = entityArgumentCaptor.getValue();
        assertEquals(productEntity.getStockQuantity(), savedEntity.getStockQuantity());
    }

    @Test
    void updateProductQuantity_expectProductNotFoundException() {
        ProductEntity invalidProductEntity = getInvalidProductEntity();

        when(productRepository.findById(invalidProductEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class,
                () -> productService.updateProductQuantity(invalidProductEntity.getId(), 1));

        String expectedMessage = format(PRODUCT_NOT_FOUND, invalidProductEntity.getId());

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getProduct_expectProduct() {
        ProductEntity productEntity = getValidProductEntity();

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        ProductEntity resultEntity = productService.getProduct(productEntity.getId());

        assertEquals(productEntity, resultEntity);
        verify(productRepository).findById(productEntity.getId());
    }

    @Test
    void getProduct_expectProductNotFoundException() {
        ProductEntity invalidProductEntity = getInvalidProductEntity();

        when(productRepository.findById(invalidProductEntity.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProduct(invalidProductEntity.getId()));

        String expectedMessage = format(PRODUCT_NOT_FOUND, invalidProductEntity.getId());

        assertEquals(expectedMessage, exception.getMessage());
    }

    private ProductEntity getValidProductEntity() {
        return ProductEntity.builder()
                .id("1")
                .name("apple")
                .unitPrice(2.0)
                .stockQuantity(5)
                .version(1)
                .build();
    }

    private ProductEntity getInvalidProductEntity() {
        return ProductEntity.builder()
                .id("1")
                .name("apple")
                .unitPrice(0.0)
                .stockQuantity(0)
                .version(1)
                .build();
    }
}
