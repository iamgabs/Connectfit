package com.example.connectfit.services;

import android.content.Context;
import android.view.View;

import com.example.connectfit.interfaces.ProfessionalsCallback;
import com.example.connectfit.interfaces.UsersCallback;
import com.example.connectfit.models.entities.UserEntity;

import java.util.List;

public interface UserService {
    public void createNewUser(UserEntity user);
    public void getUserByEmailAndPassword(String email, String password, Context context, UsersCallback callback);
    public UserEntity getUserById(String id);

    public void generateToken(Context context);

    public String getUserToken(Context context);

    public void signOut(Context context);

    public void getAllProfessionals(ProfessionalsCallback callback);
}
