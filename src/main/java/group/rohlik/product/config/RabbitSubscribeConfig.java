package group.rohlik.product.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
public class RabbitSubscribeConfig {

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("rohlik.direct");
    }

    @Bean
    Queue directQueue() {
        return new AnonymousQueue();
    }

    @Bean
    Binding directBinding(DirectExchange directExchange, Queue directQueue) {
        return bind(directQueue).to(directExchange).with("quantity.update");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
