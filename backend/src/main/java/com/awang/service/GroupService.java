package com.awang.service;

import com.awang.domain.Group;
import com.awang.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupService {
    List<Group> getUserGroup(Integer userId);
    List<Integer> getGroupUserId(Integer groupId);
    List<User> getGroupUser(Integer groupId);
    void enterGroup(Integer groupId, Integer userId);
    void exitGroup(Integer groupId, Integer userId);
    Group searchGroup(Integer groupId);
}
