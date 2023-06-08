package com.example.connectfit.interfaces;

import com.example.connectfit.HomeFragment;
import com.example.connectfit.LoginScreenFragment;
import com.example.connectfit.MainActivity;
import com.example.connectfit.SearchFragment;
import com.example.connectfit.SigninScreenFragment;
import com.example.connectfit.StudentHomeFragment;
import com.example.connectfit.modules.MyAppModules;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MyAppModules.class})
public interface MyAppComponent {
    static void inject(MainActivity activity) {}

    static void inject(LoginScreenFragment fragment) { }

    void inject(SigninScreenFragment fragment);

    static void inject(HomeFragment fragment) {}

    void inject(StudentHomeFragment fragment);
    void inject(SearchFragment fragment);



}
