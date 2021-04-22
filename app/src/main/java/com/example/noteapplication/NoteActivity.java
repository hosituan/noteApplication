package com.example.noteapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.noteapplication.bean.Category;
import com.example.noteapplication.bean.Note;
import com.example.noteapplication.bean.Priority;
import com.example.noteapplication.bean.Status;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    MyDatabaseHelper db = new MyDatabaseHelper(NoteActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        ArrayList<String> spinnerArray_cate = new ArrayList<String>();
        ArrayList<String> spinnerArray_pri = new ArrayList<String>();
        ArrayList<String> spinnerArray_stt = new ArrayList<String>();


        List<Category> categories = null;
        List<Priority> priorities = null;
        List<Status> statuses = null;

        try {
            categories = db.getAllCategory();

            priorities = db.getAllPriority();
            statuses = db.getAllStatus();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        categories.forEach((v) ->
                    {
                        spinnerArray_cate.add(v.getTitle());
                    }
            );
        priorities.forEach((v) ->
                {
                    spinnerArray_pri.add(v.getTitle());
                }
        );
        statuses.forEach((v) ->
                {
                    spinnerArray_stt.add(v.getTitle());
                }
        );




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NoteActivity.this);

                LayoutInflater inflater = (LayoutInflater)NoteActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alert_not, null);
                dialogBuilder.setView(dialogView);
                AlertDialog alertDialog = dialogBuilder.create();

                Spinner category = dialogView.findViewById(R.id.spinner_category);
                ArrayAdapter<String> spinnerArrayAdapter_category = new ArrayAdapter<String>
                        (NoteActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray_cate);
                category.setAdapter(spinnerArrayAdapter_category);

                Spinner priority = dialogView.findViewById(R.id.spinner_priority);
                ArrayAdapter<String> spinnerArrayAdapter_priority = new ArrayAdapter<String>
                        (NoteActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray_pri);
                priority.setAdapter(spinnerArrayAdapter_priority);

                Spinner status = dialogView.findViewById(R.id.spinner_status);
                ArrayAdapter<String> spinnerArrayAdapter_status = new ArrayAdapter<String>
                        (NoteActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray_stt);
                status.setAdapter(spinnerArrayAdapter_status);






                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) dialogView.findViewById(R.id.title_id);
                        String title = editText.getText().toString();
                        String cate = category.getSelectedItem().toString();
                        String pri = priority.getSelectedItem().toString();
                        String sta = status.getSelectedItem().toString();

                        Note note = new Note(title, cate, pri, sta, new Date());
                        db.addNote(note);
                        LoadList();
                        Toast.makeText(NoteActivity.this, "Added!", Toast.LENGTH_LONG).show();
                    }
                });

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();


            }
        });
        LoadList();

    }

    void LoadList() {
        List<String> title = new ArrayList<String>();
        List<String> category = new ArrayList<String>();
        List<String> priority = new ArrayList<String>();
        List<String> status = new ArrayList<String>();
        List<String> date = new ArrayList<String>();


        MyDatabaseHelper db = new MyDatabaseHelper(NoteActivity.this);
        try {

            List<Note>  notes = db.getAllNotes();
            notes.forEach((v) ->
                    {
                        title.add(v.getNoteTitle());
                        category.add(v.getCategory());
                        priority.add(v.getPriority());
                        status.add(v.getStatus());
                        date.add(v.getDate().toString());
                    }
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] tempArrayTitle = new String[title.size()];
        String[] tempArrayCategory = new String[title.size()];
        String[] tempArrayPriority= new String[title.size()];
        String[] tempArrayStatus = new String[title.size()];
        String[] tempArrayDate = new String[title.size()];


        NoteListAdapter adapter = new NoteListAdapter(this, title.toArray(tempArrayTitle), category.toArray(tempArrayCategory), priority.toArray(tempArrayPriority), status.toArray(tempArrayStatus), date.toArray(tempArrayDate));
        ListView list = (ListView)findViewById(R.id.note_list);
        list.setAdapter(adapter);
    }
}