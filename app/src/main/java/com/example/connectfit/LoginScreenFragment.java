package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowNotificationWithVibration;
import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.databinding.FragmentLoginScreenBinding;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.exceptions.SigninErrorException;
import com.example.connectfit.interfaces.UsersCallback;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.services.impl.UserServiceImpl;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class LoginScreenFragment extends Fragment {

    FragmentLoginScreenBinding binding;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    UserServiceImpl userService;
    UserEntity userLogged;

    public LoginScreenFragment() {
        super(R.layout.fragment_login_screen);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserServiceImpl();
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
                try {
                    userService.getUserByEmailAndPassword(email, password, getContext(), new UsersCallback() {
                        @Override
                        public void onUsersReceived(List<UserEntity> users) {
                            userLogged = users.get(0);
                            UserConfigSingleton.getInstance().setInstanceOfCurrentUser(userLogged);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("user_logged", (Parcelable) userLogged);
                            getParentFragmentManager().setFragmentResult("userBundle", bundle);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            createAndShowSnackBar(view, e.getMessage(), "red");
                        }
                    });
                    createAndShowSnackBar(view, "logado com sucesso!", "green");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(userLogged.getUserGroupEnum() == UserGroupEnum.STUDENT) {
                                if(userLogged.getNotifications() > 0) {
                                    createAndShowNotificationWithVibration(getContext(), view, "Você tem um novo treino, confira sua lista de treinos!");
                                }
                                Navigation.findNavController(view).navigate(R.id.studentHomeFragment);
                            } else {
                                if(userLogged.getNotifications() > 0) {
                                    createAndShowNotificationWithVibration(getContext(), view, "Você tem um novo inscrito, acesse a sua lista de estudantes para criar seu treino!");
                                }
                                Navigation.findNavController(view).navigate(R.id.homeFragment);
                            }
                        }
                    }, 4000);

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