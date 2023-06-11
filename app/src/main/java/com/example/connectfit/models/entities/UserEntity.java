package com.example.connectfit.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.connectfit.enums.UserGroupEnum;

import java.util.ArrayList;
import java.util.List;

public class UserEntity implements Parcelable {
    private String id;
    private String name;
    private String password;
    private String email;
    private String specialization;
    private UserGroupEnum userGroupEnum;
    private List<String> subscribers;
    private List<TrainningEntity> trainingList;
    private int notifications;

    public UserEntity() {}

    public UserEntity(String id, String name, String email, String password, UserGroupEnum group, String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userGroupEnum = group;
        this.specialization = specialization;
        this.subscribers = null;
        this.trainingList = null;
        this.notifications = 0;
    }

    protected UserEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        password = in.readString();
        email = in.readString();
        specialization = in.readString();
        subscribers = in.createStringArrayList();
        trainingList = new ArrayList<>();
        in.readTypedList(trainingList, TrainningEntity.CREATOR);
        notifications = in.readInt();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserGroupEnum getUserGroupEnum() {
        return userGroupEnum;
    }

    public void setUserGroupEnum(UserGroupEnum userGroupEnum) {
        this.userGroupEnum = userGroupEnum;
    }

    public String getSpecialization() { return this.specialization; }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<String> subscribers) {
        if(this.userGroupEnum == UserGroupEnum.STUDENT){
            return;
        }
        this.subscribers = subscribers;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }

    public List<TrainningEntity> getTrainingList() {
        return trainingList;
    }

    public void setTrainingList(List<TrainningEntity> trainingList) {
        this.trainingList = trainingList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(specialization);
        parcel.writeValue(userGroupEnum);
        parcel.writeStringList(subscribers);
        parcel.writeList(trainingList);
        parcel.writeInt(notifications);
    }
}
