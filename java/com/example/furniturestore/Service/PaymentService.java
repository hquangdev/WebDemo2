package com.example.furniturestore.Service;

import com.example.furniturestore.Entity.Order;
import com.example.furniturestore.Entity.OrderItem;
import com.example.furniturestore.Entity.OurUsers;
import com.example.furniturestore.Entity.Product;
import com.example.furniturestore.Repotitory.OrderItemRepository;
import com.example.furniturestore.Repotitory.OrderRepository;
import com.example.furniturestore.Repotitory.OurUserRepo;
import com.example.furniturestore.Repotitory.ProductRepo;
import com.example.furniturestore.dto.OrderItemDTO;
import com.example.furniturestore.dto.OrderRequest;
import com.example.furniturestore.dto.OrderResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // Tính tổng số tiền thanh toán từ giỏ hàng
    private BigDecimal calculateTotalAmount(Map<Long, Integer> cart) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = Long.valueOf(entry.getKey());
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Cộng tổng tiền
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        return totalAmount;
    }

    // Xử lý thanh toán trực tiếp
    @Transactional
    public OrderResponseDTO processCheckout(String userId, OrderRequest orderRequest) {
        // 1. Kiểm tra giỏ hàng
        Map<Long, Integer> cart = orderRequest.getCart();
        if (cart == null || cart.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống. Không thể thanh toán.");
        }

        // 2. Tính tổng tiền
        BigDecimal totalAmount = calculateTotalAmount(cart);

        // 3. Lấy thông tin người dùng
        OurUsers user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // 4. Lưu đơn hàng
        Order order = saveOrder(user, cart, totalAmount, orderRequest);

        // 5. Trả về thông tin đơn hàng dưới dạng DTO
        return mapOrderToResponseDTO(order);
    }

    private OrderResponseDTO mapOrderToResponseDTO(Order order) {
        // Tạo đối tượng OrderResponseDTO và ánh xạ thông tin
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(order.getId());
        responseDTO.setUserId(order.getUserId());
        responseDTO.setTotalAmount(order.getTotalAmount());
        responseDTO.setStatus(order.getStatus());
        responseDTO.setAddress(order.getAddress());
        responseDTO.setPhone(order.getPhone());
        responseDTO.setNote(order.getNote());

        // Duyệt qua danh sách OrderItem để lấy thông tin các sản phẩm trong đơn hàng
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(orderItem -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(orderItem.getProduct().getId());
            itemDTO.setProductName(orderItem.getProduct().getName());
            itemDTO.setPrice(orderItem.getPrice());
            itemDTO.setQuantity(orderItem.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList());

        responseDTO.setOrderItems(itemDTOs);
        return responseDTO;
    }


    // Lưu thông tin đơn hàng vào cơ sở dữ liệu
    private Order saveOrder(OurUsers user, Map<Long, Integer> cart, BigDecimal totalAmount, OrderRequest orderRequest) {
        // 1. Tạo đối tượng Order
        Order order = new Order();
        order.setUserId(user.getId().toString());
        order.setTotalAmount(totalAmount);
        order.setStatus("Đã thanh toán");
        order.setAddress(orderRequest.getAddress());
        order.setPhone(orderRequest.getPhone());
        order.setNote(orderRequest.getNote());

        // Lưu đơn hàng
        order = orderRepository.save(order);

        // 2. Tạo các OrderItem và lưu vào DB
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);
        }

        return order;
    }

}

