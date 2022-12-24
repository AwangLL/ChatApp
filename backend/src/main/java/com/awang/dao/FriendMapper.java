package com.awang.dao;

import com.awang.domain.Friend;
import com.awang.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendMapper {
    public List<User> getFriend(Integer myId);
    public List<User> getFriendRequest(Integer myId);
    public void addFriendRequest(@Param("myId")Integer myId, @Param("friendId")Integer friendId);
    public void deleteFriend(@Param("myId")Integer myId, @Param("friendId")Integer friendId);
    public void acceptFriendRequest(@Param("myId")Integer myId, @Param("friendId")Integer friendId);
    public void refuseFriendRequest(@Param("myId")Integer myId, @Param("friendId")Integer friendId);
}
