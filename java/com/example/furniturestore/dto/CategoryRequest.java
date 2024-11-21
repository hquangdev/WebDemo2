package com.example.furniturestore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {

    private Long id;

    @NotNull(message = "Không được để trống tên")
    private String name;

    @NotNull(message = "Không được để trống content")
    private String content;
}
