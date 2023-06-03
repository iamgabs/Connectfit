package com.example.connectfit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.exceptions.SigninErrorException;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


public class UserRepositoryIntegrationTest {

    @Mock
    UserRepository userRepository;

    @Mock
    private FirebaseAuth mAuth;

    @Mock
    private FirebaseUser mFirebaseUser;

    @Mock
    private Task<AuthResult> mAuthResultTask;

    @Mock
    private Task<Void> voidTask;

    UserEntity user;

    @Test
    void saveSuccessfullyUserWithEmailAndPasswordWithNoExceptions() {
        user = new UserEntity();
        user.setName("username");
        user.setEmail("user@test.com");
        user.setPassword("password3");
        user.setUserGroupEnum(UserGroupEnum.PERSONAL_TRAINER);

        when(mAuth.getCurrentUser()).thenReturn(mFirebaseUser);
        when(mFirebaseUser.getUid()).thenReturn("userUID");
        when(mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(mAuthResultTask);
        when(mAuthResultTask.isSuccessful()).thenReturn(true);

        verify(mAuth).createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
        verify(mAuthResultTask).isSuccessful();

        Assertions.assertThatCode(() -> {
            userRepository.saveUser(user);
        }).doesNotThrowAnyException();
    }

    @Test
    void saveUnsuccessfullyUserWithEmailAndPasswordWithException() {
        user = new UserEntity();
        user.setName("username");
        user.setEmail("user@test.com");
        user.setPassword("password3");
        user.setUserGroupEnum(UserGroupEnum.PERSONAL_TRAINER);

        when(mAuth.getCurrentUser()).thenReturn(mFirebaseUser);
        when(mFirebaseUser.getUid()).thenReturn("userUID");
        when(mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(mAuthResultTask);
        when(mAuthResultTask.isSuccessful()).thenReturn(false);

        verify(mAuth).createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
        verify(mAuthResultTask).isSuccessful();

        org.junit.jupiter.api.Assertions.assertThrows(SigninErrorException.class, () -> {
            userRepository.saveUser(user);
        });
    }
}
