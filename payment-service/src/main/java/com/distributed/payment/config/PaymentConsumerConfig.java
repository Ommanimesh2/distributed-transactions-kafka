package com.distributed.payment.config;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.distributed.common.enums.OrderStatus;
import com.distributed.common.event.OrderEvent;
import com.distributed.common.event.PaymentEvent;
import com.distributed.payment.service.PaymentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class PaymentConsumerConfig {

    @Autowired
    private PaymentService paymentService;

    /**
     * Returns a function that processes a flux of OrderEvent and returns a flux of
     * PaymentEvent. The processing is done by flat mapping the OrderEvent flux to
     * the processPayment method, which returns a Mono of PaymentEvent. The
     * processPayment method is responsible for processing the OrderEvent and
     * returning the corresponding PaymentEvent.
     * 
     * @implNote This method expects spring.cloud.function.definition to be set to
     *           {methodName}
     * 
     * @return a function that processes a flux of OrderEvent and returns a flux of
     *         PaymentEvent
     */
    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return orderEventFlux -> orderEventFlux.flatMap(this::processPayment);
    }

    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent) {

        if (OrderStatus.CREATED.equals(orderEvent.getOrderStatus())) {

            return Mono.fromSupplier(() -> this.paymentService.newOrderEvent(orderEvent));
        } else {
            return Mono.fromRunnable(() -> this.paymentService.cancelOrderEvent(orderEvent));
        }
    }
}
