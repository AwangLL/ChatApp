package com.awang.service;

import com.awang.domain.Message;

import java.util.List;

public interface MessageService {
    void insertMessage(Message message);
    void readMessage(Integer id, Integer myId);
    List<Message> getFriendHistoryMessage(Integer myId, Integer friendId, Integer id, Integer number);
    List<Message> getGroupHistoryMessage(Integer groupId, Integer id, Integer number);
    List<Message> getNewMessage(Integer id);
}
