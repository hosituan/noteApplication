package com.example.noteapplication.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapplication.MainActivity;
import com.example.noteapplication.bean.User;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapplication.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });


        Button signIn = findViewById(R.id.signInButton_id);
        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);
        signIn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                MyDatabaseHelper db = new MyDatabaseHelper(LoginActivity.this);
                User user = new User(email.getText().toString(), password.getText().toString());
                if (db.checkUser(user)) {
                    Intent dashboardIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(dashboardIntent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Wrong email or password!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}