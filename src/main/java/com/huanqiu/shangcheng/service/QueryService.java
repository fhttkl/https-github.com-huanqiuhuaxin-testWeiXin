package com.huanqiu.shangcheng.service;


import com.huanqiu.shangcheng.pojo.User;

import java.util.List;

public interface QueryService {


    public void insertUser(User user);

    public List<User> selectUser();

    public Integer sumNum(int a ,int b);

}
