package com.awang.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Message {
    private Integer id;
    private Integer fromId;
    private Integer gotoId;
    private Integer type;

//    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    private String content;
    private boolean read;

    public Message(Integer id, Integer fromId, Integer gotoId, Integer type, Date time, String content, boolean read) {
        this.id = id;
        this.fromId = fromId;
        this.gotoId = gotoId;
        this.type = type;
        this.time = time;
        this.content = content;
        this.read = read;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", gotoId=" + gotoId +
                ", type=" + type +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", read=" + read +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getGotoId() {
        return gotoId;
    }

    public void setGotoId(Integer gotoId) {
        this.gotoId = gotoId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
