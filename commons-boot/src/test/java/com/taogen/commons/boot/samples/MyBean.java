package com.taogen.commons.boot.samples;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author taogen
 */
@Component
@Data
public class MyBean {

    private String name;

    public MyBean() {
        this.name = "Taogen";
    }

    public void doSomething() {
        System.out.println("Do something!");
    }
}
