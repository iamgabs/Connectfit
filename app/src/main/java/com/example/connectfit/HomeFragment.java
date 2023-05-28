package com.example.connectfit;

import static com.example.connectfit.Utils.createAndShowSnackBar;
import static com.example.connectfit.Utils.group;
import static com.example.connectfit.Utils.isUserLogged;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.databinding.FragmentHomeBinding;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.services.impl.UserServiceImpl;

public class HomeFragment extends Fragment{

    FragmentHomeBinding binding;
    UserServiceImpl userService;
    UserEntity currentUser;
    UserConfigSingleton userConfigSingleton;
    ImageButton trainningButton;
    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServiceImpl();
        userConfigSingleton = UserConfigSingleton.getInstance();
        currentUser = userConfigSingleton.getInstanceOfCurrentUser();
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
        // checks if user is not logged and redirect it to login screen
        if(!isUserLogged(getContext())){
            Navigation.findNavController(view).navigate(R.id.loginScreenFragment);
            createAndShowSnackBar(view, "Você precisa estar logado!", "red");
        }

        trainningButton = binding.imageButtonMyTraining;

        // check user group and switch images if its a professional

        if(group != null && group.equals(UserGroupEnum.STUDENT)){
            trainningButton.setOnClickListener(l -> {
                // navegar para a tela de treinos
                Navigation.findNavController(view).navigate(R.id.myTrainingFragment);
            });
        } else {
            trainningButton.setImageResource(R.drawable.mystudents);
            trainningButton.setOnClickListener(l -> {
                Navigation.findNavController(view).navigate(R.id.myStudents);
            });
        }

        binding.imageButtonSearch.setOnClickListener(l -> {
            // navegar para a tela de busca
            Navigation.findNavController(view).navigate(R.id.searchFragment);
        });

        binding.imageButtonSettings.setOnClickListener(l -> {
           // navegar para a tela de configurações de usuário
            Navigation.findNavController(view).navigate(R.id.userProfileFragment);
        });

        binding.imageButtonLogout.setOnClickListener(l -> {
            // end user session
            userService.signOut(getContext());
            // show snackbar
            createAndShowSnackBar(view, "sessão encerrada, até mais!", "green");
            // navigate to login screen
            Navigation.findNavController(view).navigate(R.id.loginScreenFragment);
        });


    }
}