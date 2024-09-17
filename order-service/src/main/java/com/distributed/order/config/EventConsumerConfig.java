package com.distributed.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.distributed.common.event.PaymentEvent;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    @Autowired
    private OrderStatusUpdateHandler handler;

    /**
     * Returns a consumer of PaymentEvent that will update the
     * payment status of the corresponding order, based on the
     * payment status in the event.
     * 
     * @implNote This method expects spring.cloud.function.definition to be set to
     *           {methodName}
     * 
     * @return the consumer
     */
    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return (payment) -> handler.updateOrder(payment.getPaymentRequestDto().getOrderId(), po -> {
            po.setPaymentStatus(payment.getPaymentStatus());
        });
    }
}