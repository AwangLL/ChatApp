package com.awang.dao;

import com.awang.domain.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    Message getMessage(Integer id);
    void insertMessage(Message message);
    void insertGroupMessage(Message message);
    List<Message> getFriendHistoryMessage(@Param("myId")Integer myId, @Param("friendId")Integer friendId);
    List<Message> getGroupHistoryMessage(Integer groupId);
    List<Message> getNewFriendMessage(Integer myId);
    List<Message> getNewGroupMessage(Integer myId);
    void readMessage(Integer id);
    void readGroupMessage(@Param("id")Integer id, @Param("myId")Integer myId);
}
