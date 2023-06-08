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
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.interfaces.MyAppComponent;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import javax.inject.Inject;

public class HomeFragment extends Fragment{

    FragmentHomeBinding binding;
    UserRepository userRepository;
    UserEntity currentUser;
    UserConfigSingleton userConfigSingleton;
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

        getParentFragmentManager().setFragmentResultListener("userBundle", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                currentUser = bundle.getParcelable("user_logged");
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainningButton = binding.imageButtonMyTraining;
        userConfigSingleton = UserConfigSingleton.getInstance();
        currentUser = userConfigSingleton.getInstanceOfCurrentUser();

        // check user group and switch images if its a professional
        trainningButton.setOnClickListener(l -> {
            Navigation.findNavController(view).navigate(R.id.myStudents);
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