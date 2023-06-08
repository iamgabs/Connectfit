package com.example.connectfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.connectfit.adapters.ProfessionalAdapter;
import com.example.connectfit.databinding.FragmentMyStudentsBinding;
import com.example.connectfit.models.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;


public class MyStudents extends Fragment {

    FragmentMyStudentsBinding binding;

    public MyStudents() {
        super(R.layout.fragment_my_students);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentMyStudentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}