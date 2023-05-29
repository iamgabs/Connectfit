package com.example.connectfit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

    @Test(timeout = 2000)
    void saveSuccessfullyUserWithEmailAndPasswordWithNoExceptions() {
        user = new UserEntity();
        user.setName("username");
        user.setEmail("user@test.com");
        user.setPassword("password3");
        user.setUserGroupEnum(UserGroupEnum.PERSONAL_TREINER);

        when(mAuth.getCurrentUser()).thenReturn(mFirebaseUser);
        when(mFirebaseUser.getUid()).thenReturn("userUID");
        when(mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(mAuthResultTask);
        when(mAuthResultTask.isSuccessful()).thenReturn(true);

        verify(mAuth).createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
        verify(mAuthResultTask).isSuccessful();

        try {
            userRepository.saveUser(user);
        } catch (SigninErrorException signinErrorException) {
            fail(signinErrorException.getMessage());
        }
    }

    @Test(expected = SigninErrorException.class, timeout = 2000)
    void saveUnsuccessfullyUserWithEmailAndPasswordWithException() {
        user = new UserEntity();
        user.setName("username");
        user.setEmail("user@test.com");
        user.setPassword("password3");
        user.setUserGroupEnum(UserGroupEnum.PERSONAL_TREINER);

        when(mAuth.getCurrentUser()).thenReturn(mFirebaseUser);
        when(mFirebaseUser.getUid()).thenReturn("userUID");
        when(mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(mAuthResultTask);
        when(mAuthResultTask.isSuccessful()).thenReturn(false);

        verify(mAuth).createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
        verify(mAuthResultTask).isSuccessful();

        userRepository.saveUser(user);
    }
}
