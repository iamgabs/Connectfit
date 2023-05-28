package com.example.connectfit.interfaces;

import com.example.connectfit.models.entities.UserEntity;

import java.util.List;

public interface StudentsCallback {
    void onStudentsReceived(List<UserEntity> professionals);
    void onFailure(Exception e);
}
