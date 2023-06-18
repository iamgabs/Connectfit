package com.example.connectfit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.fail;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.JvmField;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentInteroperabilityTest {
    static UserEntity user;
    static UserRepository userRepository;

    @JvmField
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        userRepository = new UserRepository();
        user = new UserEntity();
        user.setEmail("creuza@teste.com");
        user.setPassword("teste123");

        // wait splash screen time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAnUser_whenLoggingSuccessfully_thenVerifyActionAtHomeFragmentToListStudentsFragment() {
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.changeScreenFromSigninToLogin)).perform(click());
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginScreenFragment)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.loginButton)).perform(click());

        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Perform action to navigate to listStudentsFragment
            onView(withId(R.id.imageButtonMyTraining)).perform(click());
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void givenAnUser_whenLoggingSuccessfully_thenVerifyActionAtHomeFragmentToUserProfileFragment() {
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.changeScreenFromSigninToLogin)).perform(click());
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginScreenFragment)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.loginButton)).perform(click());

        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Perform action to navigate to UserProfileFragment
            onView(withId(R.id.imageButtonSettings)).perform(click());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void givenAnUser_whenLoggingSuccessfully_thenVerifyActionAtHomeFragmentLogoutUser() {
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.changeScreenFromSigninToLogin)).perform(click());
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginScreenFragment)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.loginButton)).perform(click());

        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // Perform action to logout
            onView(withId(R.id.imageButtonLogout)).perform(click());
        } catch (Exception e) {
            fail();
        }
    }
}
