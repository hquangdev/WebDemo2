package com.example.furniturestore.Entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String name;
    private String phone;
    private String email;
    private String address;
    private String notes;
    private Double totalPrice;
    private String status;
    private List<OrderItem> orderItems;

}
