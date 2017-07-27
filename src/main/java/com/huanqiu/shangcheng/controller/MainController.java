package com.huanqiu.shangcheng.controller;

import com.huanqiu.shangcheng.pojo.User;
import com.huanqiu.shangcheng.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private QueryService queryService;

    @RequestMapping(value = "/login.do")
    public String index( ){
        User user =new User();
        user.setId("1");
        user.setUsername("张三");
        user.setPassword("admin");
       // queryService.insertUser(user);

        List<User> list=queryService.selectUser();
        for(User aa : list){
            System.out.println(aa);
        }

       int a= queryService.sumNum(1,2);
        System.out.println(a);
        System.out.println("hello ");
        return "index";
    }
}
