package com.distributed.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distributed.common.dto.OrderRequestDto;
import com.distributed.common.enums.OrderStatus;
import com.distributed.common.event.OrderEvent;

import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPubliser {

    @Autowired
    private Sinks.Many<OrderEvent> orderSinks;

    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderStatus(orderStatus);
        orderEvent.setOrderRequestDto(orderRequestDto);
        orderSinks.tryEmitNext(orderEvent);
    }
}
