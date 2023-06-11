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

import java.util.HashMap;
import java.util.List;

public class ListTrainingAdapter extends ArrayAdapter<Trainning> {
    private Context context;
    private List<Trainning> trainningList;
    private TrainingAdapterListener listener;

    public ListTrainingAdapter(Context context, List<Trainning> trainningList) {
        super(context, 0);
        this.context = context;
        this.trainningList = trainningList;
    }

    @Override
    public int getCount() {
        return trainningList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_training_layout, parent, false);
        }
        Trainning trainningPosition = trainningList.get(position);

        TextView textViewTrainingName = view.findViewById(R.id.textViewTrainingName);
        TextView textViewTrainingDescription = view.findViewById(R.id.textViewTrainingDescription);
        TextView textViewTrainingAmount = view.findViewById(R.id.textViewTrainingAmount);
        TextView textViewTrainingLink = view.findViewById(R.id.textViewTrainingLink);

        textViewTrainingName.setText(trainningPosition.getTrainningName());
        textViewTrainingDescription.setText(trainningPosition.getDescription());
        textViewTrainingAmount.setText(String.valueOf(trainningPosition.getTrainningAmount()));
        textViewTrainingLink.setText(trainningPosition.getLink());

        return view;
    }
}
