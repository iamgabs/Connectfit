package com.example.connectfit.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.connectfit.Utils;
import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.exceptions.SearchErrorException;
import com.example.connectfit.exceptions.SigninErrorException;
import com.example.connectfit.exceptions.TokenErrorException;
import com.example.connectfit.interfaces.ProfessionalsCallback;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserRepository {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public UserRepository(){
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    /** @method saveUser should create a new user in firebase with email and password
     *  @param user is an user with email and password only
     * */
    public void saveUser(UserEntity user) throws SigninErrorException{
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
                        UserEntity userEntity = new UserEntity(firebaseUser.getUid(), name, email, password, group);
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

    public UserEntity getUserById(String id){return null;}

    // método para perar token do usuário do firebase
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

    public String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ConnectFitToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString("loginToken", null);
    }

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

    public void getAllProfessionals(ProfessionalsCallback callback) {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        Query query = usersRef.whereIn("group", Arrays.asList("PERSONAL", "NUTRITIONIST"));

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<UserEntity> listOfProfessionals = new ArrayList<UserEntity>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    UserEntity user = new UserEntity();
                    user.setId(document.getId());
                    user.setName(document.getString("name"));
                    user.setEmail(document.getString("email"));
                    listOfProfessionals.add(user);
                }

                callback.onProfessionalsReceived(listOfProfessionals);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public void addSpecializations(List<String> specializations, UserEntity user) {
        Map<String, Object> userData = new HashMap<>();
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
}
