package com.example.connectfit;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Utils {

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
}
