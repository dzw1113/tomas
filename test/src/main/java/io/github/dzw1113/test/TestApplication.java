package io.github.dzw1113.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:启动类
 * @author: dzw
 * @date: 2021/09/15 09:58
 **/
@SpringBootApplication
public class TestApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
        System.out.println(123);
        //-javaagent:E:\\git\\asd1\\111\\tomas\\agent\\target\\tomas-agent-jar-with-dependencies.jar
    }
    
}
