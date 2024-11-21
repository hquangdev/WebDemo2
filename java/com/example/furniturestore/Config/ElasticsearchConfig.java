package com.example.furniturestore.Config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.Transport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.furniturestore.Repotitory")
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // Cấu hình RestClient để kết nối với Elasticsearch
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();

        // Tạo Transport sử dụng RestClient và tạo ElasticsearchClient
        Transport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient((ElasticsearchTransport) transport);
    }
}
