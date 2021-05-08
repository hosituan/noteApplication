package com.example.noteapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.noteapplication.MainActivity;
import com.example.noteapplication.bean.User;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapplication.R;

public class LoginActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "USER_INFO" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getInfo()) {
            Intent dashboardIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(dashboardIntent);
        }
        else {
            setUpView();
        }

    }

    public void setUpView() {
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
                if (!isValidEmail(email.getText().toString())) {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(LoginActivity.this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Warning")
                            .setMessage("Invalid email!")
                            .addButton("OK, I understand, let me check.", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                dialog.dismiss();
                            });
                    builder.show();
                }
                if (db.checkUser(user)) {
                    CheckBox remember = findViewById(R.id.checkbox_id_remember);
                    saveInfo(user, remember.isChecked());
                    Intent dashboardIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(dashboardIntent);
                }
                else {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(LoginActivity.this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Warning")
                            .setMessage("Wrong email or password!")
                            .addButton("OK, I understand, let me check.", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                dialog.dismiss();
                            });
                    builder.show();
                }
            }
        });

        Button exit = findViewById(R.id.exitButton_id);
        exit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });
    }

    public void saveInfo(User user, boolean remember) {
        SharedPreferences.Editor editor = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("email", user.getEmail());
        editor.putBoolean("isRemember", remember);
        editor.apply();
    }

    public boolean getInfo() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isRemember = sharedPref.getBoolean("isRemember", false);
        return  isRemember;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}