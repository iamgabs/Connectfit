package com.example.connectfit;

import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.Trainning;
import com.example.connectfit.models.entities.TrainningEntity;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.TrainingRepository;
import com.example.connectfit.repositories.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.JvmField;

@RunWith(AndroidJUnit4.class)
public class TrainingRepositoryIntegrationTest {
    static UserEntity userProfessional, userStudent, user;
    static TrainingRepository trainingRepository;

    @JvmField
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Mock
    private Context context;

    @Before
    public void setup() {
        trainingRepository = new TrainingRepository();

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

        // wait splash screen time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenAStudentAProfessionalAndATrainningEntity_whenCreateANewTraining_thenCreateSuccessfully() {
        List<Trainning> trainningList = new ArrayList<>();
        Trainning t1 = new Trainning();
        Trainning t2 = new Trainning();
        t1.setTrainningName("cadeira chinesa");
        t1.setDescription("realziar por 1 minuto");
        t1.setTrainningAmount(3);

        t2.setTrainningName("flexão");
        t2.setDescription("realziar segundo o vídeo");
        t2.setTrainningAmount(20);
        t2.setLink("https://www.youtube.com/watch?v=F9FC_KBsLpY&ab_channel=LeandroTwin");

        trainningList.add(t1);
        trainningList.add(t2);

        try {
            trainingRepository.createTraining(userStudent, userProfessional, trainningList);
            waitFiveSeconds();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void givenAStudentIDAndAProfessionalID_whenGettingSomeTraining_thenReturnSuccessfully() {
        try {
            trainingRepository.getTraininigById(userStudent.getId(), userProfessional.getId());
            waitFiveSeconds();
        } catch (Exception e) {
            fail();
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
