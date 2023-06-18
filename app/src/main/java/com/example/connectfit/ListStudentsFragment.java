package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.connectfit.adapters.StudentsAdapter;
import com.example.connectfit.databinding.FragmentListStudentsBinding;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.example.connectfit.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class ListStudentsFragment extends Fragment {

    UserRepository userRepository;
    FragmentListStudentsBinding binding;
    UserEntity currentUser;

    public ListStudentsFragment() {
        super(R.layout.fragment_list_students);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        currentUser = new UserEntity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListStudentsBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.getUserLogged().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                currentUser = userEntity;

                ListView results = binding.resultsListView;

                // get ALL users who are subscribers for students "x"
                List<UserEntity> dataList = new ArrayList<UserEntity>();
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                StudentsAdapter adapter = new StudentsAdapter(getContext(), dataList, fragmentManager, getViewLifecycleOwner());
                results.setAdapter(adapter);

                userRepository.getMyStudents(currentUser).observe(getViewLifecycleOwner(), students -> {
                    if (students != null) {
                        dataList.addAll(students);
                        adapter.notifyDataSetChanged();
                        if(dataList.size() == 0) {
                            createAndShowSnackBar(view, "você ainda não possui inscritos!", "red");
                        }
                    } else {
                        createAndShowSnackBar(view, "Não foi possível acessar seus inscritos", "red");
                    }
                });

            }
        });
    }
}