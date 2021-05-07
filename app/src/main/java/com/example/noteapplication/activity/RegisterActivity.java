package com.example.noteapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.noteapplication.R;
import com.example.noteapplication.bean.User;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button signIn = findViewById(R.id.signInButton_id);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.super.onBackPressed();
            }
        });

        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);
        EditText rePassword = findViewById(R.id.editTextRePassword);


        Button signUp = findViewById(R.id.signUpButton_id);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidEmail(email.getText().toString())) {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(RegisterActivity.this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Warning")
                            .setMessage("Bad email!")
                            .addButton("OK, I understand, let me check.", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                dialog.dismiss();
                            });
                    builder.show();
                }
                else {
                    if (password.getText().toString().equals(rePassword.getText().toString()) && !password.getText().toString().equals("")) {
                        String pw = password.getText().toString();
                        String rePw = rePassword.getText().toString();
                        MyDatabaseHelper db = new MyDatabaseHelper(RegisterActivity.this);
                        User user = new User(email.getText().toString(), password.getText().toString());
                        db.addUser(user);
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(RegisterActivity.this)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                .setTitle("Done")
                                .setMessage("Hope you will enjoy")
                                .addButton("OK", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                    dialog.dismiss();
                                    RegisterActivity.super.onBackPressed();
                                });
                        builder.show();

                    } else {
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(RegisterActivity.this)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                .setTitle("Warning")
                                .setMessage("Password does not match!")
                                .addButton("OK, I understand, let me check.", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                    dialog.dismiss();
                                });

                        builder.show();
                    }
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}