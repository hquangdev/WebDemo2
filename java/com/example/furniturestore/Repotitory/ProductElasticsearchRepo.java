package com.example.furniturestore.Repotitory;

import com.example.furniturestore.Entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductElasticsearchRepo extends ElasticsearchRepository<Product, Long> {


}
