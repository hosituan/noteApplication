package com.example.noteapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.noteapplication.bean.Priority;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PriorityActivity extends AppCompatActivity {

    MyDatabaseHelper db = new MyDatabaseHelper(PriorityActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PriorityActivity.this);
                builder.setTitle("Priority Form");


                final EditText input = new EditText(PriorityActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = input.getText().toString();
                        Priority priority = new Priority(text, new Date());
                        db.addPriority(priority);
                        LoadList();
                        Toast.makeText(PriorityActivity.this, "Added!", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        LoadList();
    }

    void LoadList() {
        List<String> mainTitle = new ArrayList<String>();
        List<String> subTitle = new ArrayList<String>();
        MyDatabaseHelper db = new MyDatabaseHelper(PriorityActivity.this);
        try {
            List<Priority> categories = db.getAllPriority();
            categories.forEach((v) ->
                    {
                        mainTitle.add(v.getTitle());
                        subTitle.add(v.getDate().toString());
                    }
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] tempArrayMain = new String[mainTitle.size()];
        String[] tempArraySub = new String[subTitle.size()];

        CommonListAdapter adapter = new CommonListAdapter(this, mainTitle.toArray(tempArrayMain), subTitle.toArray(tempArraySub));
        ListView list = (ListView)findViewById(R.id.category_list);
        list.setAdapter(adapter);
    }
}