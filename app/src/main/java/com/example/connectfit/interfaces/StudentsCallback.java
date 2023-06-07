package com.example.connectfit.interfaces;

import com.example.connectfit.models.entities.UserEntity;

import java.util.List;

public interface StudentsCallback {
    void onStudentsReceived(List<UserEntity> students);
    void onFailure(Exception e);
}
