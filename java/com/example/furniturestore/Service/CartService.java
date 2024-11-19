package com.example.furniturestore.Service;

import com.example.furniturestore.Entity.Order;
import com.example.furniturestore.Entity.OrderItem;
import com.example.furniturestore.Entity.Product;
import com.example.furniturestore.Repotitory.OrderItemRepository;
import com.example.furniturestore.Repotitory.OrderRepository;
import com.example.furniturestore.Repotitory.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private static final String CART_PREFIX = "cart:";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Integer> hashOperations;
    private ProductRepo productRepo;
    private OrderRepository orderRepo;
    private OrderItemRepository orderItemRepo;

    @Autowired
    public CartService(RedisTemplate<String, Object> redisTemplate,
                       ProductRepo productRepo,
                       OrderRepository orderRepo,
                       OrderItemRepository orderItemRepo) {
        this.redisTemplate = redisTemplate;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.hashOperations = redisTemplate.opsForHash();
    }

    // Thêm sản phẩm vào giỏ hàng (lưu vào Redis)
    public String addItemToCart(String sessionId, Long productId, int quantity) {
        String cartKey = CART_PREFIX + sessionId;
        String productKey = String.valueOf(productId);

        // Kiểm tra nếu sản phẩm đã có trong giỏ hàng thì cộng dồn số lượng
        if (hashOperations.hasKey(cartKey, productKey)) {
            quantity += hashOperations.get(cartKey, productKey);
        }

        hashOperations.put(cartKey, productKey, quantity);

        return "Sản phẩm đã được thêm vào giỏ hàng.";
    }

    // Lấy giỏ hàng của người dùng từ Redis và trả về thông tin chi tiết của sản phẩm
    public Map<String, Object> getCart(String sessionId) {
        String cartKey = CART_PREFIX + sessionId;
        Map<String, Integer> cartItems = hashOperations.entries(cartKey);
        if (cartItems.isEmpty()) {
            System.out.println("Giỏ hàng trống.");
        } else {
            System.out.println("Giỏ hàng có các sản phẩm: " + cartItems);
        }

        Map<String, Object> cartDetails = new HashMap<>();
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            String productId = entry.getKey();
            Integer quantity = entry.getValue();

            // Lấy thông tin chi tiết của sản phẩm từ cơ sở dữ liệu
            Product product = productRepo.findById(Long.parseLong(productId)).orElse(null);
            if (product != null) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("name", product.getName());
                productDetails.put("price", product.getPrice());
                productDetails.put("details", product.getDetails());
                productDetails.put("quantity", quantity);
                productDetails.put("image", product.getImage());

                cartDetails.put(productId, productDetails);
            }
        }
        return cartDetails;
    }

    // Xử lý thanh toán và lưu đơn hàng vào DB
    public String checkout(String sessionId, String userId, String address, String phone, String note) {
        String cartKey = CART_PREFIX + sessionId;
        Map<String, Integer> cartItems = hashOperations.entries(cartKey);

        if (cartItems.isEmpty()) {
            return "Giỏ hàng trống. Không thể thanh toán.";
        }else {
            System.out.println("Giỏ hàng có "+ cartItems);
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("Đang xử lý");
        order.setAddress(address);
        order.setPhone(phone);
        order.setNote(note);

        // Tính tổng giá trị đơn hàng
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            Long productId = Long.parseLong(entry.getKey());
            Integer quantity = entry.getValue();
            Product product = productRepo.findById(productId).orElse(null);

            if (product != null) {
                BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(quantity));
                totalAmount = totalAmount.add(itemTotal);

                // Lưu thông tin các mục trong giỏ hàng vào bảng order_items
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);
                orderItem.setPrice(product.getPrice());
                orderItem.setOrder(order);
                orderItemRepo.save(orderItem);
            }
        }

        order.setTotalAmount(totalAmount);
        orderRepo.save(order);

        redisTemplate.delete(cartKey);

        return "Thanh toán thành công. Đơn hàng của bạn đã được ghi nhận.";
    }

    // Xóa giỏ hàng sau khi thanh toán xong
    public void clearCart(String sessionId) {
        String cartKey = CART_PREFIX + sessionId;
        redisTemplate.delete(cartKey);
    }
}
