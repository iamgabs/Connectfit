package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.connectfit.adapters.ProfessionalAdapter;
import com.example.connectfit.adapters.StudentsAdapter;
import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.databinding.FragmentListStudentsBinding;
import com.example.connectfit.interfaces.StudentsCallback;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class ListStudentsFragment extends Fragment {

    UserRepository userRepository;
    FragmentListStudentsBinding binding;

    public ListStudentsFragment() {
        super(R.layout.fragment_list_students);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListStudentsBinding.inflate(inflater, container, false);

        userRepository = new UserRepository();

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView results = binding.resultsListView;

        // get ALL users who are subscribers for students "x"
        List<UserEntity> dataList = new ArrayList<UserEntity>();
        StudentsAdapter adapter = new StudentsAdapter(getContext(), dataList);
        results.setAdapter(adapter);

        UserEntity currentUser = UserConfigSingleton.getInstance().getInstanceOfCurrentUser();

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
}