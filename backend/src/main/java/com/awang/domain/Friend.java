package com.awang.domain;

public class Friend {
    // 通常为发送好友请求的用户
    private Integer id1;
    // 通常为接收好友请求的用户
    private Integer id2;
    // 是否接受好友请求
    private boolean accepted;

    @Override
    public String toString() {
        return "Friend{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", accepted=" + accepted +
                '}';
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public Integer getId2() {
        return id2;
    }

    public void setId2(Integer id2) {
        this.id2 = id2;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}

