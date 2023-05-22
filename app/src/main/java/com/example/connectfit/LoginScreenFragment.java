package com.example.connectfit;

import static com.example.connectfit.Utils.createAndShowSnackBar;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.connectfit.databinding.FragmentLoginScreenBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginScreenFragment extends Fragment {

    FragmentLoginScreenBinding binding;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

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

        View buttonLogin = binding.loginButton;
        buttonLogin.setOnClickListener(view1 -> {
            // TODO pegar os dados do frontend e cadastrar no firebase (auth padrão)
            String email = String.valueOf(binding.email.getText());
            String password = String.valueOf(binding.password.getText());
            if(email.isEmpty() || password.isEmpty()){
                createAndShowSnackBar(view, "Por favor, preencha todos os campos!", "red");
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener( authentication -> {
                    if(authentication.isSuccessful()){
                        createAndShowSnackBar(view, "logado com sucesso!", "green");
                        Navigation.findNavController(view).navigate(R.id.homeFragment);
                    } else {
                        createAndShowSnackBar(view, "Email ou senha incorretos!", "red");
                    }
                });
            }
        });












        // action to SigninScreenFragment
        View buttonChangeToSigninScreen = binding.changeScreenFromLoginToSignin;
        buttonChangeToSigninScreen.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginScreenFragment_to_signinScreenFragment));


    }
}