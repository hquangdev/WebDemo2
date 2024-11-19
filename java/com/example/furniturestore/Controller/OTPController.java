package com.example.furniturestore.Controller;
import com.example.furniturestore.Service.OTPService;
import com.example.furniturestore.dto.OtpVerificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class OTPController {

    @Autowired
    private OTPService otpService;

    // Endpoint để gửi OTP tới email
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody String email) {
        otpService.sendOtpToEmail(email);
        return ResponseEntity.ok("OTP đã được gửi tới email của bạn");
    }

    // Endpoint để xác thực OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());

        if (isValid) {
            return ResponseEntity.ok("OTP hợp lệ");
        } else {
            return ResponseEntity.status(400).body("OTP không hợp lệ hoặc đã hết hạn");
        }
    }


}
