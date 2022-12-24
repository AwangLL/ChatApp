package com.awang.controller;

import com.alibaba.fastjson.JSONObject;
import com.awang.domain.Message;
import com.awang.service.GroupService;
import com.awang.service.UserService;
import com.awang.util.Event;
import com.awang.util.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@RestController
@RequestMapping("/sse")
public class SSEController {
    private static final ConcurrentHashMap<Integer, SseEmitter> users = new ConcurrentHashMap<>();

    @Autowired
    private GroupService groupService;
    @Autowired
    private ApplicationContext applicationContext;

    public void sendMessageToUser(Integer userId, Message message) {
        if (users.containsKey(userId)) { // 用户在线
            try {
                users.get(userId).send(message);
            } catch (SocketException e) {
                // 发送消息失败，即用户掉线
                users.remove(userId);
            } catch (IOException e) {
                System.out.println("send message IO Exception");
            }
        }
    }

    public void processMessage(Message message) {
        if (message.getType() == 0) { // 好友消息
            sendMessageToUser(message.getGotoId(), message);
        } else if (message.getType() == 1) { // 群聊消息
            Integer groupId = message.getGotoId();
            List<Integer> userIds = groupService.getGroupUserId(groupId);
            userIds.remove(message.getFromId());
            for (Integer userId : userIds) {
                sendMessageToUser(userId, message);
            }
        }
    }

    public void processFriendRequest() {

    }

    // 订阅事件通道
    @CrossOrigin
    @GetMapping(path = "/event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter event(Integer id) {
        System.out.println("user {" + id + "} connected to the sse");
        SseEmitter emitter = new SseEmitter(0L);
        users.put(id, emitter);
        return emitter;
    }

    // 监听动作，发送给客户端数据
    @EventListener(classes = Event.class)
    public void triggerEvent(Event event) throws IOException {
        System.out.println(event);
        switch (event.getType()) {
            case NEW_MESSAGE:
                processMessage((Message) event.getObject());
                break;
            case FRIEND_REQUEST:
                break;
        }
    }

    @GetMapping(value = "get_true")
    @CrossOrigin
    public void getData_True(HttpServletResponse response){
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("utf-8");

        try{
            PrintWriter pw = response.getWriter();
            while (true){
                if (pw.checkError()){
                    System.out.println("客户端断开连接");
                    break;
                }
                Thread.sleep(5000);
                pw.write("data:行情:" + Math.random()+"\n\n");
                pw.flush();
            }
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
