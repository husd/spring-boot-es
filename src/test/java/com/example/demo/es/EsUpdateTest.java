package com.example.demo.es;

import com.example.demo.DemoApplicationTests;
import com.example.demo.listener.UpdateIndexListener;
import org.elasticsearch.action.update.UpdateRequest;
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
public class EsUpdateTest extends DemoApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private UpdateIndexListener updateIndexListener;

    @Test
    public void testQuery() throws IOException {

        String index = "sku_index";
        String type = "sku";
        String index_id = "12345";
        //这个是ES中数据对应的map
        Map<String, String> valueMap = new HashMap<>();
        UpdateRequest request = new UpdateRequest(index, type, index_id)
                .doc(valueMap);
        //restHighLevelClient.updateAsync(request, RequestOptions.DEFAULT, updateIndexListener);
        restHighLevelClient.update(request, RequestOptions.DEFAULT);
    }

}
