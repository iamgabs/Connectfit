package com.example.connectfit.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.exceptions.SearchErrorException;
import com.example.connectfit.exceptions.SigninErrorException;
import com.example.connectfit.models.entities.Trainning;
import com.example.connectfit.models.entities.TrainningEntity;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.results.GetUserResult;
import com.example.connectfit.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class UserRepository is the repository class for collection
 * "user" in firebase
 */
public class UserRepository {

    private FirebaseAuth mAuth;
    private TrainingRepository trainingRepository;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        trainingRepository = new TrainingRepository();
    }

    /**
     * @param user is an user with email and password only
     * @method saveUser should create a new user in firebase with email and password
     */
    public void saveUser(UserEntity user) throws SigninErrorException {
        // calls firebase function 'createUserWithEmailAndPassword'
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    // creating an object map to store extra data
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("id", firebaseUser.getUid());
                    userData.put("name", user.getName());
                    userData.put("group", user.getUserGroupEnum());
                    userData.put("specializations", "");
                    List<String> subscribersEmptyList = new ArrayList<>();
                    userData.put("subscribers", subscribersEmptyList);
                    userData.put("notifications", 0);
                    List<String> trainingStringEmptyList = new ArrayList<>();
                    userData.put("trainingList", trainingStringEmptyList);

                    DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());

                    userRef.set(userData, SetOptions.merge())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.setId(firebaseUser.getUid());
                                        Utils.setUserLoggedLiveData(user);
                                    }
                                }
                            });
                } else {
                    throw new SigninErrorException("Could not create new user!");
                }
            }
        });
    }

    /**
     * @param email    string of user email
     * @param password string of user password
     * @param context  application context
     * @param context it's the fragment context
     * @throws SigninErrorException if there is no user in firebase
     * @method getUser should "return" an user from firebase
     */
    public LiveData<GetUserResult> getUser(String email, String password, Context context) {
        MutableLiveData<GetUserResult> loginResultLiveData = new MutableLiveData<>();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authentication -> {
            if (authentication.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());
                userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String name = document.getString("name");
                        UserGroupEnum group = UserGroupEnum.valueOf(document.getString("group"));
                        String specialization = document.getString("specializations");
                        List<String> subscribers = (List<String>) document.get("subscribers");

                        Long notificationsLong = document.getLong("notifications");
                        int notifications = notificationsLong != null ? notificationsLong.intValue() : 0;

                        UserEntity userEntity = new UserEntity(firebaseUser.getUid(), name, email, password, group, specialization);
                        userEntity.setNotifications(notifications);

                        List<String> trainingListString = (List<String>) document.get("trainingList");

                        List<TrainningEntity> trainingList = new ArrayList<>();

                        if (trainingListString != null) {
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                            for (String trainingId : trainingListString) {
                                DocumentReference trainingRef = firestore.collection("training").document(trainingId);
                                trainingRef.get().addOnCompleteListener(trainingTask -> {
                                    if (trainingTask.isSuccessful()) {
                                        DocumentSnapshot trainingDoc = trainingTask.getResult();

                                        String trainningId = trainingDoc.getString("id");
                                        String professionalId = trainingDoc.getString("professional");
                                        String studentId = trainingDoc.getString("student");
                                        List<Trainning> trainningList = (List<Trainning>) trainingDoc.get("trainningList");

                                        TrainningEntity trainingEntity = new TrainningEntity();
                                        trainingEntity.setId(trainingId);
                                        trainingEntity.setProfessional(professionalId);
                                        trainingEntity.setStudent(studentId);
                                        trainingEntity.setTrainningList(trainningList);

                                        trainingList.add(trainingEntity);
                                    } else {
                                        loginResultLiveData.setValue(new GetUserResult(new SigninErrorException("Erro ao obter usuário!")));
                                    }

                                    if (trainingList.size() == trainingListString.size()) {
                                        userEntity.setTrainingList(trainingList);
                                    }
                                });
                            }
                        } else {
                            userEntity.setTrainingList(null);
                        }

                        List<UserEntity> userList = new ArrayList<>();
                        userList.add(userEntity);

                        loginResultLiveData.setValue(new GetUserResult(userList));
                    } else {
                        loginResultLiveData.setValue(new GetUserResult(task.getException()));
                    }
                });
            } else {
                loginResultLiveData.setValue(new GetUserResult(new SigninErrorException("Erro ao obter usuário!")));
            }
        });

        return loginResultLiveData;
    }

    /**
     * @param context is the application context
     * @method signout should logout the user of application
     */
    public void signout(Context context) {
        // signout firebase
        mAuth.signOut();

        // remove user login token
        SharedPreferences sharedPreferences = context.getSharedPreferences("ConnectFitToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("loginToken");
        editor.apply();

        // remove user
        Utils.setUserLoggedLiveData(null);
    }

    /**
     * @method getAllProfessionals should get a list of all professionals
     */

    public LiveData<List<UserEntity>> getAllProfessionals() {
        MutableLiveData<List<UserEntity>> professionalsLiveData = new MutableLiveData<>();

        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        Query query = usersRef.whereIn("group", Arrays.asList("PERSONAL_TRAINER", "NUTRITIONIST"));

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<UserEntity> listOfProfessionals = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    UserEntity user = new UserEntity();
                    user.setId(document.getId());
                    user.setName(document.getString("name"));
                    user.setEmail(document.getString("email"));
                    String specialization = document.getString("specializations");
                    user.setSpecialization(specialization);
                    listOfProfessionals.add(user);
                }

                professionalsLiveData.setValue(listOfProfessionals);
            } else {
                professionalsLiveData.setValue(null);
            }
        });

        return professionalsLiveData;
    }


    /**
     * @param specializations
     * @param user
     * @method addSpecializations should UPDATE firebase "users.specializations"
     * with a list of the old specializations + all specializations
     */
    public void addSpecializations(String specializations, UserEntity user) throws SearchErrorException {
        Map<String, String> userData = new HashMap<>();
        userData.put("specializations", specializations);

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(user.getId());

        userRef.set(userData, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isCanceled()) {
                            throw new SearchErrorException("Não foi possível adicionar!");
                        }
                    }
                });
    }

    /**
     * @method subscribeWithAProfessional should be responsible to subscribe a student
     * in a professional
     * @param professional is the professional which 'student' will subscribe
     * @param student      is the student that will subscribe in
     * @method subscribeWithAProfessional should subscribe a student to a professional
     */
    public void subscribeWithAProfessional(UserEntity professional, UserEntity student) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(professional.getId());

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    List<String> existingArray = (List<String>) document.get("subscribers");
                    if (existingArray == null) {
                        existingArray = new ArrayList<>();
                    }
                    existingArray.add(student.getId());
                    userRef.update("subscribers", existingArray)
                            .addOnSuccessListener(aVoid -> {
                                userRef.update("notifications", 1);
                            })
                            .addOnFailureListener(e -> {
                                throw new SearchErrorException("Não foi possível atualizar os dados!");
                            });
                }
            } else {
                throw new SearchErrorException("Não foi possível receber os dados!");
            }
        });
    }

    /***
     * @method clearNotifications should clear all notifications for 'user'
     * @param user is the user that will get your notifications to 0
     */
    public void clearNotifications(UserEntity user) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(user.getId());

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userRef.update("notifications", 0);
            }
        });
    }


    /***
     * @method getMyStudents should get all subscribers for user "n"
     * if this user has subscribers
     * @param user is the professional which the method will filtering
     */
    public LiveData<List<UserEntity>> getMyStudents(UserEntity user) {
        MutableLiveData<List<UserEntity>> studentsLiveData = new MutableLiveData<>();

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(user.getId());

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    List<UserEntity> studentsArray = new ArrayList<>();
                    List<String> existingArrayOfStudentsId = (List<String>) document.get("subscribers");
                    if (existingArrayOfStudentsId != null) {
                        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("users");
                        usersCollection.get().addOnCompleteListener(usersTask -> {
                            if (usersTask.isSuccessful()) {
                                for (QueryDocumentSnapshot userDoc : usersTask.getResult()) {
                                    if (existingArrayOfStudentsId.contains(userDoc.getId())) {
                                        UserEntity student = new UserEntity();
                                        student.setId(userDoc.getId());
                                        student.setName(userDoc.getString("name"));
                                        student.setSpecialization(userDoc.getString("specializaitons"));
                                        student.setSubscribers((List<String>) userDoc.get("subscribers"));
                                        Long notificationsLong = userDoc.getLong("notifications");
                                        int notifications = notificationsLong != null ? notificationsLong.intValue() : 0;
                                        student.setNotifications(notifications);

                                        List<String> trainingListString = (List<String>) userDoc.get("trainingList");

                                        List<TrainningEntity> trainingList = new ArrayList<>();

                                        // convert list<String> of training into a list<UserEntity>
                                        if (trainingListString != null) {
                                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                                            for (String trainingId : trainingListString) {
                                                DocumentReference trainingRef = firestore.collection("training").document(trainingId);
                                                trainingRef.get().addOnCompleteListener(trainingTask -> {
                                                    if (trainingTask.isSuccessful()) {
                                                        DocumentSnapshot trainingDoc = trainingTask.getResult();

                                                        String trainningId = trainingDoc.getString("id");
                                                        String professionalId = trainingDoc.getString("professional");
                                                        String studentId = trainingDoc.getString("student");
                                                        List<Trainning> trainningList = (List<Trainning>) trainingDoc.get("trainningList");

                                                        TrainningEntity trainingEntity = new TrainningEntity();
                                                        trainingEntity.setId(trainningId);
                                                        trainingEntity.setProfessional(professionalId);
                                                        trainingEntity.setStudent(studentId);
                                                        trainingEntity.setTrainningList(trainningList);
                                                        trainingList.add(trainingEntity);
                                                    } else {
                                                        studentsLiveData.setValue(null);
                                                    }

                                                    if (trainingList.size() == trainingListString.size()) {
                                                        student.setTrainingList(trainingList);
                                                    }
                                                });
                                            }
                                        } else {
                                            student.setTrainingList(null);
                                        }
                                        studentsArray.add(student);
                                    }
                                }
                                studentsLiveData.setValue(studentsArray);
                            } else {
                                studentsLiveData.setValue(null);
                            }
                        });
                    } else {
                        studentsLiveData.setValue(null);
                    }
                } else {
                    studentsLiveData.setValue(null);
                }
            } else {
                studentsLiveData.setValue(null);
            }
        });

        return studentsLiveData;
    }

    /**
     * @method getMyProfessionals should get all professionals for user x
     * @param student is the student we are looking for professionals
     * @return a list of UserEntity (with all professionals)
     */
    public LiveData<List<UserEntity>> getMyProfessionals(UserEntity student) {
        MutableLiveData<List<UserEntity>> professionalsLiveData = new MutableLiveData<>();
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");

        usersRef.whereArrayContains("subscribers", student.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<UserEntity> professionalsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserEntity professional = new UserEntity();
                            professional.setId(document.getId());
                            professional.setName(document.getString("name"));
                            professional.setSpecialization(document.getString("specializaitons"));
                            professional.setSubscribers((List<String>) document.get("subscribers"));
                            Long notificationsLong = document.getLong("notifications");
                            int notifications = notificationsLong != null ? notificationsLong.intValue() : 0;
                            professional.setNotifications(notifications);
                            professional.setTrainingList(null);

                            professionalsList.add(professional);
                        }
                        professionalsLiveData.setValue(professionalsList);
                    } else {
                        professionalsLiveData.setValue(null);
                    }
                });

        return professionalsLiveData;
    }

    /** this is an auxiliary test method
     * this method shouldn't be executed out of test scope
     * @param email is the user email
     * @param password is the user password
     * with theses params, the method will get the user and delete
     * all information from authentication and documents
     * which are associated with "user x"
     */
    public LiveData<Boolean> deleteUserByEmailAndPassword(String email, String password) {
        MutableLiveData<Boolean> response = new MutableLiveData<>();
        response.setValue(false);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    DocumentReference userDocRef = FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(userId);

                    user.delete()
                            .addOnCompleteListener(deleteUserTask -> {
                                if (deleteUserTask.isCanceled()) {
                                    response.setValue(false);
                                } else {
                                    // delete from document "users"
                                    userDocRef.delete().addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isCanceled()) {
                                            response.setValue(false);
                                        } else if (deleteTask.isSuccessful()) {
                                            response.setValue(true);
                                        }

                                    });
                                }
                            });
                } else {
                    response.setValue(false);
                }
            } else {
                response.setValue(false);
            }
        });
        return response;
    }


}
