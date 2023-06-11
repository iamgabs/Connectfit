package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.connectfit.adapters.MyProfessionalsAdapter;
import com.example.connectfit.adapters.StudentsAdapter;
import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.databinding.FragmentMyProfessionalsBinding;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class MyProfessionals extends Fragment {

    FragmentMyProfessionalsBinding binding;
    UserRepository userRepository;

    public MyProfessionals() {
        super(R.layout.fragment_my_professionals);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentMyProfessionalsBinding.inflate(inflater, container, false);

        userRepository = new UserRepository();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView results = binding.resultsListView;

        // get ALL users which student "x" is a subscriber
        List<UserEntity> dataList = new ArrayList<UserEntity>();
        MyProfessionalsAdapter adapter = new MyProfessionalsAdapter(getContext(), dataList, this);
        results.setAdapter(adapter);

        UserEntity currentUser = UserConfigSingleton.getInstance().getInstanceOfCurrentUser();

        userRepository.getMyProfessionals(currentUser).observe(getViewLifecycleOwner(), professionals -> {
            if (professionals != null) {
                dataList.addAll(professionals);
                adapter.notifyDataSetChanged();
                if(dataList.size() == 0) {
                    createAndShowSnackBar(view, "você ainda não está inscrito em nenhum profissional!", "red");
                }
            } else {
                createAndShowSnackBar(view, "Não foi possível acessar seus profissionais", "red");
            }
        });
    }
}