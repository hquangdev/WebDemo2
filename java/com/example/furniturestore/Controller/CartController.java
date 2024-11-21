package com.example.furniturestore.Controller;

import com.example.furniturestore.Service.CartService;
import com.example.furniturestore.Service.JWTUtils;
import com.example.furniturestore.Service.PaymentService;
import com.example.furniturestore.dto.OrderRequest;
import com.example.furniturestore.dto.OrderResponseDTO;
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

    @Autowired
    private PaymentService paymentService;

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

    @PostMapping("/user/checkout")
    public ResponseEntity<?> checkout(@RequestHeader(value = "Authorization", required = false) String token,
                                      @RequestBody OrderRequest orderRequest,
                                      HttpSession session) {

        String sessionId = session.getId();

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập. Vui lòng đăng nhập trước khi thanh toán.");
        }

        // Trích xuất userId từ token
        String jwtToken = token.substring(7);
        String userId;
        try {
            userId = jwtUtils.extractUserId(jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại.");
        }

        // Gọi service xử lý thanh toán
        OrderResponseDTO orderResponse = paymentService.processCheckout(userId, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

}


