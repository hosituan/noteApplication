package com.example.noteapplication.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.noteapplication.R;
import com.example.noteapplication.bean.User;
import com.example.noteapplication.database.MyDatabaseHelper;

public class EditProfileFragment extends Fragment {
    MyDatabaseHelper db;
    private static final String PREFS_NAME = "USER_INFO" ;
    public EditProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.db = new MyDatabaseHelper(getActivity());
        View fragmentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setupView(fragmentView);
        return fragmentView;
    }
    public  void setupView(View view) {
        EditText currentName = view.findViewById(R.id.editTextCurrentName);
        EditText name = view.findViewById(R.id.editTextName);
        currentName.setEnabled(false);
        String emailStr = getInfo();
        User user = db.getUser(emailStr);
        currentName.setText(user.getName());
        Button confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getContext())
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle("Are you sure?")
                        .setMessage("We will change your name?")
                        .addButton("OK, I understand!", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            user.setName(name.getText().toString());
                            dialog.dismiss();
                            if(db.changeName(user)) {
                                Toast.makeText(getContext(), "Changed name!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addButton("CANCEL", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.show();
            }
        });

    }

    public String getInfo() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        return  email;
    }

}