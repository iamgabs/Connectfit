package com.example.connectfit;

import android.annotation.SuppressLint;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class LoginScreenFragment extends Fragment {


    FragmentLoginScreenBinding binding;
    GoogleSignInClient googleSignInClient;

    public LoginScreenFragment() {
        super(R.layout.fragment_login_screen);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    /**
     @variable buttonChangeToSigninScreen has an action to go to SigninScreenFragment
      * */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtGoogleButton = (TextView) binding.loginGoogle.getChildAt(0);
        txtGoogleButton.setText(R.string.login_gogle);

        // action to SigninScreenFragment
        View buttonChangeToSigninScreen = binding.changeScreenFromLoginToSignin;
        buttonChangeToSigninScreen.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginScreenFragment_to_signinScreenFragment, null));
    }
}