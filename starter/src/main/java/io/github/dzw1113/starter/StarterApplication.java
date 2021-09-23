package io.github.dzw1113.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import io.github.dzw1113.starter.netty.NettyServer;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/16 10:23
 **/
@SpringBootApplication
@EnableKnife4j
public class StarterApplication {
    
    private static final Logger log = LoggerFactory.getLogger(StarterApplication.class);
    
    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
        try {
            new Thread(() -> {
                try {
                    new NettyServer(12345).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            log.error("netty创建失败！", e);
        }
    }
    
    
    /**
     * @param
     * @return springfox.documentation.spring.web.plugins.Docket
     * @description 加载内网相关的swagger接口文档
     * @author dzw
     * @date 2021/9/8 16:24
     **/
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30).useDefaultResponseMessages(false).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build().apiInfo(apiInfo()).enable(true);
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("jvm监控管理").description("监控").contact(new Contact("dzw。", "https://github.com/dzw1113", "649095437@qq.com")).version("1.0.1").build();
    }
    
}
