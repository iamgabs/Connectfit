package com.example.connectfit.models.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TrainningEntity {
    private String day;
    private List<Trainning> trainnigList;
}
