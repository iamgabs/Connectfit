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
    void givenThreeFields_whenCheckingIfAreValidFields_thenEvaluatesToTrue() {
        boolean result = signinScreenFragment.areValidFields("test", "test@email.com", "test");
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToFalseWhenReceiveOnlyName")
    void givenThreeFields_whenCheckingIfAreValidFields_thenEvaluatesToFalseReceivingNameOnly() {
        boolean result = signinScreenFragment.areValidFields("test", "", "");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToFalseWhenReceiveOnlyEmail")
    void givenThreeFields_whenCheckingIfAreValidFields_thenEvaluatesToFalseReceivingEmailOnly() {
        boolean result = signinScreenFragment.areValidFields("", "email@test.com", "");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToFalseWhenReceiveOnlyPassword")
    void givenThreeFields_whenCheckingIfAreValidFields_thenEvaluatesToFalseWhenReceivePasswordOnly() {
        boolean result = signinScreenFragment.areValidFields("", "", "testpass");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("ChecksIfAreValidFieldsEvaluatesToFalseWhenDoesn'tReceiveAnyValues")
    void givenThreeNullFields_whenCheckingIfAreValidFields_thenEvaluatesToFalseDoesntReceiveValues() {
        boolean result = signinScreenFragment.areValidFields("", "", "");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("ChecksIf'sAValidResponseOfUserGroupIfGroupIsSouAluno")
    void givenValidResponseOfUserGroup_whenCheckingUserGroup_thenEvaluatesToTrueIfResponseIsSouAluno() {
        Assertions.assertTrue(signinScreenFragment.validResponseOfUserGroup("sou aluno"));
    }


    @Test
    @DisplayName("ChecksIf'sAValidResponseOfUserGroupIfGroupIsEmpty")
    void givenValidResponseOfUserGroup_whenCheckingUserGroup_thenEvaluatesToFalseIfResponseIsEmpty() {
        Assertions.assertFalse(signinScreenFragment.validResponseOfUserGroup(""));
    }


}
