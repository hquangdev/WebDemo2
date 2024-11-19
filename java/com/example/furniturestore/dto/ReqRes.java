package com.example.furniturestore.dto;

import com.example.furniturestore.Entity.OurUsers;
import com.example.furniturestore.Entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String role;
    private Object  data;

    @Size(min = 8, message = "Mật khẩu phải trênn 8 kí tự")
    private String password;
//    private List<Product> products;
    private OurUsers ourUsers;
}