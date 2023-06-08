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
        // TODO PEGAR LISTA DE ALUNOS (MESMA DE PROFISISONAIS)
        // TODO action AO CLICAR EM UM ALUNO, DEVE APARECER UM MESSAGE BOX
        // TODO DE CONFIRMAÇÃO SOBRE CRIAR UM NOVO TREINO
        // TODO CASO A RESPOSTA SEJA SIM, REDIRECIONA PARA A TELA DE CRIAÇÃO DE TREINOS PARA O ALUNO "X"

        ListView results = binding.resultsListView;

        // get ALL users who are subscribers for professional "x"
        List<UserEntity> dataList = new ArrayList<UserEntity>();
        ProfessionalAdapter adapter = new ProfessionalAdapter(getContext(), dataList);
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