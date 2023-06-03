package com.example.connectfit.utils;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.connectfit.MainActivity;
import com.example.connectfit.R;
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


    public static void createAndShowNotificationWithVibration(Context context, String title, String message){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // create notification
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.connect_fit_main_img)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // get notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // show notification
        notificationManager.notify(1, notificationBuilder.build());

        // start vibration
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(500);
        }
    }


    public static boolean isUserLogged(Context context){
        UserServiceImpl userService = new UserServiceImpl();
        if(userService.getUserToken(context) != null) {
            return true;
        }
        return false;
    }
}
