package com.example.furniturestore.Controller;

import com.example.furniturestore.Service.CartService;
import com.example.furniturestore.Service.JWTUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JWTUtils jwtUtils;

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/home/add-cart")
    public String addItemToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        String sessionId = session.getId();

        return cartService.addItemToCart(sessionId, productId, quantity);
    }

    // Xem giỏ hàng của người dùng (dựa vào sessionId)
    @GetMapping("/home/view")
    public Map<String, Object> viewCart(HttpSession session) {
        String sessionId = session.getId();

        System.out.println("giỏ hàng:" + sessionId);
        return cartService.getCart(sessionId);
    }

    // Xử lý thanh toán và lưu đơn hàng vào DB
    @PostMapping("/user/checkout")
    public ResponseEntity<String> checkout(@RequestHeader(value = "Authorization", required = false) String token,
                                           @RequestParam String address, @RequestParam String phone,
                                           @RequestParam String note, HttpSession session) {

        System.out.println("Địa chỉ: " + address);
        String sessionId = session.getId();
        System.out.println("Session ID: " + sessionId);
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập. Vui lòng đăng nhập trước khi thanh toán.");
        }

        // Trích xuất userId từ token
        String jwtToken = token.substring(7);
        String userId;
        try {
            userId = jwtUtils.extractUserId(jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại.");
        }

        String result = cartService.checkout(sessionId, userId, address, phone, note);
        if (result.equals("Giỏ hàng trống. Không thể thanh toán.")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        return ResponseEntity.ok(result);
    }

    // Xóa giỏ hàng sau khi thanh toán
    @DeleteMapping("/clear")
    public String clearCart(HttpSession session) {
        String sessionId = session.getId();
        cartService.clearCart(sessionId);
        return "Giỏ hàng đã được xóa.";
    }

}


