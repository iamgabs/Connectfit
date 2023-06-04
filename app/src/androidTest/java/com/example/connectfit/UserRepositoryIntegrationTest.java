package com.example.connectfit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.exceptions.SigninErrorException;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;


@RunWith(AndroidJUnit4ClassRunner.class)
public class UserRepositoryIntegrationTest {

    UserEntity user;
    UserRepository userRepository;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    void setup() {
        userRepository = new UserRepository();
        user = new UserEntity();
        user.setName("testName");
        user.setEmail("usertest@test.com");
        user.setPassword("userPassword");
        user.setUserGroupEnum(UserGroupEnum.STUDENT);
    }

    @Test
    void saveSuccessfullyUserWithEmailAndPasswordWithNoExceptions() {
        try {
            userRepository.saveUser(user);
        } catch (SigninErrorException signinErrorException) {
            fail();
        }

        onView(withText("usuário criado com sucesso!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
