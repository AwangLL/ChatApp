package com.awang.dao;

import com.awang.domain.Group;
import com.awang.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupMapper {
    List<Group> getUserGroup(Integer userId);
    List<Integer> getGroupUserId(Integer groupId);
    List<User> getGroupUser(Integer groupId);
    void enterGroup(@Param("groupId")Integer groupId, @Param("userId")Integer userId);
    void exitGroup(@Param("groupId")Integer groupId, @Param("userId")Integer userId);
    Group searchGroup(Integer groupId);
}
