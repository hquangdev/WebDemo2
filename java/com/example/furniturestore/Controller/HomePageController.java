package com.example.furniturestore.Controller;


import com.example.furniturestore.Entity.Category;
import com.example.furniturestore.Entity.Product;
import com.example.furniturestore.Repotitory.CategoryRepo;
import com.example.furniturestore.Repotitory.ProductRepo;
import com.example.furniturestore.Service.ProductService;
import com.example.furniturestore.dto.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/home")
@RestController
public class HomePageController {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductService productService;

    @GetMapping("/content")
    public ResponseEntity<Object> listContent(){
        List<Product> products = productRepo.findAll();
        List<Category> categories = categoryRepo.findAll();

        // Tạo một Map để chứa phản hồi
        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("categories", categories);

        return ResponseEntity.ok(response);
    }

    //tim kiem va loc san pham

//    @GetMapping("/search")
//    public ReqRes searchProducts(
//            @RequestParam String name,
//            @RequestParam double minPrice,
//            @RequestParam double maxPrice) {
//        return productService.searchProducts(name, minPrice, maxPrice);
//    }
}
