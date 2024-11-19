package com.example.furniturestore.Controller;

import com.example.furniturestore.Entity.Category;
import com.example.furniturestore.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> insertCategory(@RequestBody Category category) {
        String name = category.getName();
        categoryService.insertCategory(name, category);

        return ResponseEntity.status(201).body("Bạn đã thêm thành công danh mục: " + name);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {
        categoryService.updateCategory(id, updatedCategory);
        return ResponseEntity.status(201).body("Cập nhật danh mục thành công");
    }

    //xoa danh muc
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
            categoryService.deleteCategory(id);
            return ResponseEntity.status(201).body("Xóa danh mục thành công");
    }
}
