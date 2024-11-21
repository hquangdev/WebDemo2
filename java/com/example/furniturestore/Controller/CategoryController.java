package com.example.furniturestore.Controller;

import com.example.furniturestore.Entity.Category;
import com.example.furniturestore.Service.CategoryService;
import com.example.furniturestore.dto.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> listCategory() {
        List<Category> list = categoryService.getAllCategory();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@Validated @RequestBody CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return ResponseEntity.status(200).body("Bạn đã thêm thành công danh mục");
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@Validated @PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.status(200).body("Cập nhật danh mục thành công");
    }

    //xoa danh muc
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
            categoryService.deleteCategory(id);
            return ResponseEntity.status(201).body("Xóa danh mục thành công");
    }
}
