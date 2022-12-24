package com.awang.service;

import com.awang.domain.Friend;
import com.awang.domain.User;

import java.util.List;

public interface FriendService {
    public List<User> getFriend(Integer myId);
    public List<User> getFriendRequest(Integer myId);
    public void addFriendRequest(Integer myId, Integer friendId);
    public void deleteFriend(Integer myId, Integer friendId);
    public void acceptFriendRequest(Integer myId, Integer friendId);
    public void refuseFriendRequest(Integer myId, Integer friendId);
}
