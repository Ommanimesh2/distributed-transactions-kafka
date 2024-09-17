package com.distributed.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distributed.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
