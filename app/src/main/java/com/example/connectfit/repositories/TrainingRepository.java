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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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

    public LiveData<TrainningEntity> getTraininigById(String studentId, String professionalId) {
        MutableLiveData<TrainningEntity> trainingEntityResponse = new MutableLiveData<>();
        CollectionReference trainingCollectionRef = FirebaseFirestore.getInstance().collection("training");

        Query query = trainingCollectionRef
                .whereEqualTo("professional", professionalId)
                .whereEqualTo("student", studentId)
                .limit(1);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    DocumentSnapshot trainingEntity = snapshot.getDocuments().get(0);
                    TrainningEntity trainning = new TrainningEntity();
                    trainning.setId(trainingEntity.getId());
                    trainning.setStudent(trainingEntity.getString("student"));
                    trainning.setProfessional(trainingEntity.getString("professional"));
                    trainning.setTrainningList((List<Trainning>) trainingEntity.get("trainningList"));
                    trainingEntityResponse.setValue(trainning);
                } else {
                    trainingEntityResponse.setValue(null);
                }
            } else {
                trainingEntityResponse.setValue(null);
            }
        });

        return trainingEntityResponse;
    }

    public LiveData<Boolean> deleteTraining(String id, UserEntity student) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        DocumentReference trainingDocRef = FirebaseFirestore.getInstance().collection("training").document(id);

        trainingDocRef.delete().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("users").document(student.getId());
               userDocRef.update("trainingList", FieldValue.arrayRemove(id))
                       .addOnCompleteListener(task1 -> {
                           if (task1.isSuccessful()) {
                               result.setValue(true);
                           } else {
                               result.setValue(false);
                           }
                       });
           } else {
               result.setValue(false);
           }
        });

        return result;
    }

}
