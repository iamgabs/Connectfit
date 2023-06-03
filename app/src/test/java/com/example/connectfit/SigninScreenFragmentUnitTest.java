package com.example.connectfit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SigninScreenFragmentUnitTest {

    private SigninScreenFragment signinScreenFragment;

//    @BeforeEach
    @BeforeEach
    void setup() {
        signinScreenFragment = new SigninScreenFragment();
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToTrueWhenReceiveAllFields")
    void areValidFields_evaluatesToTrue() {
        boolean result = signinScreenFragment.areValidFields("test", "test@email.com", "test");
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToFalseWhenReceiveOnlyName")
    void areValidFields_evaluatesToFalseWhenReceiveNameOnly() {
        boolean result = signinScreenFragment.areValidFields("test", "", "");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToFalseWhenReceiveOnlyEmail")
    void areValidFields_evaluatesToFalseWhenReceiveEmailOnly() {
        boolean result = signinScreenFragment.areValidFields("", "email@test.com", "");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToFalseWhenReceiveOnlyPassword")
    void areValidFields_evaluatesToFalseWhenReceivePasswordOnly() {
        boolean result = signinScreenFragment.areValidFields("", "", "testpass");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToFalseWhenDoesn'tReceiveAnyValues")
    void areValidFields_evaluatesToFalseWhenDoesntReceiveValues() {
        boolean result = signinScreenFragment.areValidFields("", "", "");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("ChecksIf'sAValidResponseOfUserGroupIfGroupIsSouAluno")
    void validResponseOfUserGroup_evaluatesToTrueIfResponseIsSouAluno() {
        Assertions.assertTrue(signinScreenFragment.validResponseOfUserGroup("sou aluno"));
    }


    @Test
    @DisplayName("ChecksIf'sAValidResponseOfUserGroupIfGroupIsEmpty")
    void validResponseOfUserGroup_evaluatesToFalseIfResponseIsEmpty() {
        Assertions.assertFalse(signinScreenFragment.validResponseOfUserGroup(""));
    }


}
