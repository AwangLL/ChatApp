package com.awang.controller;

import com.alibaba.fastjson.JSONObject;
import com.awang.domain.Group;
import com.awang.domain.Message;
import com.awang.domain.User;
import com.awang.service.FriendService;
import com.awang.service.GroupService;
import com.awang.service.MessageService;
import com.awang.service.UserService;
import com.awang.util.Event;
import com.awang.util.EventType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/api")
public class APIController {
    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ApplicationContext applicationContext;

    private final Path p = Paths.get("D:\\.Project\\ChatRoom\\backend\\src\\main\\webapp\\img");

    /**
     * /api/user/verify
     * 用户验证
     *
     * @param
     * {
     *      account: ID或邮箱
     *      password: 密码
     * }
     * @return 校验结果
     */
    @CrossOrigin
    @GetMapping("/user/verify")
    @ResponseBody
    public Integer UserVerify(
            @RequestParam("account") String account,
            @RequestParam("password") String password) {
        User user = null;
        if (account.indexOf('@') != -1) {
            user = userService.findUserByEmail(account);
        } else {
            user = userService.findUserById(Integer.parseInt(account));
        }
        int ret = 1;
        if (user == null) {
            ret = -1;
        } else if (!user.getPassword().equals(password)) {
            ret = 0;
        }
        return ret;
    }

    /**
     * /api/user/insert
     * 新增用户
     *
     * @param
     * {
     *     username: 用户名
     *     password: 密码
     *     sex: 性别
     *     birth: 出生日期
     *     email: 邮箱地址
     *     info: 个性签名
     * }
     * @return 系统分配的用户id
     */
    @CrossOrigin
    @PostMapping("/user/insert")
    @ResponseBody
    public Integer InsertUser(@RequestBody JSONObject obj) throws ParseException {
        User user = new User();
        user.setId(userService.getMaxId() + 1);
        user.setUsername(obj.getString("username"));
        user.setPassword(obj.getString("password"));
        user.setSex(obj.getString("sex"));
        String date = obj.getString("birth");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            user.setBirth(sdf.parse(date.substring(0, 10)));
        } catch (StringIndexOutOfBoundsException e) {
            user.setBirth(null);
        }
        user.setEmail(obj.getString("email"));
        user.setInfo(obj.getString("info"));
        user.setOnline(false);

        userService.insertUser(user);

