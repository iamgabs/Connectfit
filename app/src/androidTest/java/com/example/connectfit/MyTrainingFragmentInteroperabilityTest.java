package com.example.connectfit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
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
public class MyTrainingFragmentInteroperabilityTest {
    static UserEntity user;
    static UserRepository userRepository;

    @JvmField
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        userRepository = new UserRepository();
        user = new UserEntity();
        user.setEmail("ateste@teste.com");
        user.setPassword("teste123");

        // wait splash screen time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAnUser_whenLoggingSuccessfully_thenVerifyActionAtUserProfileFragment() {
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.changeScreenFromSigninToLogin)).perform(click());
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginScreenFragment)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.loginButton)).perform(click());

        waitFiveSeconds();

        onView(withId(R.id.imageButtonMyTraining)).perform(click());

        waitFiveSeconds();

        onView(withText("creuza")).perform(click());

        onView(withText("Sim")).perform(click());

        waitFiveSeconds();

        // perform access to my training fragment
        onView(withText("cadeira chinesa"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("realizar por 30 segundos"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("3"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("https://youtu.be/k9ETzvj6TIs"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));


        onView(withText("realizar segundo o video"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("https://youtube.com/video"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("20"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("barra"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
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
