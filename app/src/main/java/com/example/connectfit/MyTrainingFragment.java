package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.connectfit.adapters.ListTrainingAdapter;
import com.example.connectfit.adapters.MyProfessionalsAdapter;
import com.example.connectfit.adapters.TrainningAdapter;
import com.example.connectfit.database.UserConfigSingleton;
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
        userLogged = UserConfigSingleton.getInstance().getInstanceOfCurrentUser();
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

        ListView results = binding.resultListView;

        UserEntity currentUser = UserConfigSingleton.getInstance().getInstanceOfCurrentUser();

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

        // FIXME DEU PAU AQUI
        Utils.getTrainningEntity().observe(getViewLifecycleOwner(), new Observer<TrainningEntity>() {
            @Override
            public void onChanged(TrainningEntity trainningEntity) {
                List<Trainning> dataList = trainningEntity.getTrainningList();
                ListTrainingAdapter adapter = new ListTrainingAdapter(getContext(), dataList);
                results.setAdapter(adapter);
                // FIXME

            }
        });



        binding.buttonDelete.setOnClickListener(l -> {
            if(userLogged.getUserGroupEnum() != UserGroupEnum.STUDENT) {
                Utils.getTrainningEntity().observe(getViewLifecycleOwner(), new Observer<TrainningEntity>() {
                    @Override
                    public void onChanged(TrainningEntity trainningEntity) {
                        trainingRepository.deleteTraining(trainningEntity.getId(), student);
                    }
                });
            } else {
            createAndShowSnackBar(view, "Você não tem permissão para deletar este treino!", "red");
            }
        });
    }
}