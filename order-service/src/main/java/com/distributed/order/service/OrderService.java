package com.distributed.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distributed.common.dto.OrderRequestDto;
import com.distributed.common.enums.OrderStatus;
import com.distributed.order.entity.PurchaseOrder;
import com.distributed.order.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusPubliser orderStatusPubliser;

    /**
     * Creates a new order and notifies the order status publisher of the CREATED
     * status.
     *
     * @param orderRequestDto the order request DTO
     * @return the created order
     */
    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDto) {

        PurchaseOrder order = orderRepository.save(convertDtoToEntity(orderRequestDto));

        orderRequestDto.setOrderId(order.getId());
        orderStatusPubliser.publishOrderEvent(orderRequestDto, OrderStatus.CREATED);
        return order;
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    private PurchaseOrder convertDtoToEntity(OrderRequestDto orderRequestDto) {
        return PurchaseOrder.builder().amount(orderRequestDto.getAmount()).productId(orderRequestDto.getProductId())
                .userId(orderRequestDto.getUserId()).quantity(orderRequestDto.getQuantity())
                .orderStatus(OrderStatus.CREATED)
                .build();
    }
}
