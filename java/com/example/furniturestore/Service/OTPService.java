package com.example.furniturestore.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MailService mailService;

    // Tạo OTP ngẫu nhiên
    public String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    // Gửi OTP qua email và lưu OTP vào Redis
    public void sendOtpToEmail(String email) {
        String otp = generateOtp();
        mailService.sendOtpEmail(email, otp);

        // Lưu OTP vào Redis, thời gian hết hạn 5p
        redisTemplate.opsForValue().set("otp:" + email, otp, 5, TimeUnit.MINUTES);
        System.out.println("OTP saved to Redis for email: " + email);
    }

    // Kiểm tra OTP từ Redis
    public boolean validateOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get("otp:" + email);
        return otp.equals(storedOtp);
    }
}
