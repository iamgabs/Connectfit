package com.example.connectfit.exceptions;

import com.example.connectfit.models.entities.UserEntity;

import java.util.List;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String s) {
        super(s);
    }
}
