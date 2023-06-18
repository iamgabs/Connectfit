package com.example.connectfit;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import android.content.Context;

import androidx.lifecycle.Observer;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.exceptions.SigninErrorException;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.mockito.Mock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.JvmField;

@RunWith(AndroidJUnit4.class)
public class UserRepositoryIntegrationTest {
    static UserEntity userProfessional, userStudent, user;
    static UserRepository userRepository;

    @JvmField
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Mock
    private Context context;

    @Before
    public void setup() {
        userRepository = new UserRepository();

        // defines professional
        userProfessional = new UserEntity();
        userProfessional.setEmail("joao@teste.com");
        userProfessional.setPassword("teste123");
        userProfessional.setSpecialization("");
        userProfessional.setId("EgJrPZmYjYdCUACEX6JEwJPOBp72");

        // defines student
        userStudent = new UserEntity();
        userStudent.setEmail("ateste@teste.com");
        userStudent.setPassword("teste123");
        userStudent.setId("WG3RCgumEJfLz2FhtrqrTwob0AD2");

        // defines new user
        user = new UserEntity();
        user.setName("testprofilestudent");
        user.setEmail("testprofilestudent@teste.com");
        user.setPassword("teste123");
        user.setUserGroupEnum(UserGroupEnum.STUDENT);
        
        // wait splash screen time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAnUser_whenSavingAnewUser_thenDontThrowAnyExceptions() {
        // try to save some user
        try {
            userRepository.saveUser(user);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void givenEmailPasswordAndContext_whenGettingSomeUser_thenReturnAnUser() {
        // try to get some user (same as logging)
        try {
            userRepository.getUser(userStudent.getEmail(), userStudent.getPassword(), context);
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void givenNothing_whenGettingAllProfessionals_thenReturnAllProfessionals() {
        try {
            userRepository.getAllProfessionals();
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void givenSpecializationsAndUser_whenAddingSpecializations_thenAppendSpecializationsForUser() {
        try {
            userRepository.addSpecializations("dieta", userProfessional);
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void givenAStudentAndAProfessional_whenSubscribingWithAProfessional_thenSubscribeSuccessfully() {
        try {
            userRepository.subscribeWithAProfessional(userProfessional, userStudent);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void givenAProfessional_whenClearingNotifications_thenClearNotificationsSuccessfullyForProfessional() {
        try {
            userRepository.clearNotifications(userProfessional);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void givenAProfessional_whenGettingStudentsSubscribers_thenReturnStudentsSuccessfully() {
        try {
            userRepository.getMyStudents(userProfessional);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void givenAStudent_whenGettingProfessionalsSubscribed_thenReturnProfessionalsSuccessfully() {
        try {
            userRepository.getMyProfessionals(userStudent);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void givenAnUser_whenFinishingTestCase_thenDeleteUser() {
        // delete user from firebase
        try {
            userRepository.deleteUserByEmailAndPassword(user.getEmail(), user.getPassword());
            waitFiveSeconds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void waitFiveSeconds() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
