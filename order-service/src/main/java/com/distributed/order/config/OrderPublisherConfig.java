package com.distributed.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.distributed.common.event.OrderEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class OrderPublisherConfig {

    @Bean
    public Sinks.Many<OrderEvent> orderSinks() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    /**
     * Returns a supplier of Flux of OrderEvent. The Flux is created from the
     * Sinks.Many
     * instance which is used to publish the OrderEvent instances. The Flux is used
     * to
     * subscribe to the published events and react to them.
     * 
     * @implNote This method expects spring.cloud.function.definition to be set to
     *           {methodName}
     * 
     * @param sinks the Sinks.Many instance used to publish the OrderEvent instances
     * @return a supplier of Flux of OrderEvent
     */
    @Bean
    public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sinks) {
        return sinks::asFlux;
    }
}