package com.example.connectfit.interfaces;

import com.example.connectfit.MainActivity;
import com.example.connectfit.modules.MyAppModules;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MyAppModules.class})
public interface MyAppComponent {
    void inject(MainActivity activity);
}
