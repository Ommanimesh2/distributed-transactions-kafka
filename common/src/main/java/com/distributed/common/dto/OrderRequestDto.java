package com.distributed.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {
    private Integer orderId;
    private Integer userId;

    private Integer productId;
    private Integer quantity;
    private Integer amount;

}
