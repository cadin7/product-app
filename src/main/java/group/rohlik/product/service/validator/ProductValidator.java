package group.rohlik.product.service.validator;

import group.rohlik.product.exceptions.ProductValidationException;
import group.rohlik.product.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
public class ProductValidator {

    private static final String INVALID_PRODUCT_MESSAGE = "Product %s is invalid!";

    public void validateProduct(ProductEntity productEntity) {
        validate(productEntity)
                .ifPresent(validationException -> {
                    throw validationException;
                });
    }

    private Optional<ProductValidationException> validate(ProductEntity productEntity) {
        if (productEntity.getName().isBlank() || productEntity.getStockQuantity() < 1 ||
                productEntity.getUnitPrice() <= 0) {
            return of(new ProductValidationException(format(INVALID_PRODUCT_MESSAGE, productEntity)));
        } else {
            return empty();
        }
    }
}
