package com.example.connectfit.models.entities;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TrainningEntity implements Parcelable {
    private String id;
    private List<Trainning> trainningList;
    private String student;
    private String professional;

    public TrainningEntity() {}

    public TrainningEntity(List<Trainning> trainningList, String student, String professional) {
        this.trainningList = trainningList;
        this.student = student;
        this.professional = professional;
        this.id = null;
    }

    protected TrainningEntity(Parcel in) {
        id = in.readString();
        trainningList = new ArrayList<>();
        in.readTypedList(trainningList, Trainning.CREATOR);
        student = in.readString();
        professional = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trainning> getTrainningList() {
        return trainningList;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getStudent() {
        return student;
    }

    public void setTrainningList(List<Trainning> trainningList) {
        this.trainningList = trainningList;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public static final Creator<TrainningEntity> CREATOR = new Creator<TrainningEntity>() {
        @Override
        public TrainningEntity createFromParcel(Parcel in) {
            return new TrainningEntity(in);
        }

        @Override
        public TrainningEntity[] newArray(int size) {
            return new TrainningEntity[size];
        }
    };

    // Getters and Setters

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(trainningList);
        dest.writeString(student);
        dest.writeString(professional);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
