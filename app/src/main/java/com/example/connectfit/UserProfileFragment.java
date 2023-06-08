package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.databinding.FragmentUserProfileBinding;
import com.example.connectfit.exceptions.SearchErrorException;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import javax.inject.Inject;

public class UserProfileFragment extends Fragment {

    FragmentUserProfileBinding binding;
    UserEntity user;

    UserRepository userRepository;

    public UserProfileFragment() { super(R.layout.fragment_user_profile); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        user = UserConfigSingleton.getInstance().getInstanceOfCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentUserProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        /* checks whether the user has "specializations" field different of null
        * add all specializations into text view*/
        final String[] specialitionsString = {""};
        try{
            specialitionsString[0] = user.getSpecialization();
        } catch (NullPointerException nullPointerException) {
        }

        if(!specialitionsString[0].isEmpty()) {
            binding.textView.setText(specialitionsString[0]);
            binding.editTextSpecializations.setText(specialitionsString[0]);
        }

        // concat specializations and save in firebase
        binding.button.setOnClickListener(L -> {
            String editTextStringResource = String.valueOf(binding.editTextSpecializations.getText());
            editTextStringResource = editTextStringResource.replace(" ", ",");
            if(!editTextStringResource.isEmpty()) {
                try {
                    userRepository.addSpecializations(editTextStringResource, user);
                    binding.editTextSpecializations.setText("");
                    createAndShowSnackBar(view, "adicionado com sucesso!","green");
                    binding.textView.setText(editTextStringResource);
                } catch (SearchErrorException searchErrorException) {
                    createAndShowSnackBar(view, searchErrorException.getMessage(), "red");
                }
            } else {
                createAndShowSnackBar(view, "informe as especialidades!", "red");
            }
        });

    }
}