package group.rohlik.product.model.mapper;

import group.rohlik.product.model.api.Product;
import group.rohlik.product.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements Mapper<Product, ProductEntity> {

    @Override
    public Product toApi(ProductEntity source) {
        if (source == null) {
            return null;
        }

        var target = new Product();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setStockQuantity(source.getStockQuantity());
        target.setUnitPrice(source.getUnitPrice());

        return target;
    }

    @Override
    public ProductEntity toEntity(Product source) {
        if (source == null) {
            return null;
        }

        var target = new ProductEntity();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setStockQuantity(source.getStockQuantity());
        target.setUnitPrice(source.getUnitPrice());

        return target;
    }
}
