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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.example.connectfit.R;
import com.example.connectfit.models.entities.TrainningEntity;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.utils.Utils;

import java.util.List;

public class StudentsAdapter  extends ArrayAdapter<UserEntity> {
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;
    private UserEntity userLogged;
    private LifecycleOwner lifecycleOwner;

    public StudentsAdapter(Context context, List<UserEntity> users, FragmentManager fragmentManager, LifecycleOwner lifecycleOwner) {
        super(context, 0, users);
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        this.userLogged = null;
        this.lifecycleOwner = lifecycleOwner;
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
                StudentsAdapter.this.userLogged = userEntity;


                TextView titleTextView = convertView1.findViewById(android.R.id.text1);
                TextView bodyTextView = convertView1.findViewById(android.R.id.text2);
                UserEntity user = getItem(position);

                if (user != null) {
                    titleTextView.setText(user.getName());
                }

                // add click event
                View finalConvertView = convertView1;
                final int itemPosition = position;
                convertView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserEntity clickedUser = getItem(itemPosition);
                        if (clickedUser != null && userLogged != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getContext());
                            builder.setTitle("Confirmação");
                            builder.setMessage("Deseja acessar o treino deste inscrito?");
                            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // set student clicked
                                    Utils.setStudentClicked(user);
                                    if(user.getTrainingList() != null && !user.getTrainingList().isEmpty()){
                                        TrainningEntity hasTraining = null;
                                        for(TrainningEntity t: user.getTrainingList()) {
                                            if(t.getProfessional().equalsIgnoreCase(userLogged.getId())) {
                                                Utils.setTrainningEntity(t);
                                                hasTraining = t;
                                            }
                                        }
                                        if(hasTraining != null) {
                                            Navigation.findNavController(finalConvertView).navigate(R.id.myTrainingFragment);
                                        } else {
                                            createAndShowSnackBar(finalConvertView, "Você ainda não possui treinos com este/a aluno(a)!", "red");
                                            Navigation.findNavController(finalConvertView).navigate(R.id.createTrainingFragment);
                                        }
                                    } else {
                                        createAndShowSnackBar(finalConvertView, "Você ainda não possui treinos com este/a aluno(a)!", "red");
                                        Navigation.findNavController(finalConvertView).navigate(R.id.createTrainingFragment);
                                    }

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