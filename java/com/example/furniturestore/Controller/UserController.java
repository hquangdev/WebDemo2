package com.example.furniturestore.Controller;

import com.example.furniturestore.Entity.OurUsers;
import com.example.furniturestore.Repotitory.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private OurUserRepo ourUserRepo;

    @GetMapping("/admin/users/list")
    public ResponseEntity<List<OurUsers>> listUser(){
        return ResponseEntity.ok(ourUserRepo.findAll());

    }

//
//    //Sửa người dùng
//    @PutMapping("/admin/users/update/{id}")
//    public ResponseEntity<String> updateUSer(@PathVariable Long id,@RequestBody UserRequest userRequest){
//
//
//
//        return null;
//    }

    @GetMapping("/admin/users/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        Optional<OurUsers> userID = ourUserRepo.findById(id);
        if (userID.isPresent()){
            throw new RuntimeException("không thấy ng dùng");
        }

        ourUserRepo.delete(userID.get());
        return ResponseEntity.ok("Đã xóa thành công");


    }
    //Xóa người dùng

}
