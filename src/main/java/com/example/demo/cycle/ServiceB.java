package com.example.demo.cycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 这里是这个类的功能描述
 *
 * @author hushengdong
 */
@Component
public class ServiceB {

    @Autowired
    private ServiceA serviceA;

    public ServiceB() {
        System.out.println("init B");
    }
}
