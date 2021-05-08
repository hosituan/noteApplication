package com.example.noteapplication.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.noteapplication.R;
import com.example.noteapplication.bean.Category;
import com.example.noteapplication.bean.Note;
import com.example.noteapplication.bean.Priority;
import com.example.noteapplication.bean.Status;
import com.example.noteapplication.bean.User;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {
    MyDatabaseHelper db;
    private static final String PREFS_NAME = "USER_INFO" ;
    public NoteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.db = new MyDatabaseHelper(getActivity());
        View fragmentView = inflater.inflate(R.layout.fragment_note, container, false);



        FloatingActionButton fab = fragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNote(new Note(), fragmentView, true);

            }
        });
        LoadList(fragmentView);
        return fragmentView;
    }

    void showAddNote(Note note, View fragmentView, boolean isAdd) {
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
        for (Category v1 : categories) {
            spinnerArray_cate.add(v1.getTitle());
        }
        for (Priority v1 : priorities) {
            spinnerArray_pri.add(v1.getTitle());
        }
        for (Status v : statuses) {
            spinnerArray_stt.add(v.getTitle());
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_note, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        Spinner category = dialogView.findViewById(R.id.spinner_category);
        ArrayAdapter<String> spinnerArrayAdapter_category = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray_cate);
        category.setAdapter(spinnerArrayAdapter_category);

        Spinner priority = dialogView.findViewById(R.id.spinner_priority);
        ArrayAdapter<String> spinnerArrayAdapter_priority = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray_pri);
        priority.setAdapter(spinnerArrayAdapter_priority);

        Spinner status = dialogView.findViewById(R.id.spinner_status);
        ArrayAdapter<String> spinnerArrayAdapter_status = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray_stt);
        status.setAdapter(spinnerArrayAdapter_status);

        EditText editText = (EditText) dialogView.findViewById(R.id.title_id);
        editText.setText(note.getNoteTitle());
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) dialogView.findViewById(R.id.title_id);
                String title = editText.getText().toString();
                String cate = category.getSelectedItem().toString();
                String pri = priority.getSelectedItem().toString();
                String sta = status.getSelectedItem().toString();
                String email = getInfo();
                note.setNoteTitle(title);
                note.setCategory(cate);
                note.setPriority(pri);
                note.setStatus(sta);
                note.setEmail(email);
                if (isAdd) {
                    db.addNote(note);
                }
                else {
                    db.updateNote(note);
                }

                LoadList(fragmentView);
                Toast.makeText(getContext(), "Done!", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }

    void LoadList(View view) {
        List<String> title = new ArrayList<String>();
        List<String> category = new ArrayList<String>();
        List<String> priority = new ArrayList<String>();
        List<String> status = new ArrayList<String>();
        List<String> date = new ArrayList<String>();
        List<Note> notes = new ArrayList<>();

        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        try {
            String email = getInfo();
            notes = db.getAllNotes(new User(email));
            for (Note v : notes) {
                title.add(v.getNoteTitle());
                category.add(v.getCategory());
                priority.add(v.getPriority());
                status.add(v.getStatus());
                date.add(v.getDate().toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] tempArrayTitle = new String[title.size()];
        String[] tempArrayCategory = new String[title.size()];
        String[] tempArrayPriority= new String[title.size()];
        String[] tempArrayStatus = new String[title.size()];
        String[] tempArrayDate = new String[title.size()];


        NoteListAdapter adapter = new NoteListAdapter(getActivity(), title.toArray(tempArrayTitle), category.toArray(tempArrayCategory), priority.toArray(tempArrayPriority), status.toArray(tempArrayStatus), date.toArray(tempArrayDate));
        ListView list = (ListView)view.findViewById(R.id.note_list);
        list.setLongClickable(true);
        list.setAdapter(adapter);
        List<Note> finalNotes = notes;
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Note note = finalNotes.get(pos);
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getContext())
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("")
                        .setMessage("")
                        .addButton("Edit", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                            showAddNote(note, view, false);
                        })
                        .addButton("Delete", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                            if(db.deleteNote(note)) {
                                LoadList(view);
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addButton("CANCEL", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.show();
                return true;
            }
        });
    }

    public String getInfo() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        return  email;
    }
}