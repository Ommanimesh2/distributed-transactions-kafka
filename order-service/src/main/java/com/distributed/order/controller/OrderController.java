package com.distributed.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distributed.common.dto.OrderRequestDto;
import com.distributed.order.entity.PurchaseOrder;
import com.distributed.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody OrderRequestDto orderRequestDto) {

        PurchaseOrder purchaseOrder = orderService.createOrder(orderRequestDto);

        return ResponseEntity.ok().body(purchaseOrder);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PurchaseOrder>> getAllOrders() {

        List<PurchaseOrder> purchaseOrders = orderService.getAllOrders();
        return ResponseEntity.ok().body(purchaseOrders);
    }
}
