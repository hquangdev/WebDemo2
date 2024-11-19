package com.example.furniturestore.Service;

import com.example.furniturestore.Entity.OurUsers;
import com.example.furniturestore.Repotitory.OurUserRepo;
import com.example.furniturestore.dto.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OTPService otpService;


    //lây danh sách
    public List<OurUsers> getAllUser(){
        return ourUserRepo.findAll();
    }

    // Đăng ký người dùng
    public ReqRes registerUserWithOtp(ReqRes reqRes) {
        ReqRes resp = new ReqRes();

        //ktra email đã được đăng ký hay chưa
        if (ourUserRepo.findByEmail(reqRes.getEmail()) != null) {
            resp.setMessage("Email đã được đăng ký.");
            resp.setStatusCode(400);
            return resp;
        }

        // Gửi OTP nếu email chưa được đăng ký
        otpService.sendOtpToEmail(reqRes.getEmail());
        resp.setMessage("OTP đã được gửi đến email của bạn. Vui lòng xác thực.");
        System.out.println("Mã otp là: " + resp );
        resp.setStatusCode(200);

        return resp;
    }

    // Xác thực OTP và hoàn tất đăng ký
    public ReqRes verifyOtpAndSaveUser(String email, String name, String otp, String password, String role) {
        ReqRes resp = new ReqRes();

        // Kiểm tra mã OTP
        if (!otpService.validateOtp(email, otp)) {
            resp.setMessage("Mã OTP không đúng hoặc đã hết hạn.");
            resp.setStatusCode(400);
            return resp;
        }

        //  lưu người dùng vào cơ sở dữ liệu
        OurUsers ourUsers = new OurUsers();
        ourUsers.setName(name);
        ourUsers.setEmail(email);
        ourUsers.setPassword(passwordEncoder.encode(password));
        ourUsers.setRole(role);


        OurUsers savedUser = ourUserRepo.save(ourUsers);

        if (savedUser != null) {
            resp.setOurUsers(savedUser);
            resp.setMessage("Đăng ký thành công.");
            resp.setStatusCode(200);
        } else {
            resp.setMessage("Lỗi khi lưu người dùng.");
            resp.setStatusCode(500);
        }

        return resp;
    }


    public ReqRes signIn(ReqRes signinRequest){
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
            var user = ourUserRepo.findByEmail(signinRequest.getEmail());

            System.out.println("USER IS: "+ user);

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Đăng nhập thanh công");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        OurUsers users = ourUserRepo.findByEmail(ourEmail);
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }
}
