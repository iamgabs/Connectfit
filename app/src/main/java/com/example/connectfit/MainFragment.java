package com.example.connectfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.connectfit.databinding.FragmentMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainFragment extends Fragment {

    // timer to wait (in milliseconds) before going to the next fragment
    private static final int SPLASH_TIMER = 5000;
    FragmentMainBinding binding;

    public MainFragment() {
        super(R.layout.fragment_main);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    /**
     * @method onViewCreated should be responsible
     * to start splash and call next screen
     * after 5000 milliseconds
     * */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigation.findNavController(view).navigate(R.id.action_fragment_splash_to_loginScreenFragment);
            }
        }, SPLASH_TIMER);
    }
}