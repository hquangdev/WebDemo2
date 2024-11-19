package com.example.furniturestore.Controller;

import com.example.furniturestore.Service.AuthService;
import com.example.furniturestore.dto.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    // API đăng ký và gửi OTP
    @PostMapping("/register")
    public ReqRes registerUser(@RequestBody ReqRes reqRes) {
        return authService.registerUserWithOtp(reqRes);
    }

    // API xác thực OTP và hoàn tất đăng ký
    @PostMapping("/verify")
    public ReqRes verifyOtp(@RequestParam String email,
                            @RequestParam String name,
                            @RequestParam String otp,
                            @RequestParam String password,
                            @RequestParam String role) {
        return authService.verifyOtpAndSaveUser(email, name, otp, password, role);
    }

    @PostMapping("/login")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }



}