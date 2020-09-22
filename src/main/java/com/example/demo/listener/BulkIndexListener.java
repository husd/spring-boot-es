package com.example.demo.listener;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkIndexListener implements ActionListener<BulkResponse> {

    private final static Logger LOG = LoggerFactory.getLogger(BulkIndexListener.class);

    private static BulkIndexListener instance;

    public static synchronized BulkIndexListener getInstance() {
        if (instance == null) {
            instance = new BulkIndexListener();
        }
        return instance;
    }

    @Override
    public void onFailure(Exception e) {
       //TODO
    }

    @Override
    public void onResponse(BulkResponse res) {
        //TODO
    }


}
