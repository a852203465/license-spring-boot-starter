package cn.darkjrong.licenseverify.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 证书认证接口测试
 *
 * @author Rong.Jia
 * @date 2022/03/11
 */
@CrossOrigin
@RestController
@RequestMapping("api")
public class VerifyTestController {

    @GetMapping("/hello")
    public String sayHello(){
        return "hello license !";
    }

}
