package com.example.demo.listener;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.update.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateIndexListener implements ActionListener<UpdateResponse> {

    private final static Logger LOG = LoggerFactory.getLogger(UpdateIndexListener.class);

    private static UpdateIndexListener instance;

    public static synchronized UpdateIndexListener getInstance() {
        if (instance == null) {
            instance = new UpdateIndexListener();
        }
        return instance;
    }

    @Override
    public void onFailure(Exception e) {
    }

    @Override
    public void onResponse(UpdateResponse res) {
    }


}
