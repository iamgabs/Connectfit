package com.example.connectfit.services.impl;

import android.content.Context;

import com.example.connectfit.interfaces.ProfessionalsCallback;
import com.example.connectfit.interfaces.UsersCallback;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.example.connectfit.services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = new UserRepository();
    }

    @Override
    public void createNewUser(UserEntity user) {
        userRepository.saveUser(user);
    }

    @Override
    public void getUserByEmailAndPassword(String email, String password, Context context, UsersCallback callback) {
        userRepository.getUser(email, password, context, callback);
    }

    @Override
    public UserEntity getUserById(String id) {
        return null;
    }

    @Override
    public void generateToken(Context context) {
        userRepository.getToken(context);
    }

    @Override
    public String getUserToken(Context context) {
        return userRepository.getToken(context);
    }

    @Override
    public void signOut(Context context) {
        userRepository.signout(context);
    }

    @Override
    public void getAllProfessionals(ProfessionalsCallback callback) { userRepository.getAllProfessionals(callback); }
}
