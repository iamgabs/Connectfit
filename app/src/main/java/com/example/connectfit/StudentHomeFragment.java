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
import android.widget.ImageButton;

import com.example.connectfit.databinding.FragmentStudentHomeBinding;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.example.connectfit.utils.Utils;

public class StudentHomeFragment extends Fragment{

    FragmentStudentHomeBinding binding;
    UserRepository userRepository;
    UserEntity currentUser;

    ImageButton trainningButton;
    public StudentHomeFragment(){
        super(R.layout.fragment_student_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false);

        userRepository = new UserRepository();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Utils.getUserLogged().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                currentUser = userEntity;

                trainningButton = binding.imageButtonMyTraining;

                trainningButton.setOnClickListener(l -> {
                    // navegar para a tela de treinos
                    Navigation.findNavController(view).navigate(R.id.myProfessionals);
                });

                binding.imageButtonSearch.setOnClickListener(l -> {
                    // navegar para a tela de busca
                    Navigation.findNavController(view).navigate(R.id.searchFragment);
                });

                binding.imageButtonLogout.setOnClickListener(l -> {
                    // end user session
                    userRepository.signout(getContext());
                    // show snackbar
                    createAndShowSnackBar(view, "sessão encerrada, até mais!", "green");
                    // navigate to login screen
                    Navigation.findNavController(view).navigate(R.id.loginScreenFragment);
                });

            }
        });

    }
}