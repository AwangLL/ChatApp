package com.awang.service;

import com.awang.domain.User;

import java.util.List;

public interface UserService {
    public Integer getMaxId();
    public User findUserById(Integer id);
    public User findUserByEmail(String email);
    public List<User> searchUser(String name);
    public void insertUser(User user);
    public void updateOnline(User user);
    public void updateUser(User user);
}
