package com.distributed.payment.service;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distributed.common.dto.OrderRequestDto;
import com.distributed.common.dto.PaymentRequestDto;
import com.distributed.common.enums.PaymentStatus;
import com.distributed.common.event.OrderEvent;
import com.distributed.common.event.PaymentEvent;
import com.distributed.payment.entity.Payment;
import com.distributed.payment.entity.UserTransaction;
import com.distributed.payment.repository.PaymentRepository;
import com.distributed.payment.repository.UserTransactionRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @PostConstruct
    public void init() {
        paymentRepository
                .saveAll(Stream.of(new Payment(1, 1000), new Payment(2, 2000), new Payment(3, 3000))
                        .collect(Collectors.toList()));
    }

    /**
     * Handles a new order event and updates the user balance accordingly. If the
     * user balance is sufficient for the order, the balance is updated and a
     * payment event with status SUCCESS is returned. If the user balance is not
     * sufficient,
     * a payment event with status FAILED is returned.
     *
     * @param orderEvent the order event
     * @return the payment event
     */
    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {

        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .amount(orderRequestDto.getAmount())
                .orderId(orderRequestDto.getOrderId())
                .userId(orderRequestDto.getUserId())
                .build();

        return paymentRepository.findById(orderRequestDto.getUserId())
                .filter(userBalance -> isSufficientBalance(userBalance, orderRequestDto.getAmount()))
                .map(userBalance -> processPayment(userBalance, orderRequestDto))
                .orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.FAILED));
    }

    private boolean isSufficientBalance(Payment userBalance, Integer orderAmount) {
        return userBalance.getAmount().compareTo(orderAmount) > 0;
    }

    private PaymentEvent processPayment(Payment userBalance, OrderRequestDto orderRequestDto) {
        Integer newBalance = userBalance.getAmount() - orderRequestDto.getAmount();
        userBalance.setAmount(newBalance);

        userTransactionRepository.save(
                UserTransaction.builder()
                        .amount(newBalance)
                        .orderId(orderRequestDto.getOrderId())
                        .userId(orderRequestDto.getUserId())
                        .build());

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .amount(orderRequestDto.getAmount())
                .orderId(orderRequestDto.getOrderId())
                .userId(orderRequestDto.getUserId())
                .build();

        return new PaymentEvent(paymentRequestDto, PaymentStatus.SUCCESS);
    }

    /**
     * Cancels an order event, by reverting the user balance change associated with
     * the order, and deleting the user transaction associated with the order.
     *
     * @param orderEvent the order event to cancel
     */
    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {

        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();
        Integer userId = orderRequestDto.getUserId();

        userTransactionRepository.findById(userId).ifPresent(userTransaction -> {
            userTransactionRepository.delete(userTransaction);

            paymentRepository.findById(userId).ifPresent(payment -> {
                Integer updatedAmount = payment.getAmount() + userTransaction.getAmount();
                payment.setAmount(updatedAmount);
                paymentRepository.save(payment);
            });
        });
    }
}
