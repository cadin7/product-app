package group.rohlik.product.controller;

import com.github.fge.jsonpatch.JsonPatch;
import group.rohlik.product.model.api.Product;
import group.rohlik.product.model.mapper.ProductMapper;
import group.rohlik.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {

    private final ProductMapper mapper;
    private final ProductService service;

    @PostMapping
    Product createProduct(@RequestBody Product product) {
        return mapper.toApi(
                service.createProduct(
                        mapper.toEntity(product)));
    }

    @DeleteMapping("{productId}")
    Product deleteProduct(@PathVariable String productId) {
        return mapper.toApi(
                service.deleteProduct(productId));
    }

    @PatchMapping("{productId}")
    Product updateProduct(@PathVariable String productId, @RequestBody JsonPatch patch) {
        return mapper.toApi(
                service.updateProduct(productId, patch));
    }

    @GetMapping("{productId}")
    Product getProduct(@PathVariable String productId) {
        return mapper.toApi(
                service.getProduct(productId));
    }
}
