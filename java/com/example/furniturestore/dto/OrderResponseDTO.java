package com.example.furniturestore.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private String userId;
    private BigDecimal totalAmount;
    private String status;
    private String address;
    private String phone;
    private String note;
    private List<OrderItemDTO> orderItems;
}
