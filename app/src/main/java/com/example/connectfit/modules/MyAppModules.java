package com.example.connectfit.modules;

import com.example.connectfit.repositories.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyAppModules {
    @Provides
    @Singleton
    UserRepository providesUserRepository(){
        return new UserRepository();
    }
}
