package com.example.connectfit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlin.jvm.JvmField;

@RunWith(AndroidJUnit4.class)
public class LoginScreenFragmentInteroperabilityTest {

    UserEntity user;
    UserRepository userRepository;

    @JvmField
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        userRepository = new UserRepository();
        user = new UserEntity();
        user.setName("testName");
        user.setEmail("testprofile@test.com");
        user.setPassword("userPassword");
        user.setUserGroupEnum(UserGroupEnum.STUDENT);
    }

    public void givenEmailPasswordAndContext_WhenLoggingSomeUser_thenReturnSuccessfullyAnUser() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // do login
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("logado com sucesso!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public void givenEmptyFields_WhenLoggingSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // do login
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Por favor, preencha todos os campos!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public void givenOnlyEmailAndContext_WhenLoggingSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // do login
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Por favor, preencha todos os campos!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public void givenOnlyPasswordAndContext_whenLoggingSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // do login
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withText("Por favor, preencha todos os campos!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public void givenEmailPasswordAndContext_WhenLoggingSomeUser_thenRedirectedToHome() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ActivityScenario.launch(MainActivity.class);

        // do login
        onView(withId(R.id.email)).perform(typeText(user.getEmail()));
        onView(withId(R.id.password)).perform(typeText(user.getPassword()));
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.studentHomeFragment)).check(ViewAssertions.matches(isDisplayed()));
    }

    @After
    public void finishTests_deleteUserTestFromFirebase() {
        userRepository.deleteUserByEmailAndPassword(user.getEmail(), user.getPassword());
    }
}
