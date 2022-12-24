package com.awang.service.impl;

import com.awang.dao.UserMapper;
import com.awang.domain.User;
import com.awang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer getMaxId() {
        return userMapper.getMaxId();
    }

    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    @Override
    public User findUserById(Integer id) {
        return userMapper.findUserById(id);
    }

    @Override
    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    @Override
    public void updateOnline(User user) {
        userMapper.updateOnline(user);
    }

    @Override
    public List<User> searchUser(String account) {
        boolean isNumeric = true;
        for (int i = account.length() - 1; i >= 0; i--) {
            if(!Character.isDigit(account.charAt(i))) {
                isNumeric = false;
                break;
            }
        }
        List<User> users = new ArrayList<>();
        if(isNumeric) {
            users.add(userMapper.findUserById(Integer.parseInt(account)));
        }
        users.addAll(userMapper.findUserByUsername(account));

        return users;
    }

    @Override
    public void updateUser(User user) {
        this.userMapper.updateUser(user);
    }
}
