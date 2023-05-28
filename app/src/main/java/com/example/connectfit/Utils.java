package com.example.connectfit;

import android.content.Context;
import android.view.View;

import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.services.impl.UserServiceImpl;
import com.google.android.material.snackbar.Snackbar;

public class Utils {

    public static UserGroupEnum group = null;

    public static void createAndShowSnackBar(View view, String msg, String color) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        if(color.equalsIgnoreCase("red")){
            snackbar.setBackgroundTint(view.getResources().getColor(R.color.RED));
        } else if(color.equalsIgnoreCase("green")){
            snackbar.setTextColor(view.getResources().getColor(R.color.black));
            snackbar.setBackgroundTint(view.getResources().getColor(R.color.LIGHT_GREEN));
        }
        snackbar.show();
    }

    public static boolean isUserLogged(Context context){
        UserServiceImpl userService = new UserServiceImpl();
        if(userService.getUserToken(context) != null) {
            return true;
        }
        return false;
    }
}
