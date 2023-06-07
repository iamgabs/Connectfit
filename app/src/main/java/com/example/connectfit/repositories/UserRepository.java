package com.example.connectfit.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.exceptions.SearchErrorException;
import com.example.connectfit.exceptions.SigninErrorException;
import com.example.connectfit.exceptions.TokenErrorException;
import com.example.connectfit.interfaces.CallbackSpecializations;
import com.example.connectfit.interfaces.ProfessionalsCallback;
import com.example.connectfit.interfaces.StudentsCallback;
import com.example.connectfit.interfaces.UsersCallback;
import com.example.connectfit.models.entities.UserEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserRepository {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
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

                    DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());

                    userRef.set(userData, SetOptions.merge())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.setId(firebaseUser.getUid());
                                        UserConfigSingleton.getInstance().setInstanceOfCurrentUser(user);
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
     * @param callback it's the callback of the method
     * @throws SigninErrorException if there is no user in firebase
     * @method getUser should "return" an user from firebase
     */
    public void getUser(String email, String password, Context context, UsersCallback callback) throws SigninErrorException {
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
                        generateToken(context);

                        List<UserEntity> list = new ArrayList<>();
                        list.add(userEntity);

                        callback.onUsersReceived(list);

                    } else {
                        callback.onFailure(task.getException());
                    }
                });
            } else {
                throw new StringIndexOutOfBoundsException("Erro ao logar!");
            }
        });
    }

    public UserEntity getUserById(String id) {
        return null;
    }

    // método para perar token do usuário do firebase

    /**
     * @param context is the application context
     * @method generateToken should generate (get from firebase) a token
     * and save it on SharedPreferences
     */
    public void generateToken(Context context) throws TokenErrorException {
        Objects.requireNonNull(mAuth.getCurrentUser()).getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            SharedPreferences sharedPreferences = context.getSharedPreferences("ConnectFitToken", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loginToken", token);
                            editor.apply();
                        } else {
                            throw new TokenErrorException("Erro ao obter o token de login");
                        }
                    }
                });
    }

    /**
     * @param context is the application context
     * @return the token
     * @method getToken should getToken from SharedPreferences
     */
    public String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ConnectFitToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString("loginToken", null);
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
        UserConfigSingleton.getInstance().setInstanceOfCurrentUser(null);
    }

    /**
     * @param callback is the callback of professionals references ProfessionalsCallback.class
     * @method getAllProfessionals should get a list of all professionals
     */
    public void getAllProfessionals(ProfessionalsCallback callback) {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        Query query = usersRef.whereIn("group", Arrays.asList("PERSONAL_TRAINER", "NUTRITIONIST"));

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<UserEntity> listOfProfessionals = new ArrayList<UserEntity>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    UserEntity user = new UserEntity();
                    user.setId(document.getId());
                    user.setName(document.getString("name"));
                    user.setEmail(document.getString("email"));
                    user.setSpecialization(document.getString("specializations"));
                    listOfProfessionals.add(user);
                }

                callback.onProfessionalsReceived(listOfProfessionals);
            } else {
                callback.onFailure(task.getException());
            }
        });
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

    public void getMySpecializations(String id, CallbackSpecializations callback) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(id);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String specializations = document.getString("specializations");
                callback.onSpecializationReceived(specializations);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    /**
     * @param professional is the professional which 'student' will subscribe
     * @param student      is the student that will subscribe in
     * @method subscribeWithAProfessional should subscribe a student to a professional
     */
    public void subscribeWithAProfessional(UserEntity professional, UserEntity student) {
        // TODO adicionar ao campo subscribers (Array) o id do aluno
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
     * @param callback is the callback interface
     */
    public void getMyStudents(UserEntity user, StudentsCallback callback) {
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
                                        UserEntity student = userDoc.toObject(UserEntity.class);
                                        studentsArray.add(student);
                                    }
                                }
                                callback.onStudentsReceived(studentsArray);
                            } else {
                                callback.onFailure(usersTask.getException());
                            }
                        });
                    } else {
                        callback.onFailure(task.getException());
                    }
                } else {
                    callback.onFailure(task.getException());
                }
            }
        });
    }


}
