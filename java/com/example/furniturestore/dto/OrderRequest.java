package com.example.furniturestore.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OrderRequest {

    private String address;
    private String phone;
    private String note;
    private Map<Long, Integer> cart;

}
