package com.example.connectfit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kotlin.jvm.JvmField;

/**
 * Before all tests, should wait 5seconds (splash screen time)
 */

@RunWith(AndroidJUnit4.class)
public class SigninScreenFragmentInteroperabilityTest {
    static UserEntity user;
    static UserRepository userRepository;

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

        // wait splash screen time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenNameEmailPasswordAndGroup_WhenSigningSomeUser_thenReturnSuccessfullyAConfirmationMessage() {
        // do signup
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.signinUserName)).perform(typeText(user.getName()));
        onView(withId(R.id.signinUserEmail)).perform(typeText(user.getEmail()));
        onView(withId(R.id.signinUserPassword)).perform(typeText(user.getPassword()));
        onView(withId(R.id.radioButtonStudent)).perform(click());

        onView(withId(R.id.signinButton)).perform(click());

        onView(withText("usuário criado com sucesso!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void givenOnlyEmailGroupAndPassword_WhenSigningSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        // do signup
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.signinUserEmail)).perform(typeText(user.getEmail()));
        onView(withId(R.id.signinUserPassword)).perform(typeText(user.getPassword()));
        onView(withId(R.id.radioButtonStudent)).perform(click());

        onView(withId(R.id.signinButton)).perform(click());

        onView(withText("Por favor, verifique os campos"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void givenOnlyPasswordAndGroup_WhenSigningSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        // do signup
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.signinUserPassword)).perform(typeText(user.getPassword()));
        onView(withId(R.id.radioButtonStudent)).perform(click());

        onView(withId(R.id.signinButton)).perform(click());

        onView(withText("Por favor, verifique os campos"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void givenOnlyNameEmailAndGroup_WhenSigningSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        // do signup
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.signinUserName)).perform(typeText(user.getName()));
        onView(withId(R.id.signinUserEmail)).perform(typeText(user.getEmail()));
        onView(withId(R.id.radioButtonStudent)).perform(click());

        onView(withId(R.id.signinButton)).perform(click());

        onView(withText("Por favor, verifique os campos"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void givenOnlyNameEmailAndPassword_WhenSigningSomeUser_thenReturnUnsuccessfullyAnErrorMessage() {
        // do signup
        onView(withId(R.id.changeScreenFromLoginToSignin)).perform(click());
        onView(withId(R.id.signinUserName)).perform(typeText(user.getName()));
        onView(withId(R.id.signinUserEmail)).perform(typeText(user.getEmail()));
        onView(withId(R.id.signinUserPassword)).perform(typeText(user.getPassword()));

        onView(withId(R.id.signinButton)).perform(click());

        onView(withText("Você deve escolher um grupo de usuário!"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

}
