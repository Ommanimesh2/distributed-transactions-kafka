package com.distributed.common.event;

import java.util.Date;
import java.util.UUID;

import com.distributed.common.dto.OrderRequestDto;
import com.distributed.common.dto.PaymentRequestDto;
import com.distributed.common.enums.OrderStatus;
import com.distributed.common.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentEvent {

    private UUID eventId = UUID.randomUUID();
    private Date date = new Date();
    private PaymentRequestDto paymentRequestDto;
    private PaymentStatus paymentStatus;

    public PaymentEvent(PaymentRequestDto paymentRequestDto, PaymentStatus paymentStatus) {
        this.paymentRequestDto = paymentRequestDto;
        this.paymentStatus = paymentStatus;
    }

}
