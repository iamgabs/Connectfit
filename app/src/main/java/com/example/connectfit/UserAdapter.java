package com.example.connectfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.connectfit.models.entities.UserEntity;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserEntity> {
    private LayoutInflater inflater;

    public UserAdapter(Context context, List<UserEntity> users) {
        super(context, 0, users);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView titleTextView = convertView.findViewById(android.R.id.text1);
        TextView bodyTextView = convertView.findViewById(android.R.id.text2);
        UserEntity user = getItem(position);

        if (user != null) {
            titleTextView.setText(user.getName());
//            bodyTextView.setText(user.getSpecialization());
        }

        return convertView;
    }
}

