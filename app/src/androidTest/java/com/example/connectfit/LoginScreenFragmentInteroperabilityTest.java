package com.example.connectfit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.fail;

import androidx.test.espresso.assertion.ViewAssertions;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.JvmField;

@RunWith(AndroidJUnit4.class)
public class LoginScreenFragmentInteroperabilityTest {

    static UserEntity user;
    static UserRepository userRepository;

    @JvmField
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @BeforeClass
    public static void before() {
        userRepository = new UserRepository();
        user = new UserEntity();
        user.setEmail("ateste@teste.com");
        user.setPassword("teste123");
    }

    @Before
    public void setup() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAnUser_whenLogging_thenSuccessfullyLoggingApplication() {
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.changeScreenFromSigninToLogin)).perform(click());
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginScreenFragment)).check(ViewAssertions.matches(isDisplayed()));

        try {
            onView(withId(R.id.loginButton)).perform(click());
        } catch (Exception e) {
            fail();
        }
    }

        @Test
    public void givenEmptyFields_WhenLoggingSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        // do login
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.changeScreenFromSigninToLogin)).perform(click());
        onView(withId(R.id.loginScreenFragment)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Por favor, preencha todos os campos!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void givenOnlyEmailAndContext_WhenLoggingSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        // do login
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.changeScreenFromSigninToLogin)).perform(click());
        onView(withId(R.id.loginScreenFragment)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Por favor, preencha todos os campos!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void givenOnlyPasswordAndContext_whenLoggingSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        // do login
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.changeScreenFromSigninToLogin)).perform(click());
        onView(withId(R.id.loginScreenFragment)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Por favor, preencha todos os campos!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
