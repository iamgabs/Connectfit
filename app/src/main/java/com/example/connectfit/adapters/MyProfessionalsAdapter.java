package com.example.connectfit.adapters;

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
import androidx.navigation.Navigation;

import com.example.connectfit.R;
import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.models.entities.TrainningEntity;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.repositories.TrainingRepository;
import com.example.connectfit.utils.Utils;

import java.util.List;

public class MyProfessionalsAdapter extends ArrayAdapter<UserEntity> {

    private LayoutInflater inflater;
    private TrainingRepository trainingRepository;
    private LifecycleOwner lifecycleOwner;
    public MyProfessionalsAdapter(@NonNull Context context, List<UserEntity> users, LifecycleOwner lifecycleOwner) {
        super(context, 0, users);
        inflater = LayoutInflater.from(context);
        trainingRepository = new TrainingRepository();
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView titleTextView = convertView.findViewById(android.R.id.text1);
        UserEntity user = getItem(position);

        if (user != null) {
            titleTextView.setText(user.getName());
        }

        // add click event
        final int itemPosition = position;
        View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEntity clickedUser = getItem(itemPosition);
                if (clickedUser != null) {
                    UserEntity userLogged = UserConfigSingleton.getInstance().getInstanceOfCurrentUser();
                    AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getContext());
                    builder.setTitle("Confirmação");
                    builder.setMessage("Deseja acessar o treino deste profissional?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utils.setProfessionalClicked(clickedUser);
                            trainingRepository.getTraininigById(userLogged.getId(), clickedUser.getId()).observe(lifecycleOwner, new Observer<TrainningEntity>() {
                                @Override
                                public void onChanged(TrainningEntity trainningEntity) {
                                    Utils.setTrainningEntity((TrainningEntity) trainningEntity);
                               // navegar para a próxima tela apenas quando o treino for recebido
                                    Navigation.findNavController(finalConvertView).navigate(R.id.myTrainingFragment);
                                }
                            });
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

        return convertView;
    }
}
