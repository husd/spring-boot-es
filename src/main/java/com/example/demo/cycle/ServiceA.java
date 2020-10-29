package com.example.demo.cycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 这里是这个类的功能描述
 *
 * @author hushengdong
 */
@Component
public class ServiceA {

    @Autowired
    private ServiceB serviceB;

    public ServiceA() {
        System.out.println("init A");
    }
}
