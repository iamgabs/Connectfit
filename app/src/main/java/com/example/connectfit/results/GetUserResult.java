package com.example.connectfit.results;

import com.example.connectfit.models.entities.UserEntity;

import java.util.List;

public class GetUserResult {
    private List<UserEntity> userList;
    private Exception exception;

    public GetUserResult(List<UserEntity> userList) {
        this.userList = userList;
    }

    public GetUserResult(Exception exception) {
        this.exception = exception;
    }

    public List<UserEntity> getUserList() {
        return userList;
    }

    public Exception getException() {
        return exception;
    }
}
