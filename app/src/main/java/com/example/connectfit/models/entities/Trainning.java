package com.example.connectfit.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.connectfit.enums.TrainningLinksEnum;

public class Trainning implements Parcelable {
    private String trainningName;

    private String description;
    private int trainningAmount;
    private String link;

    public Trainning(){};

    public Trainning(String name, String description, int amount){
        this.trainningName = name;
        this.description = description;
        this.trainningAmount = amount;

        // checks if the there is a link for the trainning
        TrainningLinksEnum[] arrayOfTrainnings = TrainningLinksEnum.values();
        for(int i=0; i< arrayOfTrainnings.length; i++) {
            if(arrayOfTrainnings[i].name().replace("_", " ").equalsIgnoreCase(name)){
                this.link = arrayOfTrainnings[i].getValue();
            }
        }
    }

    public Trainning(String name, String description, int amount, String link){
        this.trainningName = name;
        this.description = description;
        this.trainningAmount = amount;
        this.link = link;
    }

    protected Trainning(Parcel in) {
        trainningName = in.readString();
        description = in.readString();
        trainningAmount = in.readInt();
        link = in.readString();
    }

    public String getTrainningName() {
        return trainningName;
    }

    public void setTrainningName(String trainningName) {
        this.trainningName = trainningName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTrainningAmount() {
        return trainningAmount;
    }

    public void setTrainningAmount(int trainningAmount) {
        this.trainningAmount = trainningAmount;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static final Creator<Trainning> CREATOR = new Creator<Trainning>() {
        @Override
        public Trainning createFromParcel(Parcel in) {
            return new Trainning(in);
        }

        @Override
        public Trainning[] newArray(int size) {
            return new Trainning[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trainningName);
        dest.writeString(description);
        dest.writeInt(trainningAmount);
        dest.writeString(link);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
