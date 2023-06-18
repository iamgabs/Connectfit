package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowNotificationWithVibration;
import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.connectfit.databinding.FragmentLoginScreenBinding;
import com.example.connectfit.enums.UserGroupEnum;

import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.example.connectfit.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class LoginScreenFragment extends Fragment {

    FragmentLoginScreenBinding binding;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    UserRepository userRepository;
    UserEntity userLogged;

    public LoginScreenFragment() {
        super(R.layout.fragment_login_screen);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    /**
     * @method onViewCreated is responsible to call an instance of userservice which
     * call firebase to login application
     @variable buttonChangeToSigninScreen has an action to go to SigninScreenFragment
      * */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtGoogleButton = (TextView) binding.loginGoogle.getChildAt(0);
        txtGoogleButton.setText(R.string.login_gogle);

        /***
         * @variable buttonLogin gets the click in login button
         */

        View buttonLogin = binding.loginButton;
        buttonLogin.setOnClickListener(view1 -> {
            // get data from frontend and sign-in with firebase (email/password auth)
            String email = String.valueOf(binding.email.getText());
            String password = String.valueOf(binding.password.getText());
            if(email.isEmpty() || password.isEmpty()){
                createAndShowSnackBar(view, "Por favor, preencha todos os campos!", "red");
            } else {
                try {
                    userRepository.getUser(email, password, getContext()).observe(getViewLifecycleOwner(), getUserResult -> {
                        if (getUserResult.getException() != null) {
                            Exception exception = getUserResult.getException();
                            createAndShowSnackBar(view, exception.getMessage(), "red");
                        } else {
                             List<UserEntity> userList = getUserResult.getUserList();
                             userLogged = userList.get(0);
                             Utils.setUserLoggedLiveData(userLogged);
                             Bundle bundle = new Bundle();
                             bundle.putParcelable("user_logged", (Parcelable) userLogged);
                             getParentFragmentManager().setFragmentResult("userBundle", bundle);
                             createAndShowSnackBar(view, "logado com sucesso!", "green");

                            if(userLogged.getUserGroupEnum() == UserGroupEnum.STUDENT) {
                                if(userLogged.getNotifications() > 0) {
                                    createAndShowNotificationWithVibration(getContext(), view, "Você tem um novo treino, confira sua lista de treinos!");
                                    // clearNotifications
                                    userRepository.clearNotifications(userLogged);
                                }
                                Navigation.findNavController(view).navigate(R.id.studentHomeFragment);
                            } else {
                                if(userLogged.getNotifications() > 0) {
                                    createAndShowNotificationWithVibration(getContext(), view, "Você tem um novo inscrito, acesse a sua lista de estudantes para criar seu treino!");
                                    // clearNotifications
                                    userRepository.clearNotifications(userLogged);
                                }
                                Navigation.findNavController(view).navigate(R.id.homeFragment);
                            }

                        }
                    });

                } catch (Exception exception) {
                    createAndShowSnackBar(view, "não foi possível logar!", "red");
                }
            }
        });

        // action to SigninScreenFragment
        View buttonChangeToSigninScreen = binding.changeScreenFromLoginToSignin;
        buttonChangeToSigninScreen.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginScreenFragment_to_signinScreenFragment));


    }
}