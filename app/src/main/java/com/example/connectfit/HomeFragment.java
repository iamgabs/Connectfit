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

import com.example.connectfit.databinding.FragmentHomeBinding;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.example.connectfit.utils.Utils;

public class HomeFragment extends Fragment{

    FragmentHomeBinding binding;
    UserRepository userRepository;
    UserEntity currentUser;
    ImageButton trainningButton;
    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

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

                // check user group and switch images if its a professional
                trainningButton.setOnClickListener(l -> {
                    Navigation.findNavController(view).navigate(R.id.listStudentsFragment);
                });

                binding.imageButtonSettings.setOnClickListener(l -> {
                    // navegar para a tela de configurações de usuário
                    Navigation.findNavController(view).navigate(R.id.userProfileFragment);
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