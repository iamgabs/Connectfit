package com.example.connectfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.connectfit.databinding.FragmentLoginScreenBinding;
import com.example.connectfit.databinding.FragmentSigninScreenBinding;

public class SigninScreenFragment extends Fragment {

    FragmentSigninScreenBinding binding;
    public SigninScreenFragment() {super(R.layout.fragment_signin_screen);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSigninScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    /**
     @variable buttonChangeToSigninScreen has an action to go to SigninScreenFragment
      * */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // action to SigninScreenFragment
        View buttonChangeToLoginScreen = binding.changeScreenFromSigninToLogin;
        buttonChangeToLoginScreen.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signinScreenFragment_to_loginScreenFragment, null));
    }
}