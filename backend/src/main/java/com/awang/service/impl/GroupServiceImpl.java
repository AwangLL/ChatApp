package com.awang.service.impl;

import com.awang.dao.GroupMapper;
import com.awang.domain.Group;
import com.awang.domain.User;
import com.awang.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupMapper groupMapper;
    @Override
    public List<Group> getUserGroup(Integer userId) {
        return groupMapper.getUserGroup(userId);
    }

    @Override
    public List<Integer> getGroupUserId(Integer groupId) {
        return groupMapper.getGroupUserId(groupId);
    }

    @Override
    public List<User> getGroupUser(Integer groupId) {
        return groupMapper.getGroupUser(groupId);
    }

    @Override
    public void enterGroup(Integer groupId, Integer userId) {
        groupMapper.enterGroup(groupId, userId);
    }

    @Override
    public void exitGroup(Integer groupId, Integer userId) {
        groupMapper.exitGroup(groupId, userId);
    }

    @Override
    public Group searchGroup(Integer groupId) {
        return groupMapper.searchGroup(groupId);
    }
}
