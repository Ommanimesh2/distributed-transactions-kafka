package com.distributed.common.dto;

import com.distributed.common.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Integer orderId;
    private Integer productId;
    private Integer userId;

    private Integer quantity;
    private Integer amount;
    private OrderStatus orderStatus;

}
