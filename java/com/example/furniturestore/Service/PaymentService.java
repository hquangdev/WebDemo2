package com.example.furniturestore.Service;

import com.example.furniturestore.Entity.Order;
import com.example.furniturestore.Entity.OrderItem;
import com.example.furniturestore.Entity.OurUsers;
import com.example.furniturestore.Entity.Product;
import com.example.furniturestore.Repotitory.OrderItemRepository;
import com.example.furniturestore.Repotitory.OrderRepository;
import com.example.furniturestore.Repotitory.OurUserRepo;
import com.example.furniturestore.Repotitory.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private OurUserRepo userRepository;

    // Xử lý thanh toán trực tiếp
    public boolean processPayment(Long userId, Map<String, Integer> cart) {
        // Tính tổng số tiền thanh toán từ giỏ hàng
        BigDecimal totalAmount = calculateTotalAmount(cart);

        OurUsers user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        boolean paymentSuccessful = true;

        if (paymentSuccessful) {
            saveOrder(user, cart, totalAmount);
        }

        return paymentSuccessful;
    }

    // Tính tổng số tiền thanh toán từ giỏ hàng
    private BigDecimal calculateTotalAmount(Map<String, Integer> cart) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            Long productId = Long.valueOf(entry.getKey());
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Cộng tổng tiền
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        return totalAmount;
    }

    // Lưu thông tin đơn hàng vào cơ sở dữ liệu
    private void saveOrder(OurUsers user, Map<String, Integer> cart, BigDecimal totalAmount) {
        // Tạo đối tượng đơn hàng
        Order order = new Order();
        order.setUserId(user.getId().toString());
        order.setTotalAmount(totalAmount);
        order.setStatus("Đã thanh toán");

        // Lưu đơn hàng vào cơ sở dữ liệu
        order = orderRepository.save(order);

        // Lưu các item trong đơn hàng (OrderItems)
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            Long productId = Long.valueOf(entry.getKey());
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);
        }
    }
}

