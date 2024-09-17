package com.distributed.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distributed.payment.entity.UserTransaction;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {

}
