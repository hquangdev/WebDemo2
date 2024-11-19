package com.example.furniturestore.Service;


import com.example.furniturestore.Entity.Category;
import com.example.furniturestore.Repotitory.CategoryRepo;
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
    public void insertCategory(String name, Category category)  {
        Optional<Category> categoryName = categoryRepo.findByName(name);

        if(categoryName.isPresent()){
            throw new RuntimeException("Tên danh mục đã tồn tại");
        }
        categoryRepo.save(category);

    }

    //Sửa danh mục
    public void updateCategory(Long id, Category category) {
        // Tìm danh mục hiện tại
        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));

        Optional<Category> CategoryName = categoryRepo.findByName(category.getName());

        if (CategoryName.isPresent() ) {
            throw new RuntimeException("Tên danh mục đã tồn tại");
        }

        // Cập nhật thông tin và lưu danh mục
        existingCategory.setName(category.getName());
        existingCategory.setContent(category.getContent());
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
