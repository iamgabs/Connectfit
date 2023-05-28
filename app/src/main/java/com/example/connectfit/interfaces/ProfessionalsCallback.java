package com.example.connectfit.interfaces;

import com.example.connectfit.models.entities.UserEntity;

import java.util.List;

public interface ProfessionalsCallback {
    void onProfessionalsReceived(List<UserEntity> professionals);
    void onFailure(Exception e);
}
