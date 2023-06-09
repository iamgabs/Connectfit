package com.example.connectfit;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.connectfit.adapters.ProfessionalAdapter;
import com.example.connectfit.databinding.FragmentSearchBinding;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    FragmentSearchBinding binding;
    UserRepository userRepository;
    List<UserEntity> resultState;

    public SearchFragment() {super(R.layout.fragment_search);}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        resultState = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText search = binding.editTextSearch;
        ListView results = binding.resultsListView;

        // get ALL users if user group is Personal or Nutritionist
        List<UserEntity> dataList = new ArrayList<UserEntity>();
        ProfessionalAdapter adapter = new ProfessionalAdapter(getContext(), dataList, getViewLifecycleOwner());
        results.setAdapter(adapter);

        userRepository.getAllProfessionals().observe(getViewLifecycleOwner(), professionals -> {
            if (professionals != null) {
                dataList.addAll(professionals);
                resultState.addAll(professionals);
                adapter.notifyDataSetChanged();
            } else {
                createAndShowSnackBar(view, "não foi possível obter os profissionais!", "red");
            }
        });



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(dataList.size() == 0) {
                    createAndShowSnackBar(view, "Nenhum usuário econtrado", "red");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                binding.searchButton.setOnClickListener(l -> {
                    if(editable.toString().isEmpty()) {
                        adapter.clear();
                        if(dataList.size() == 0) {
                            dataList.addAll(resultState);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    if(dataList.size() == 0) {
                        createAndShowSnackBar(view, "Nenhum profissional encontrado!", "red");
                    } else {
                        String searchText = editable.toString().toLowerCase();

                        for(UserEntity u : dataList) {
                            System.out.println(">>>>>>>>>>"+u.getSpecialization());
                        }

                        List<UserEntity> filteredList = new ArrayList<>();
                        for (UserEntity item : dataList) {
                            if ((item.getName() != null && item.getName().toLowerCase().contains(searchText)) ||
                                    (item.getSpecialization() != null && item.getSpecialization().contains(searchText))) {
                                filteredList.add(item);
                            }
                        }

                        adapter.clear();
                        adapter.addAll(filteredList);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        });


    }
}