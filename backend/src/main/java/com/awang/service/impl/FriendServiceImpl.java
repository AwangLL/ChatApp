package com.awang.service.impl;

import com.awang.dao.FriendMapper;
import com.awang.domain.Friend;
import com.awang.domain.User;
import com.awang.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendMapper friendMapper;
    @Override
    public List<User> getFriend(Integer myId) {
        return friendMapper.getFriend(myId);
    }

    @Override
    public List<User> getFriendRequest(Integer myId) {
        return friendMapper.getFriendRequest(myId);
    }

    @Override
    public void addFriendRequest(Integer myId, Integer friendId) {
        friendMapper.addFriendRequest(myId, friendId);
    }

    @Override
    public void deleteFriend(Integer myId, Integer friendId) {
        friendMapper.deleteFriend(myId, friendId);
    }

    @Override
    public void acceptFriendRequest(Integer myId, Integer friendId) {
        friendMapper.acceptFriendRequest(myId, friendId);
    }

    @Override
    public void refuseFriendRequest(Integer myId, Integer friendId) {
        friendMapper.refuseFriendRequest(myId, friendId);
    }
}
