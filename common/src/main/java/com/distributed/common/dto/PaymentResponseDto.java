package com.distributed.common.dto;

import com.distributed.common.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private Integer orderId;
    private Integer amount;
    private Integer userId;
}
