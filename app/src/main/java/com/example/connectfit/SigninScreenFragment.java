package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.connectfit.databinding.FragmentSigninScreenBinding;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.exceptions.SigninErrorException;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SigninScreenFragment extends Fragment {
    String userId;
    UserGroupEnum userGroup = UserGroupEnum.STUDENT;
    FragmentSigninScreenBinding binding;

    UserRepository userRepository;

    private FirebaseAuth mAuth;
    public SigninScreenFragment() {super(R.layout.fragment_signin_screen);}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        userRepository = new UserRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSigninScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    /**
     @variable buttonChangeToSigninScreen has an action to go to SigninScreenFragment
      * */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // TODO pegar os dados do frontend e cadastrar user no firebase (auth padrão)
        final RadioGroup radioGroup = binding.radioGroup;
        final String[] response = {""};
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                response[0] = button.getText().toString().toLowerCase();
            }
        });


        View buttonSignin = binding.signinButton;
        buttonSignin.setOnClickListener(view1 -> {
            String name = String.valueOf(binding.signinUserName.getText());
            String email = String.valueOf(binding.signinUserEmail.getText());
            String password = String.valueOf(binding.signinUserPassword.getText());
            if(areValidFields(name, email, password)) {
                    if (validResponseOfUserGroup(response[0])) {
                        UserEntity user = new UserEntity();
                        user.setName(name);
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setUserGroupEnum(userGroup);

                        // deve tentar cadastrar usuário caso ainda não seja cadastrado!
                        try {
                            userRepository.saveUser(user);
                            binding.signinUserName.setText("");
                            binding.signinUserEmail.setText("");
                            binding.signinUserPassword.setText("");
                            createAndShowSnackBar(view, "usuário criado com sucesso!", "green");
                            if(userGroup.equals(UserGroupEnum.STUDENT))  {
                                Navigation.findNavController(view).navigate(R.id.studentHomeFragment);
                            } else {
                                Navigation.findNavController(view).navigate(R.id.homeFragment);
                            }
                        } catch (SigninErrorException signinErrorException) {
                            createAndShowSnackBar(view, signinErrorException.getMessage(), "red");
                        }
                    } else {
                        createAndShowSnackBar(view, "Você deve escolher um grupo de usuário!", "red");
                    }
            } else {
                createAndShowSnackBar(view, "Por favor, verifique os campos", "red");
            }
        });

        // action to SigninScreenFragment
        View buttonChangeToLoginScreen = binding.changeScreenFromSigninToLogin;
        buttonChangeToLoginScreen.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signinScreenFragment_to_loginScreenFragment, null));
    }

    public boolean areValidFields(String name, String email, String password) {
        if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        };
        return true;
    }

    public boolean validResponseOfUserGroup(String response) {
        if(!response.isEmpty()){
            if(response.equalsIgnoreCase("sou aluno")){
                this.userGroup = UserGroupEnum.STUDENT;
            } else if(response.equalsIgnoreCase("sou personal")) {
                this.userGroup = UserGroupEnum.PERSONAL_TRAINER;
            } else if(response.equalsIgnoreCase("sou nutricionista")) {
                this.userGroup = UserGroupEnum.NUTRITIONIST;
            }
            return true;
        }
        return false;
    }


}