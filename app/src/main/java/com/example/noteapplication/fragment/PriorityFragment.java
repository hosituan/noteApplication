package com.example.noteapplication.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.noteapplication.R;
import com.example.noteapplication.bean.Category;
import com.example.noteapplication.bean.Priority;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PriorityFragment extends Fragment {
    MyDatabaseHelper db;
    public PriorityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.db = new MyDatabaseHelper(getActivity());
        View fragmentView = inflater.inflate(R.layout.fragment_priority, container, false);
        FloatingActionButton fab = fragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddPriority(new Priority(), fragmentView, true);
            }
        });
        LoadList(fragmentView);
        return fragmentView;
    }

    void showAddPriority(Priority priority, View fragmentView, boolean isAdd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Category Form");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(priority.getTitle());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                priority.setTitle(text);
                if (isAdd) {
                    db.addPriority(priority);
                }
                else {
                    db.updatePriority(priority);
                }
                LoadList(fragmentView);
                Toast.makeText(getContext(), "Done!", Toast.LENGTH_LONG).show();
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

    void LoadList(View view) {
        List<String> mainTitle = new ArrayList<String>();
        List<String> subTitle = new ArrayList<String>();
        List<Priority> categories = new ArrayList<>();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        try {
            categories = db.getAllPriority();
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

        CommonListAdapter adapter = new CommonListAdapter(getActivity(), mainTitle.toArray(tempArrayMain), subTitle.toArray(tempArraySub));
        ListView list = (ListView)view.findViewById(R.id.priority_list);
        list.setAdapter(adapter);
        list.setLongClickable(true);
        List<Priority> finalCategories = categories;
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Priority category = finalCategories.get(pos);
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getContext())
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("")
                        .setMessage("")
                        .addButton("Edit", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                            showAddPriority(category, view, false);
                        })
                        .addButton("Delete", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                            if(db.deletePriority(category)) {
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
}