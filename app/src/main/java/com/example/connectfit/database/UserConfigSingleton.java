package com.example.connectfit.database;

import com.example.connectfit.models.entities.UserEntity;

public class UserConfigSingleton {
    public static UserConfigSingleton object;
    public UserEntity userObject;

    private UserConfigSingleton(){}

    public static UserConfigSingleton getInstanceOfCurrentUser(UserEntity user){
        if(object == null) {
            object = new UserConfigSingleton();
            object.userObject = user;
        }
        return object;
    }

}
