package com.example.connectfit.adapters;

import static com.example.connectfit.utils.Utils.createAndShowSnackBar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.UserRepository;
import com.example.connectfit.utils.Utils;

import java.util.List;

public class ProfessionalAdapter extends ArrayAdapter<UserEntity> {
    private LayoutInflater inflater;
    UserRepository userRepository;

    private UserEntity userLogged;
    private LifecycleOwner lifecycleOwner;

    public ProfessionalAdapter(Context context, List<UserEntity> users, LifecycleOwner lifecycleOwner) {
        super(context, 0, users);
        inflater = LayoutInflater.from(context);
        this.lifecycleOwner = lifecycleOwner;
        this.userLogged = new UserEntity();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        final View convertView1 = convertView;

        Utils.getUserLogged().observe(lifecycleOwner, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                ProfessionalAdapter.this.userLogged = userEntity;

                TextView titleTextView = convertView1.findViewById(android.R.id.text1);
                TextView bodyTextView = convertView1.findViewById(android.R.id.text2);
                UserEntity user = getItem(position);
                userRepository = new UserRepository();

                if (user != null) {
                    titleTextView.setText(user.getName());
                }

                // add click event
                final int itemPosition = position;
                View finalConvertView = convertView1;
                convertView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserEntity clickedUser = getItem(itemPosition);
                        if (clickedUser != null) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getContext());
                            builder.setTitle("Confirmação");
                            builder.setMessage("Deseja se increver com este profissional?");
                            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userRepository.subscribeWithAProfessional(clickedUser, userLogged);
                                    createAndShowSnackBar(finalConvertView, "inscrito com sucesso!", "green");
                                }
                            });
                            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // it will do nothing
                                }
                            });
                            builder.create();
                            builder.show();
                        }
                    }
                });
            }
        });

        return convertView1;
    }
}

