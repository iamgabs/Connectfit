package com.example.connectfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.connectfit.R;
import com.example.connectfit.interfaces.TrainingAdapterListener;
import com.example.connectfit.models.entities.Trainning;

import java.util.List;

public class TrainningAdapter extends ArrayAdapter<Trainning> {
    private Context context;
    private List<Trainning> trainningList;
    private TrainingAdapterListener listener;

    public TrainningAdapter(Context context, List<Trainning> trainningList, TrainingAdapterListener listener) {
        super(context, 0, trainningList);
        this.context = context;
        this.trainningList = trainningList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        }

        Trainning trainning = trainningList.get(position);

        TextView textViewTrainningName = view.findViewById(R.id.textViewTrainningName);
        textViewTrainningName.setText(trainning.getTrainningName());

        Button editButton = view.findViewById(R.id.buttonEdit);
        editButton.setOnClickListener(v -> {
            // TODO editar o treino com base na posição
            listener.editTraining(position);
        });

        Button deleteButton = view.findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(v -> {
            // TODO excluir o treino com base na posição
            listener.deleteTraining(position);
        });

        return view;
    }
}

