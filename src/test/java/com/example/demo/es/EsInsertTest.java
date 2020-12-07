package com.example.demo.es;

import com.example.demo.BaseTest;
import com.example.demo.listener.AddIndexListener;
import com.example.demo.listener.BulkIndexListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 这里是这个类的功能描述
 *
 * @author hushengdong
 */
public class EsInsertTest extends BaseTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private AddIndexListener addIndexListener;

    @Autowired
    private BulkIndexListener bulkIndexListener;

    @Test
    public void testIndex() throws IOException {

        String index = "sku_index";
        String type = "sku";
        String index_id = "12345";
        //这个是ES中数据对应的map
        Map<String, String> valueMap = new HashMap<>();
        IndexRequest indexRequest = new IndexRequest(index, type, index_id).source(valueMap);
        //这种是异步的新增
        //restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, addIndexListener);
        //这个是同步的新增
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Test
    public void testBulkIndex() throws IOException {

        //批量新增
        //TODO 注意处理异常信息
        String index = "sku_index";
        String type = "sku";
        String index_id = "12345";
        //这个是ES中数据对应的map
        Map<String, String> valueMap = new HashMap<>();
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < 10; i++) {
            IndexRequest indexRequest = new IndexRequest(index, type, index_id).source(valueMap);
            bulkRequest.add(indexRequest);
        }
        restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkIndexListener);
    }

}
