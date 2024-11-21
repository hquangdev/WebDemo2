package com.example.furniturestore.Controller;

import com.example.furniturestore.Repotitory.ProductRepo;
import com.example.furniturestore.Service.ProductService;
import com.example.furniturestore.dto.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepo productRepo;


    @GetMapping("/admin/product/list")
    public ResponseEntity<Object> getAllProducts(){
        return ResponseEntity.ok(productRepo.findAll());
    }

    //theem san [ham
    @PostMapping("/admin/product/add")
    public ResponseEntity<String> createProduct(@Validated @ModelAttribute ProductRequest dto) throws IOException {
        try {
            productService.createProduct(dto);
            return ResponseEntity.ok("Sản phẩm đã được thêm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã có lỗi xảy ra: " + e.getMessage());
        }
    }


    //Sửa sản phẩm
    @PutMapping("/admin/product/edit/{id}")
    public ResponseEntity<String> editProduct(@Validated @PathVariable Long id,
                                              @ModelAttribute ProductRequest productRequest) throws IOException {

            productService.editProduct(id, productRequest);
            return ResponseEntity.ok("Đã cập nhật thành công sản phẩm");
    }

    //Xóa san phẩm
    @GetMapping("/admin/product/delete/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id){
        productService.delelteProduct(id);
        return ResponseEntity.ok("Xóa sản phẩm thành công");
    }

//    // API tìm kiếm sản phẩm
//    @GetMapping("/admin/products/search")
//    public List<Product> searchProducts(@RequestParam String keyword) throws IOException {
//        return productService.getProductSuggestions(keyword);
//    }



//    @GetMapping("/user/alone")
//    public ResponseEntity<Object> userAlone(){
//        return ResponseEntity.ok("Chỉ người dùng mới có thể truy cập ApI này");
//    }
//
//    @GetMapping("/adminuser/both")
//    public ResponseEntity<Object> bothAdminaAndUsersApi(){
//        return ResponseEntity.ok("Cả Admin và Người dùng đều có thể truy cập api");
//    }
//
//    /** You can use this to get the details(name,email,role,ip, e.t.c) of user accessing the service*/
//    @GetMapping("/public/email")
//    public String getCurrentUserEmail() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication); //get all details(name,email,password,roles e.t.c) of the user
//        System.out.println(authentication.getDetails()); // get remote ip
//        System.out.println(authentication.getName()); //returns the email because the email is the unique identifier
//        return authentication.getName(); // returns the email
//    }

}
