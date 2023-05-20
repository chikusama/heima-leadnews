package com.itheima.config;

import com.itheima.hello.HelloProperties;
import com.itheima.hello.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义Hello自动化配置类
 */
@EnableConfigurationProperties(HelloProperties.class)
@Configuration
@ConditionalOnClass(HelloService.class)
public class HelloAutoConfiguration {

    @Autowired
    HelloProperties helloProperties;

    @Bean
    @ConditionalOnMissingBean(HelloService.class)
    public HelloService helloService(){
        HelloService helloService = new HelloService();
        helloService.setMyName(helloProperties.getMyName());
        return helloService;
    }

}