        return user.getId();
    }

    @CrossOrigin
    @PostMapping("/user/update")
    @ResponseBody
    public Integer updateUser(@RequestBody JSONObject obj) throws ParseException {
        User user = new User();
        user.setId(obj.getInteger("id"));
        user.setUsername(obj.getString("username"));
        user.setPassword(obj.getString("password"));
        user.setSex(obj.getString("sex"));
        String date = obj.getString("birth");
        if(date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            user.setBirth(sdf.parse(date.substring(0, 10)));
        } else user.setBirth(null);
        user.setEmail(obj.getString("email"));
        user.setInfo(obj.getString("info"));
        user.setOnline(false);

        userService.updateUser(user);

        return user.getId();
    }

    /**
     * /api/user/search
     *
     * @param name: 用户ID或用户名
     * @return list: 符合条件的用户表
     */
    @CrossOrigin
    @GetMapping("/user/search")
    @ResponseBody
    public List<User> searchUser(@RequestParam("name") String name) {
        return userService.searchUser(name);
    }

    /**
     * /api/friend/add
     * 添加好友
     *
     * @param
     * {
     *     me: 发送好友请求的用户ID
     *     friend: 要添加的好友的ID
     * }
     * @return "success"
     */
    @CrossOrigin
    @GetMapping("/friend/add")
    @ResponseBody
    public String addFriend(
            @RequestParam("me") Integer myId,
            @RequestParam("friend") Integer friendId) {
        friendService.addFriendRequest(myId, friendId);
        return "success";
    }

    /**
     * /api/friend/accept
     * 接受好友请求
     * @param
     * {
     *     me: 接受好友请求的用户ID
     *     friend: 要同意的好友ID
     * }
     * @return "success"
     */
    @CrossOrigin
    @GetMapping("/friend/accept")
    @ResponseBody
    public String acceptFriend(
            @RequestParam("me") Integer myId,
            @RequestParam("friend") Integer friendId) {
        System.out.println("accept" + myId + "," + friendId);
        friendService.acceptFriendRequest(myId, friendId);
        return "success";
    }

    /**
     * /api/friend/refuse
     * 拒绝好友请求
     * @param
     * {
     *     me: 拒绝好友请求 的用户ID
     *     friend: 要拒绝的好友ID
     * }
     * @return "success"
     */
    @CrossOrigin
    @GetMapping("/friend/refuse")
    @ResponseBody
    public String denyFriend(
            @RequestParam("me") Integer myId,
            @RequestParam("friend") Integer friendId) {
        friendService.refuseFriendRequest(myId, friendId);
        return "success";
    }

    @CrossOrigin
    @GetMapping("/friend/delete")
    @ResponseBody
    public String deleteFriend(
            @RequestParam("me") Integer myId,
            @RequestParam("friend") Integer friendId) {
        friendService.deleteFriend(myId, friendId);
        return "success";
    }

    /**
     * /api/message/gf
     * 获取和某个好友的历史消息
     * @param
     * {
     *     me: 请求用户ID
     *     friend: 好友ID
     *     id: 最历史的一条消息的ID
     * }
     */
    @CrossOrigin
    @GetMapping("/message/gf")
    @ResponseBody
    public List<Message> getFriendHistoryMessage(
            @RequestParam("me") Integer myId,
            @RequestParam("friend") Integer friendId,
            @RequestParam("id") Integer id) {
        return messageService.getFriendHistoryMessage(myId, friendId, id, 20);
    }

    /**
     * /api/message/gg
     * 获取某个群里的历史消息
     * @param
     * {
     *     group: 群ID
     *     id: 最历史的一条消息的ID
     * }
     */
    @CrossOrigin
    @GetMapping("/message/gg")
    @ResponseBody
    public List<Message> getFriendHistoryMessage(
            @RequestParam("group") Integer groupId,
            @RequestParam("id") Integer id) {
        return messageService.getGroupHistoryMessage(groupId, id, 20);
    }

    @CrossOrigin
    @GetMapping("/message/read")
    @ResponseBody
    public String readMessage(
            @RequestParam("id") Integer messageId,
            @RequestParam("user") Integer userId) {
        messageService.readMessage(messageId, userId);
        return "success";
    }

    @CrossOrigin
    @PostMapping("/message/send")
    @ResponseBody
    public String sendMessage(@RequestBody JSONObject obj) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Message message = new Message( 0,
                obj.getInteger("from"),
                obj.getInteger("goto"),
                obj.getInteger("type"),
                sdf.parse(obj.getString("time")),
                obj.getString("content"), false);
        messageService.insertMessage(message);
        applicationContext.publishEvent(new Event(EventType.NEW_MESSAGE, message));
        return "success";
    }

    @CrossOrigin
    @GetMapping("/group/search")
    @ResponseBody
    public Group enterGroup(
            @RequestParam("group") Integer groupId) {
        return groupService.searchGroup(groupId);
    }
    /**
     * /api/group/enter
     * 加入群聊
     *
     * @param
     * {
     *     group: 群ID
     *     user: 用户ID
     * }
     * @return "success"
     */
    @CrossOrigin
    @GetMapping("/group/enter")
    @ResponseBody
    public String enterGroup(
            @RequestParam("group") Integer groupId,
            @RequestParam("user") Integer userId) {
        groupService.enterGroup(groupId, userId);
        return "success";
    }

    /**
     * /api/group/exit
     * 退出群聊
     *
     * @param
     * {
     *     group: 群ID
     *     user: 用户ID
     * }
     * @return "success"
     */
    @CrossOrigin
    @GetMapping("/group/exit")
    @ResponseBody
    public String exitGroup(
            @RequestParam("group") Integer groupId,
            @RequestParam("user") Integer userId) {
        groupService.exitGroup(groupId, userId);
        return "success";
    }

    /**
     * /api/login
     * 用户登录
     *
     * @param account: 用户ID或邮箱
     * @return {
     * user: 用户信息,
     * friends: 好友列表,
     * newfriends: 新好友请求
     * groups: 群列表
     * messages: 新消息
     * }
     */
    @CrossOrigin
    @GetMapping("/login")
    @ResponseBody
    public JSONObject userLogin(@RequestParam("account") String account) {
        JSONObject json = new JSONObject();
        // 查找用户信息
        User user = null;
        if (account.indexOf('@') != -1) {
            user = userService.findUserByEmail(account);
        } else {
            user = userService.findUserById(Integer.parseInt(account));
        }
//        user.setPassword("*");
        // 更新用户在线状态
        user.setOnline(true);
        userService.updateOnline(user);
        json.put("user", user);
        // 查找用户好友信息
        List<User> friends = friendService.getFriend(user.getId());
        json.put("friends", friends);
        // 查找用户群组信息
        List<Group> groups = groupService.getUserGroup(user.getId());
        json.put("groups", groups);
        // 获取新好友请求
        List<User> newfriends = friendService.getFriendRequest(user.getId());
        json.put("newfriends", newfriends);
        // 获取未读消息
        List<Message> messages = messageService.getNewMessage(user.getId());
        json.put("messages", messages);
        return json;
    }

    /**
     * /api/login
     * 用户登录
     *
     * @param id: 用户ID
     * @return 0
     */
    @CrossOrigin
    @GetMapping("/logout")
    @ResponseBody
    public Integer userLogout(@RequestParam("id") Integer id) {
        User user = new User();
        user.setId(id);
        user.setOnline(false);
        userService.updateOnline(user);
        return 0;
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = p.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                System.out.println("no file");
            }
        } catch (MalformedURLException e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serverFile(@PathVariable String filename){
        Resource file = loadAsResource(filename);
        return ResponseEntity
                .ok()
                .body(file);
    }

    @CrossOrigin
    @PostMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile file, Integer id) throws IOException {
        System.out.println(id);
        String filename = "user_" + id +".png";
        String path = p.toString();
        file.transferTo(new File(path + "/" + filename));

        return "success";
    }
}
