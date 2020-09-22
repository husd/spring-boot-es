package com.example.demo;

import com.example.demo.listener.DeleteIndexListener;
import org.elasticsearch.action.delete.DeleteRequest;
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
public class EsDeleteTest extends DemoApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private DeleteIndexListener deleteIndexListener;

    @Test
    public void testQuery() throws IOException {

        String index = "sku_index";
        String type = "sku";
        String index_id = "12345";
        //这个是ES中数据对应的map
        Map<String, String> valueMap = new HashMap<>();
        DeleteRequest delRequest = new DeleteRequest(index, type, index_id);
        //这种是异步的删除
        // restHighLevelClient.deleteAsync(delRequest, RequestOptions.DEFAULT, deleteIndexListener);
        //这个是同步的删除
        restHighLevelClient.delete(delRequest, RequestOptions.DEFAULT);
    }

}
