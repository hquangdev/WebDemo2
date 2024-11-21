package com.example.furniturestore.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.furniturestore.Entity.Category;
import com.example.furniturestore.Entity.Product;
import com.example.furniturestore.Repotitory.CategoryRepo;
import com.example.furniturestore.Repotitory.ProductRepo;
import com.example.furniturestore.dto.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;



@Service
public class ProductService {
    private final String uploadDir = "F:/File_web/FurnitureStore/src/main/resources/static/images/product/" ;
    @Autowired
    private ProductRepo productRepo;
//
//    @Autowired
//    private ProductElasticsearchRepo productESRepository;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ElasticsearchClient elasticsearchClient;


    public void createProduct(ProductRequest dto) throws IOException {
        // Kiểm tra xem category có tồn tại không
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        Integer rating = 0;

        // Lưu ảnh và lấy tên ảnh
        String imageName = saveFile(dto.getImage());

        // Tạo đối tượng Product và gán giá trị
        Product product = new Product();
        product.setName(dto.getName());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setDetails(dto.getDetails());
        product.setImage(imageName);
        product.setCategory(category);
        product.setRating(rating);

        // Lưu sản phẩm vào cơ sở dữ liệu
        productRepo.save(product);
    }


    //Xử lí thêm ảnh
    public String saveFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String filePath = Paths.get(uploadDir, originalFileName).toString();
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        // Trả về tên file gốc
        return originalFileName;
    }

    public void editProduct(Long id, ProductRequest productRequest) throws IOException {
        Category category = categoryRepo.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không có danh mục"));

        // Kiểm tra xem tên sản phẩm đã tồn tại chưa
        if (productRepo.findByName(productRequest.getName()).isPresent()) {
            throw new RuntimeException("Tên sản phẩm đã tồn tại");
        }

        // Tìm sản phẩm theo ID
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Xóa ảnh cũ nếu có
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            delelteFile(product.getImage());
        }

        // Lưu ảnh mới vào thư mục chỉ nếu có ảnh mới
        if (productRequest.getImage() != null && !productRequest.getImage().isEmpty()) {
            String newImageName = saveFile(productRequest.getImage());
            product.setImage(newImageName);
        }

        int rating = 0;

        // Cập nhật thông tin sản phẩm
        product.setName(productRequest.getName());
        product.setTitle(productRequest.getTitle());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setDetails(productRequest.getDetails());
        product.setCategory(category);
        product.setRating(rating);

        productRepo.save(product);
    }


    //xoa sanr pham
    public void delelteProduct(Long id){
        Product productId = productRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Không tim thấy sản phẩm"));

        delelteFile(productId.getImage());
        productRepo.delete(productId);

    }

    //xoa anh trong thu muc
    public String delelteFile(String fileName) {
        File deleteFile = new File(uploadDir + fileName);

        if (deleteFile.exists() && deleteFile.delete()) {
            return "Tệp đã được xóa thành công: " + fileName;
        }
        return deleteFile.exists() ? "Không thể xóa tệp: " + fileName : "Không tìm thấy tệp: " + fileName;
    }

//
//    public List<Product> getProductSuggestions(String keyword) throws IOException {
//
//        MatchQuery matchQuery = new MatchQuery.Builder()
//                .query(keyword)
//                .build();
//
//        SearchRequest searchRequest = new SearchRequest.Builder()
//                .index("products")
//                .query(q -> q
//                        .match(m -> m
//                                .field("name")
//                                .query(keyword)
//                        )
//                )
//                .build();
//
//        SearchResponse<Product> searchResponse = elasticsearchClient.search(searchRequest, Product.class);
//
//        // Chuyển đổi kết quả thành danh sách sản phẩm
//        return searchResponse.hits().hits().stream()
//                .map(hit -> hit.source())
//                .collect(Collectors.toList());
//    }


}
