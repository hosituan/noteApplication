package com.example.noteapplication.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.noteapplication.R;
import com.example.noteapplication.bean.Category;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryFragment extends Fragment {
    MyDatabaseHelper db;
    public CategoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.db = new MyDatabaseHelper(getActivity());

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_category, container, false);

        FloatingActionButton fab = fragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategory(new Category(), fragmentView, true);
            }
        });
        LoadList(fragmentView);
        return fragmentView;
    }

    void showAddCategory(Category category, View fragmentView, boolean isAdd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Category Form");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(category.getTitle());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                category.setTitle(text);
                if (isAdd) {
                    db.addCategory(category);
                }
                else {
                    db.updateCategory(category);
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
        List<Category> categories = new ArrayList<Category>();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        try {
            categories = db.getAllCategory();
            for (Category v : categories) {
                mainTitle.add(v.getTitle());
                subTitle.add(v.getDate().toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] tempArrayMain = new String[mainTitle.size()];
        String[] tempArraySub = new String[subTitle.size()];

        CommonListAdapter adapter = new CommonListAdapter(getActivity(), mainTitle.toArray(tempArrayMain), subTitle.toArray(tempArraySub));
        ListView list = (ListView)view.findViewById(R.id.category_list);
        list.setLongClickable(true);
        list.setAdapter(adapter);
        List<Category> finalCategories = categories;
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Category category = finalCategories.get(pos);
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getContext())
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("")
                        .setMessage("")
                        .addButton("Edit", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                            showAddCategory(category, view, false);
                        })
                        .addButton("Delete", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                            if(db.deleteCategory(category)) {
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                            }
                            LoadList(view);
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
