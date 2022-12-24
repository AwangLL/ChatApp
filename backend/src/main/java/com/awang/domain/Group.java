package com.awang.domain;

public class Group {
    Integer id;
    String name;
    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + id +
                ", name=" + name +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
