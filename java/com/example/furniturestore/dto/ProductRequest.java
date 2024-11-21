package com.example.furniturestore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotNull(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, max = 100, message = "Tên sản phẩm phải từ 3 đến 100 ký tự")
    private String name;

    private String details;

    private Integer rating;

    @NotNull(message = "Giá sản phẩm không được để trống")
    private BigDecimal price;

    @NotNull(message = "Tiêu đề không được để trống")
    @Size(min = 3, max = 200, message = "Tiêu đề phải từ 3 đến 200 ký tự")
    private String title;

    private int quantity;

    private MultipartFile image;

    @NotNull(message = "Danh mục sản phẩm không được để trống")
    private Long categoryId;


}
