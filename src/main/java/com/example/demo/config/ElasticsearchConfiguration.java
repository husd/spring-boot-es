package com.example.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这里是这个类的功能描述
 *
 * @author hushengdong
 */
@Configuration
public class ElasticsearchConfiguration {

    @Value("${elasticsearch.ip}")
    private String ip;

    @Bean(destroyMethod = "close", name = "EsClient")
    public RestHighLevelClient restClient() {
        String[] ipArray = ip == null ? null : ip.split(";");
        RestClientBuilder c;
        if (ipArray == null) {
            c = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        } else {
            HttpHost[] httpHosts = new HttpHost[ipArray.length];
            for (int i = 0; i < httpHosts.length; i++) {
                String[] r = ipArray[i].split(":");
                String host = r[0];
                String port = r[1];
                HttpHost httpHost = new HttpHost(host, Integer.parseInt(port), "http");
                httpHosts[i] = httpHost;
            }
            c = RestClient.builder(httpHosts);
        }
        System.out.println("ServerApplication Started... Server nodes:" + ipArray.length);
        return new RestHighLevelClient(c);
    }

}
