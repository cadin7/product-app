package group.rohlik.product.bootstrap;

import group.rohlik.product.model.entity.ProductEntity;
import group.rohlik.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductService service;

    @Override
    public void run(String... args) {
        service.createProduct(
                ProductEntity.builder()
                        .name("apple")
                        .stockQuantity(1)
                        .unitPrice(2.0)
                        .build()
        );
        service.createProduct(
                ProductEntity.builder()
                        .name("croissant")
                        .stockQuantity(10)
                        .unitPrice(7.0)
                        .build()
        );
        service.createProduct(
                ProductEntity.builder()
                        .name("onion")
                        .stockQuantity(25)
                        .unitPrice(4.0)
                        .build()
        );
        service.createProduct(
                ProductEntity.builder()
                        .name("paprika")
                        .stockQuantity(5)
                        .unitPrice(1.5)
                        .build()
        );
    }
}
