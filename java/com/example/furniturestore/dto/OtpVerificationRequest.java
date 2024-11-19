package com.example.furniturestore.dto;


import lombok.Data;

@Data
public class OtpVerificationRequest {
        private String email;
        private String otp;
}
