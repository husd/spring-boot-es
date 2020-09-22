package com.example.demo.listener;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteIndexListener implements ActionListener<DeleteResponse> {

    private final static Logger LOG = LoggerFactory.getLogger(DeleteIndexListener.class);

    private static DeleteIndexListener instance;

    public static synchronized DeleteIndexListener getInstance() {
        if (instance == null) {
            instance = new DeleteIndexListener();
        }
        return instance;
    }

    @Override
    public void onFailure(Exception e) {
    }

    @Override
    public void onResponse(DeleteResponse res) {

    }


}
