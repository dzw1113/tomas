package io.github.dzw1113.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/15 13:23
 **/
@RestController
public class IndexController {
    
    @RequestMapping(value = {"/","/index"})
    public String index() {
        System.out.println("index");
        return "返回index";
    }
    
}
