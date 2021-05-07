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
import com.example.noteapplication.MainActivity;
import com.example.noteapplication.R;
import com.example.noteapplication.activity.RegisterActivity;
import com.example.noteapplication.bean.User;
import com.example.noteapplication.database.MyDatabaseHelper;

public class ChangePasswordFragment extends Fragment {
    MyDatabaseHelper db;
    private static final String PREFS_NAME = "USER_INFO" ;
    public ChangePasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.db = new MyDatabaseHelper(getActivity());
        View fragmentView = inflater.inflate(R.layout.fragment_change_password, container, false);
        setupView(fragmentView);
        return fragmentView;
    }


    public  void setupView(View view) {
        EditText email = view.findViewById(R.id.editTextEmail);
        email.setEnabled(false);
        String emailStr = getInfo();
        email.setText(emailStr);

        EditText currentPassword = view.findViewById(R.id.editTextCurrentPassword);
        EditText newPassword = view.findViewById(R.id.editTextPassword);
        EditText rePassword = view.findViewById(R.id.editTextRePassword);

        User user = db.getUser(emailStr);

        Button confirmButton = view.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.getPassword().equals(currentPassword.getText().toString())) {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getContext())
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Warning")
                            .setMessage("Please make sure you are filling correct current password!")
                            .addButton("OK, I understand, let me check.", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                dialog.dismiss();
                            });

                    builder.show();
                }
                else if (!newPassword.getText().toString().equals(rePassword.getText().toString())) {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getContext())
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Warning")
                            .setMessage("Password does not match!")
                            .addButton("OK, I understand, let me check.", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                dialog.dismiss();
                            });

                    builder.show();
                }
                else {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getContext())
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Are you sure?")
                            .setMessage("We will change your password?")
                            .addButton("OK, I understand!", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                user.setPassWord(newPassword.getText().toString());
                                dialog.dismiss();
                                if(db.changePassword(user)) {
                                    Toast.makeText(getContext(), "Changed password!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addButton("CANCEL", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                dialog.dismiss();
                            });
                    builder.show();
                }
            }
        });


    }

    public String getInfo() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        return  email;
    }
}