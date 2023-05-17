package com.example.connectfit.models.entities;

import com.example.connectfit.enums.TrainningLinksEnum;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Trainning {
    private String trainning_name;
    private int getTrainning_amount;
    private TrainningLinksEnum trainningLinksEnum;
}
