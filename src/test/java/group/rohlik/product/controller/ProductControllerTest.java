package group.rohlik.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.rohlik.product.model.api.Product;
import group.rohlik.product.model.entity.ProductEntity;
import group.rohlik.product.model.mapper.ProductMapper;
import group.rohlik.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {ProductController.class, ProductService.class})
public class ProductControllerTest {

    private static final String PRODUCTS_URL = "/products";
    public static final String SLASH = "/";

    @MockBean
    private ProductService productService;
    @MockBean
    private ProductMapper productMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private Product product;
    private ProductEntity productEntity;


    @BeforeEach
    public void setup() {
        product = getProduct();
        productEntity = getProductEntity();
    }

    @Test
    void createProduct_expectProductCreatedAndHttpStatusOk() throws Exception {
        when(productMapper.toEntity(product)).thenReturn(productEntity);
        when(productMapper.toApi(productEntity)).thenReturn(product);
        when(productService.createProduct(productEntity)).thenReturn(productEntity);

        mockMvc.perform(post(PRODUCTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.unitPrice").value(product.getUnitPrice()))
                .andExpect(jsonPath("$.stockQuantity").value(product.getStockQuantity()));

        verify(productService).createProduct(productEntity);
        verify(productMapper).toEntity(product);
        verify(productMapper).toApi(productEntity);
    }

    @Test
    void deleteProduct_expectProductDeletedAndHttpStatusOk() throws Exception {
        when(productMapper.toApi(productEntity)).thenReturn(product);
        when(productService.deleteProduct(productEntity.getId())).thenReturn(productEntity);

        mockMvc.perform(delete(PRODUCTS_URL + SLASH + productEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.unitPrice").value(product.getUnitPrice()))
                .andExpect(jsonPath("$.stockQuantity").value(product.getStockQuantity()));

        verify(productService).deleteProduct(productEntity.getId());
        verify(productMapper).toApi(productEntity);
    }

    @Test
    void getProduct_expectProductAndHttpStatusOk() throws Exception {
        when(productMapper.toApi(productEntity)).thenReturn(product);
        when(productService.getProduct(productEntity.getId())).thenReturn(productEntity);

        mockMvc.perform(get(PRODUCTS_URL + "/" + productEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.unitPrice").value(product.getUnitPrice()))
                .andExpect(jsonPath("$.stockQuantity").value(product.getStockQuantity()));

        verify(productService).getProduct(productEntity.getId());
        verify(productMapper).toApi(productEntity);
    }

    private Product getProduct() {
        Product product = new Product();
        product.setId("1");
        product.setName("apple");
        product.setUnitPrice(5.0);
        product.setStockQuantity(5);
        return product;
    }

    private ProductEntity getProductEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId("1");
        productEntity.setName("apple");
        productEntity.setUnitPrice(5.0);
        productEntity.setStockQuantity(5);
        return productEntity;
    }
}
