package group.rohlik.product.event;

public record ProductEvent(
        String productId,
        Integer quantity
) {
}
