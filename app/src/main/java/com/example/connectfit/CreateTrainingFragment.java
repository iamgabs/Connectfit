package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.connectfit.adapters.TrainningAdapter;
import com.example.connectfit.databinding.FragmentCreateTrainingBinding;
import com.example.connectfit.interfaces.TrainingAdapterListener;
import com.example.connectfit.models.entities.Trainning;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.TrainingRepository;
import com.example.connectfit.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class CreateTrainingFragment extends Fragment implements TrainingAdapterListener {

    FragmentCreateTrainingBinding binding;
    List<Trainning> trainningList;
    TrainningAdapter adapter;
    TrainingRepository trainingRepository;
    UserEntity userLogged;
    public CreateTrainingFragment() {super(R.layout.fragment_create_training);}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trainingRepository = new TrainingRepository();
        userLogged = new UserEntity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentCreateTrainingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.getUserLogged().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                userLogged = userEntity;

                ListView listView = binding.listview;
                trainningList = new ArrayList<Trainning>();
                adapter = new TrainningAdapter(getContext(), trainningList, CreateTrainingFragment.this);
                listView.setAdapter(adapter);

                // CREATE ONE TRAINING AND ADDING IT TO LIST
                View addButton = binding.addButton;
                addButton.setOnClickListener(view1 -> {
                    String name = String.valueOf(binding.editTextTrainningName.getText());
                    String description = String.valueOf(binding.editTextTrainningDescription.getText());
                    int amount = Integer.parseInt(String.valueOf(binding.editTextTrainningAmount.getText()));
                    String link = String.valueOf(binding.editTextTrainningLink.getText());
                    try {
                        trainningList.add(createNewTraining(name, description, amount, link));
                        // adicionar o nome dos treinos ao list view
                        adapter.notifyDataSetChanged();
                        clearEditTexts();
                    } catch (RuntimeException runtimeException) {
                        if(runtimeException.getMessage().equals("You must to specify the training!")) {
                            createAndShowSnackBar(view, runtimeException.getMessage(), "red");
                        }
                    }
                });

                // PUSH TRAINING
                View createTrainning = binding.createButton;
                createTrainning.setOnClickListener(view1 -> {
                    Utils.getStudentClicked().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
                        @Override
                        public void onChanged(UserEntity student) {
                            trainingRepository.createTraining(student, userLogged, trainningList);
                            createAndShowSnackBar(view, "treino criado com sucesso!", "green");
                            Navigation.findNavController(view).navigate(R.id.homeFragment);
                        }
                    });
                });
            }
        });

    }
    @Override
    public void editTraining(int position){
        Trainning training = trainningList.get(position);

        // set all text fields
        binding.editTextTrainningName.setText(training.getTrainningName());
        binding.editTextTrainningDescription.setText(training.getDescription());
        binding.editTextTrainningAmount.setText(training.getTrainningAmount());
        binding.editTextTrainningLink.setText(training.getLink());
    }

    @Override
    public void deleteTraining(int position) {
        trainningList.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void clearEditTexts() {
        // set all text fields
        binding.editTextTrainningName.setText("");
        binding.editTextTrainningDescription.setText("");
        binding.editTextTrainningAmount.setText("");
        binding.editTextTrainningLink.setText("");
    }

    private Trainning createNewTraining(String name, String description, int amount, String link) throws RuntimeException{
        if(!name.isEmpty() && amount > 0) {
            if(!link.isEmpty()) {
                return new Trainning(name, description, amount, link);
            }
            return new Trainning(name, description, amount);
        }
        throw new RuntimeException("You must to specify the trainning!");
    }

}