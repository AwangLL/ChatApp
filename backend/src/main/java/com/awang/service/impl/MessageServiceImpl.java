package com.awang.service.impl;

import com.awang.dao.GroupMapper;
import com.awang.dao.MessageMapper;
import com.awang.domain.Group;
import com.awang.domain.Message;
import com.awang.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Override
    public void insertMessage(Message message) {
        messageMapper.insertMessage(message);
        if(message.getType()==1) { // 群消息
            messageMapper.insertGroupMessage(message);
        }
    }

    @Override
    public void readMessage(Integer id, Integer myId) {
        Message message = messageMapper.getMessage(id);
        if(message.getType()==1) { //群消息
            messageMapper.readGroupMessage(id, myId);
        } else {
            messageMapper.readMessage(id);
        }
    }

    @Override
    public List<Message> getFriendHistoryMessage(Integer myId, Integer friendId, Integer id, Integer number) {
        List<Message> messages = messageMapper.getFriendHistoryMessage(myId, friendId);
        List<Message> ret = new ArrayList<>(number);
        int cnt = 0;
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);
            if (message.getId() < id && cnt < number) {
                ret.add(message);
            }
        }
        return ret;
    }

    @Override
    public List<Message> getGroupHistoryMessage(Integer groupId, Integer id, Integer number) {
        List<Message> messages = messageMapper.getGroupHistoryMessage(groupId);
        List<Message> ret = new ArrayList<>(number);
        int cnt = 0;
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);
            if (message.getId() < id && cnt < number) {
                ret.add(message);
            }
        }
        return ret;
    }

    @Override
    public List<Message> getNewMessage(Integer userId) {
        // 群消息
        List<Message> res = messageMapper.getNewGroupMessage(userId);
        // 好友消息
        res.addAll(messageMapper.getNewFriendMessage(userId));
        return res;
    }
}
