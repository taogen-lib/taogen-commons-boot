package com.taogen.commons.boot.samples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author taogen
 */
@Configuration
public class MyConfig {

    @Bean({"myBeanAlias1", "myBeanAlias2"})
    public MyBean myBeanWithAlias() {
        return new MyBean();
    }
}
