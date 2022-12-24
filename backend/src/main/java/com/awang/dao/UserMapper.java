package com.awang.dao;

import com.awang.domain.User;

import java.util.List;

public interface UserMapper {
    public Integer getMaxId();
    public User findUserById(Integer id);
    public User findUserByEmail(String email);
    public List<User> findUserByUsername(String username);
    public List<User> searchUser(String account);
    public void insertUser(User user);
    public void updateOnline(User user);
    public void updateUser(User user);
}
