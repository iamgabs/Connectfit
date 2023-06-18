package com.example.connectfit.utils;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.connectfit.R;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.TrainningEntity;
import com.example.connectfit.models.entities.UserEntity;
import com.google.android.material.snackbar.Snackbar;

public class Utils {
    private static MutableLiveData<UserEntity> studentLiveData = new MutableLiveData<>();
    private static MutableLiveData<TrainningEntity> trainningEntityMutableLiveData = new MutableLiveData<>();
    private static MutableLiveData<UserEntity> professionalLiveData = new MutableLiveData<>();
    private static MutableLiveData<UserEntity> userLoggedLiveData = new MutableLiveData<>();

    public static UserGroupEnum group = null;

    public static void createAndShowSnackBar(View view, String msg, String color) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        if(color.equalsIgnoreCase("red")){
            snackbar.setBackgroundTint(view.getResources().getColor(R.color.RED));
        } else if(color.equalsIgnoreCase("green")){
            snackbar.setTextColor(view.getResources().getColor(R.color.black));
            snackbar.setBackgroundTint(view.getResources().getColor(R.color.LIGHT_GREEN));
        } else if(color.equalsIgnoreCase("blue")) {
            snackbar.setBackgroundTint(view.getResources().getColor(R.color.LIGHT_BLUE));
        }
        snackbar.show();
    }


    public static void createAndShowNotificationWithVibration(Context context, View view, String message){
        createAndShowSnackBar(view, message, "blue");
        // start vibration
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(500);
        }
    }

    public static void setStudentClicked(UserEntity student) {
        studentLiveData.setValue(student);
    }

    public static LiveData<UserEntity> getStudentClicked() {
        return studentLiveData;
    }

    public static void setProfessionalClicked(UserEntity professional) {
        professionalLiveData.setValue(professional);
    }

    public static LiveData<UserEntity> getProfessionalClicked() {
        return professionalLiveData;
    }


    public static void setTrainningEntity(TrainningEntity t) {
        trainningEntityMutableLiveData.setValue(t);
    }

    public static LiveData<TrainningEntity> getTrainningEntity() {
        return trainningEntityMutableLiveData;
    }

    public static void setUserLoggedLiveData(UserEntity userLoggedLiveData) {
        Utils.userLoggedLiveData.setValue(userLoggedLiveData);
    }

    public static LiveData<UserEntity> getUserLogged() {
        return userLoggedLiveData;
    }
}
