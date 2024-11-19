package com.example.furniturestore.Entity;


import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products")
public class ProductDocument {

        @Id
        private String id; // Elasticsearch sử dụng String cho ID

        @Field(type = FieldType.Text)
        private String name;

        @Field(type = FieldType.Text)
        private String details;

        @Field(type = FieldType.Integer)
        private Integer rating;

        @Field(type = FieldType.Double)
        private Double price;

        private String title;
        private int quantity;
        private String image;

        @Field(type = FieldType.Keyword)
        private String categoryId;

    }

