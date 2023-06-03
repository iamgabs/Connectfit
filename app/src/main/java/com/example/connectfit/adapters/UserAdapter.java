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

import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.models.entities.UserEntity;
import com.example.connectfit.services.impl.UserServiceImpl;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserEntity> {
    private LayoutInflater inflater;
    private UserServiceImpl userService;

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
//            if(user.getSpecialization() != null) {
//                bodyTextView.setText(user.getSpecialization());
//            }
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
                    builder.setMessage("Deseja se increver com este profissional?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserServiceImpl userService = new UserServiceImpl();
                            userService.doSubscribe(userLogged, user);
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

        return convertView;
    }
}

