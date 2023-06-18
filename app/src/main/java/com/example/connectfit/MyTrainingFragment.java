package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.connectfit.adapters.ListTrainingAdapter;
import com.example.connectfit.databinding.FragmentMyTrainingBinding;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.Trainning;
import com.example.connectfit.models.entities.TrainningEntity;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.TrainingRepository;
import com.example.connectfit.repositories.UserRepository;
import com.example.connectfit.utils.Utils;

import java.util.ArrayList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * MyTrainingFragment the fragment responsible to show all
 * trainings about user "X" by professional "Y"
 * if it exist
 */
public class MyTrainingFragment extends Fragment {

    FragmentMyTrainingBinding binding;
    UserRepository userRepository;
    TrainingRepository trainingRepository;
    TrainningEntity trainningEntity;
    UserEntity userLogged, professional, student;
    public MyTrainingFragment() { super(R.layout.fragment_my_training); }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        trainingRepository = new TrainingRepository();
        userLogged = new UserEntity();
        professional = new UserEntity();
        student = new UserEntity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyTrainingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Utils.getUserLogged().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                userLogged = userEntity;

                ListView results = binding.resultListView;


                Utils.getProfessionalClicked().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
                    @Override
                    public void onChanged(UserEntity professional) {
                        MyTrainingFragment.this.professional = professional;
                    }
                });

                Utils.getStudentClicked().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
                    @Override
                    public void onChanged(UserEntity student) {
                        MyTrainingFragment.this.student = student;
                    }
                });

                // Get all training in training entity for user X and update list view

                try{
                    Utils.getTrainningEntity().observe(getViewLifecycleOwner(), new Observer<TrainningEntity>() {
                        @Override
                        public void onChanged(TrainningEntity trainningEntity) {
                            if(trainningEntity == null) {
                                createAndShowSnackBar(view, "Nenhum treino cadastrado", "red");
                            }
                            if(trainningEntity.getTrainningList() != null) {
                                String jsonString = String.valueOf(trainningEntity.getTrainningList());
                                List<Trainning> trainings = new ArrayList<>();

                                Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
                                Matcher matcher = pattern.matcher(jsonString);

                                while (matcher.find()) {
                                    String objectString = matcher.group(1);

                                    String link = extractValue(objectString, "link");
                                    String trainningName = extractValue(objectString, "trainningName");
                                    String description = extractValue(objectString, "description");
                                    int trainningAmount = Integer.parseInt(extractValue(objectString, "trainningAmount"));

                                    Trainning training = new Trainning(trainningName, description, trainningAmount, link);
                                    trainings.add(training);
                                }

                                ListTrainingAdapter adapter = new ListTrainingAdapter(getContext(), trainings);
                                results.setAdapter(adapter);

                            } else {
                                createAndShowSnackBar(view, "Nenhum treino cadastrado", "red");
                            }
                        }
                    });

                }catch (NullPointerException nullPointerException){
                    createAndShowSnackBar(view, "Sem treinos para serem exibidos", "red");
                }
            }

            // chat gpt fez a boa aqui
            // extract value for json object format [{... : ...}]
            // obs: not a real json because there is not "key": "value"
            private String extractValue(String objectString, String fieldName) {
                String patternString = fieldName + "=([^,]+)";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(objectString);

                if (matcher.find()) {
                    return matcher.group(1).trim();
                }

                return null;
            }
            });
    }
}