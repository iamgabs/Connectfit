package com.example.connectfit.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.connectfit.models.entities.Trainning;
import com.example.connectfit.models.entities.TrainningEntity;
import com.example.connectfit.models.entities.UserEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TrainingRepository {

    private FirebaseAuth mAuth;

    public TrainingRepository() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void createTraining(UserEntity student, UserEntity professional, List<Trainning> trainingEntity) {
        CollectionReference trainingCollectionRef = FirebaseFirestore.getInstance().collection("training");

        DocumentReference trainingDocRef = trainingCollectionRef.document();
        String trainingId = trainingDocRef.getId();

        // Cria um objeto TrainningEntity com os dados fornecidos
        TrainningEntity trainning = new TrainningEntity();
        trainning.setId(trainingId);
        trainning.setStudent(student.getId());
        trainning.setProfessional(professional.getId());
        trainning.setTrainningList(trainingEntity);

        trainingDocRef.set(trainning)
                .addOnSuccessListener(add -> {
                    DocumentReference studentDocRef = FirebaseFirestore.getInstance().collection("users").document(student.getId());
                    studentDocRef.update("trainingList", FieldValue.arrayUnion(trainingId));
                });
    }

    public LiveData<TrainningEntity> getTraininigById(String id) {
        MutableLiveData<TrainningEntity> trainingEntityResponse = new MutableLiveData<>();

        DocumentReference trainingRef = FirebaseFirestore.getInstance().collection("training").document(id);
        trainingRef.get().addOnCompleteListener(task -> {
          if(task.isSuccessful()) {
              DocumentSnapshot document = task.getResult();
              if(document != null && document.exists()) {
                  List<TrainningEntity> trainningEntityList = new ArrayList<>();
                  TrainningEntity trainning = new TrainningEntity();
                  trainning.setId(document.getId());
                  trainning.setStudent(document.getString("student"));
                  trainning.setProfessional(document.getString("professional"));
                  trainning.setTrainningList((List<Trainning>) document.get("trainningList"));
                  trainningEntityList.add(trainning);
                  trainingEntityResponse.setValue(trainning);
              } else {
                  trainingEntityResponse.setValue(null);
              }
          }
        });

        return trainingEntityResponse;
    }


    public boolean deleteTraining(String id) {
        return false;
    }

}
