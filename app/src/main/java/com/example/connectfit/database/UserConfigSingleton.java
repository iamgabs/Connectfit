package com.example.connectfit.database;

import com.example.connectfit.models.entities.UserEntity;

public class UserConfigSingleton {
    private static UserConfigSingleton instance;
    private UserEntity userObject;

    private UserConfigSingleton() {}

    public static UserConfigSingleton getInstance() {
        if (instance == null) {
            instance = new UserConfigSingleton();
        }
        return instance;
    }

    public void setInstanceOfCurrentUser(UserEntity user) {
        this.userObject = user;
    }

    public UserEntity getInstanceOfCurrentUser() {
        return userObject;
    }
}

