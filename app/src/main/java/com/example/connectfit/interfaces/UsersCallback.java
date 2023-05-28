package com.example.connectfit.interfaces;

import com.example.connectfit.models.entities.UserEntity;

import java.util.List;

public interface UsersCallback {
    void onUsersReceived(List<UserEntity> users);
    void onFailure(Exception e);
}
