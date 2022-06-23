package group.rohlik.product.listener;

import group.rohlik.product.event.ProductEvent;
import group.rohlik.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventListener {

    private final ProductService service;

    @RabbitListener(queues = "#{directQueue.name}")
    public void listenProductUpdate(ProductEvent event) {
        log.info("Updating product with ID: " + event.productId());

        service.updateProductQuantity(event.productId(), event.quantity());
    }

}
