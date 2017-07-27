package com.huanqiu.shangcheng.service.impl;

import com.huanqiu.shangcheng.mapper.UserMapper;
import com.huanqiu.shangcheng.pojo.User;
import com.huanqiu.shangcheng.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    @Qualifier("UserCRUD")
    private UserMapper userMapper;

    @Transactional
    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    @Override
    public List<User> selectUser() {
       List<User> list= userMapper.selectUser();
        return list;
    }

    @Override
    public Integer sumNum(int a, int b) {

        return a+b;
    }


}
