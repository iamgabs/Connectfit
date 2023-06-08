package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;
import static com.example.connectfit.utils.Utils.group;
import static com.example.connectfit.utils.Utils.isUserLogged;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.databinding.FragmentHomeBinding;
import com.example.connectfit.databinding.FragmentStudentHomeBinding;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import javax.inject.Inject;

public class StudentHomeFragment extends Fragment{

    FragmentStudentHomeBinding binding;
    UserRepository userRepository;
    UserEntity currentUser;
    UserConfigSingleton userConfigSingleton;
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

        getParentFragmentManager().setFragmentResultListener("userBundle", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                currentUser = bundle.getParcelable("user_logged");
            }
        });

        userRepository = new UserRepository();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainningButton = binding.imageButtonMyTraining;
        userConfigSingleton = UserConfigSingleton.getInstance();
        currentUser = userConfigSingleton.getInstanceOfCurrentUser();

        trainningButton.setOnClickListener(l -> {
            // navegar para a tela de treinos
            Navigation.findNavController(view).navigate(R.id.myTrainingFragment);
        });

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
            userRepository.signout(getContext());
            // show snackbar
            createAndShowSnackBar(view, "sessão encerrada, até mais!", "green");
            // navigate to login screen
            Navigation.findNavController(view).navigate(R.id.loginScreenFragment);
        });


    }
}