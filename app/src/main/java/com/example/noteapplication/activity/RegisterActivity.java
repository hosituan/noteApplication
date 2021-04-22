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
                    Toast.makeText(RegisterActivity.this, "Bad email!", Toast.LENGTH_LONG).show();
                }
                else {
                    if (password.getText().toString().equals(rePassword.getText().toString()) && !password.getText().toString().equals("")) {
                        String pw = password.getText().toString();
                        String rePw = rePassword.getText().toString();
                        MyDatabaseHelper db = new MyDatabaseHelper(RegisterActivity.this);
                        User user = new User(email.getText().toString(), password.getText().toString());
                        db.addUser(user);
                        Toast.makeText(RegisterActivity.this, "Added User", Toast.LENGTH_LONG).show();
                        RegisterActivity.super.onBackPressed();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Password does not match!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}