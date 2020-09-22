package com.example.demo.listener;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddIndexListener implements ActionListener<IndexResponse> {

    private final static Logger LOG = LoggerFactory.getLogger(AddIndexListener.class);

    private static AddIndexListener instance;

    public static synchronized AddIndexListener getInstance() {
        if (instance == null) {
            instance = new AddIndexListener();
        }
        return instance;
    }

    @Override
    public void onFailure(Exception e) {
    }

    @Override
    public void onResponse(IndexResponse res) {
    }


}
