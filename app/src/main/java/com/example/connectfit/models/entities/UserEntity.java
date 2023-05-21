package com.example.connectfit.models.entities;

import com.example.connectfit.enums.UserGroupEnum;

public class UserEntity {
    private String id;
    private String name;
    private String password;
    private String email;
    private UserGroupEnum userGroupEnum;

    public UserEntity() {}

    public UserEntity(String id, String name, String email, String password, UserGroupEnum group) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userGroupEnum = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserGroupEnum getUserGroupEnum() {
        return userGroupEnum;
    }

    public void setUserGroupEnum(UserGroupEnum userGroupEnum) {
        this.userGroupEnum = userGroupEnum;
    }
}
