package com.example.furniturestore.Service;


import com.example.furniturestore.Entity.Category;
import com.example.furniturestore.Repotitory.CategoryRepo;
import com.example.furniturestore.dto.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    //lấy danh scahs danh mục
    public List<Category> getAllCategory(){
      return categoryRepo.findAll();
    }

    //thêm danh mục
    public void addCategory(CategoryRequest categoryRequest)  {

        Optional<Category> existingCategory = categoryRepo.findByName(categoryRequest.getName().trim());
        if (existingCategory.isPresent()) {
            throw new RuntimeException("Tên danh mục đã tồn tại");
        }

        Category category = new Category();

        category.setName(categoryRequest.getName().trim());
        category.setContent(categoryRequest.getContent());

        categoryRepo.save(category);

    }

    //Sửa danh mục
    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        // Tìm danh mục hiện tại
        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));

        Optional<Category> CategoryName = categoryRepo.findByName(categoryRequest.getName());

        if (CategoryName.isPresent() ) {
            throw new RuntimeException("Tên danh mục đã tồn tại");
        }

        // Cập nhật thông tin và lưu danh mục
        existingCategory.setName(categoryRequest.getName());
        existingCategory.setContent(categoryRequest.getContent());
        categoryRepo.save(existingCategory);
    }

    //xóa danh muc
    public void deleteCategory(Long id){
        // Tìm danh mục hiện tại
        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));
        categoryRepo.delete(existingCategory);
    }

}
