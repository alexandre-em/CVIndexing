package com.indexation.cv.config;

import com.indexation.cv.utils.Constant;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.indexation.cv.repository")
public class ElasticSearchConfiguration {
    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(Constant.ELASTIC_ENDPOINT)
                .withBasicAuth(Constant.ELASTIC_USERNAME, Constant.ELASTIC_PASSWORD)
                .withConnectTimeout(10000)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() { return new ElasticsearchRestTemplate(client()); }
}
