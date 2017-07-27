package com.huanqiu.shangcheng.mapper;

import com.huanqiu.shangcheng.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserCRUD")
public interface UserMapper {
    public void insertUser(User user);
    public List<User> selectUser();
}
