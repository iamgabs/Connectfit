package com.example.connectfit;

import static com.example.connectfit.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.connectfit.databinding.FragmentCreateTrainingBinding;
import com.example.connectfit.models.entities.Trainning;

import java.util.ArrayList;
import java.util.List;


public class CreateTrainingFragment extends Fragment {


    FragmentCreateTrainingBinding binding;
    public CreateTrainingFragment() {super(R.layout.fragment_create_training);}

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
        FragmentContainerView listView = binding.fragmentContainerViewTag;
        List<Trainning> trainningList = new ArrayList<Trainning>();

        // CREATE ONE TRAINING AND ADDING IT TO LIST
        View addButton = binding.addButton;
        addButton.setOnClickListener(view1 -> {
            String name = String.valueOf(binding.editTextTrainningName.getText());
            String description = String.valueOf(binding.editTextTrainningDescription.getText());
            int amount = Integer.parseInt(String.valueOf(binding.editTextTrainningAmount.getText()));
            String link = String.valueOf(binding.editTextTrainningLink.getText());
            try {
                trainningList.add(createNewTraining(name, description, amount, link));
                // TODO deve adicionar o nome dos treinos ao list view
            } catch (RuntimeException runtimeException) {
                if(runtimeException.getMessage().equals("You must to specify the training!")) {
                    createAndShowSnackBar(view, runtimeException.getMessage(), "red");
                }
            }
        });


        // PUSH TRAINNING
        View createTrainning = binding.createButton;
        createTrainning.setOnClickListener(view1 -> {

        });

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