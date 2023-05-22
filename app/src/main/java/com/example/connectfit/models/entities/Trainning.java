package com.example.connectfit.models.entities;

import com.example.connectfit.enums.TrainningLinksEnum;

public class Trainning {
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
}
