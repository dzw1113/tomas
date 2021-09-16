package io.github.dzw1113.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 动态加载这个jar的SystemController
 * @author: dzw
 * @date: 2021/09/15 14:04
 **/
@RestController
public class SystemController {
    
    @RequestMapping("/system")
    public String index() {
        System.out.println("system");
        return "返回system";
    }
}
