package com.example.noteapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.noteapplication.activity.LoginActivity;
import com.example.noteapplication.activity.RegisterActivity;
import com.example.noteapplication.bean.Status;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.example.noteapplication.fragment.CategoryFragment;
import com.example.noteapplication.fragment.ChangePasswordFragment;
import com.example.noteapplication.fragment.EditProfileFragment;
import com.example.noteapplication.fragment.HomeFragment;
import com.example.noteapplication.fragment.NoteFragment;
import com.example.noteapplication.fragment.PriorityFragment;
import com.example.noteapplication.fragment.StatusFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);

    AnyChartView anyChartView;

    //Change Fragment
    private static final int fragment_home2 = 1;
    private static final int fragment_category = 2;
    private static final int fragment_priority = 3;
    private static final int fragment_status = 4;
    private static final int fragment_note = 5;
    private static final int fragment_editprofile = 6;
    private static final int fragment_changepassword = 7;

    private int currentFragment = fragment_home2;
    //Change Fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_category, R.id.nav_priority, R.id.nav_status, R.id.nav_note, R.id.nav_editprofile, R.id.nav_changepassword)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setNavigationViewListener();

        //Change Fragment
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        replaceFragment(new HomeFragment());
        //Change Fragment



        anyChartView = findViewById(R.id.any_chart_view);

        try {
            setupPieChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setupPieChart() throws ParseException {
//        int d = db.countDone();
//        int p = db.countProcessing();
//        int pe = db.countPending();
        Pie pie = AnyChart.pie();
        List<Status> statuses = db.getAllStatus();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i=0; i < statuses.size(); i++) {
            String title = statuses.get(i).getTitle();
            int count = db.countStatus(title);
            dataEntries.add(new ValueDataEntry(title, count));
        }
//        dataEntries.add(new ValueDataEntry("Done", d));
//        dataEntries.add(new ValueDataEntry("Processing", p));
//        dataEntries.add(new ValueDataEntry("Pending", pe));

        pie.data(dataEntries);
        pie.title("Dashboard");
        anyChartView.setChart(pie);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        /*switch (item.getItemId()) {
            case R.id.nav_home: {
                Intent intent = getIntent();
                finish();
                try {
                    setupPieChart();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                break;
            }
            case R.id.nav_category: {
                Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                MainActivity.this.startActivity(categoryIntent);
                break;
            }
            case R.id.nav_priority: {
                Intent priorityIntent = new Intent(MainActivity.this, PriorityActivity.class);
                MainActivity.this.startActivity(priorityIntent);
                break;
            }
            case R.id.nav_status: {
                Intent  statusIntent = new Intent(MainActivity.this, StatusActivity.class);
                MainActivity.this.startActivity(statusIntent);
                break;
            }
            case R.id.nav_note: {
                Intent noteIntent = new Intent(MainActivity.this, NoteActivity.class);
                MainActivity.this.startActivity(noteIntent);
                break;
            }
        }
        return true;*/

        //Change Fragment
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (fragment_home2 != currentFragment) {
                replaceFragment(new HomeFragment());
                currentFragment = fragment_home2;
            }
        } else if (id == R.id.nav_category) {
            if (fragment_category != currentFragment) {
                replaceFragment(new CategoryFragment());
                currentFragment = fragment_category;
            }
        } else if (id == R.id.nav_priority) {
            if (fragment_priority != currentFragment) {
                replaceFragment(new PriorityFragment());
                currentFragment = fragment_priority;
            }
        } else if (id == R.id.nav_status) {
            if (fragment_status != currentFragment) {
                replaceFragment(new StatusFragment());
                currentFragment = fragment_status;
            }
        } else if (id == R.id.nav_note) {
            if (fragment_note != currentFragment) {
                replaceFragment(new NoteFragment());
                currentFragment = fragment_note;
            }
        } else if (id == R.id.nav_editprofile) {
            if (fragment_editprofile != currentFragment) {
                replaceFragment(new EditProfileFragment());
                currentFragment = fragment_editprofile;
            }
        } else if (id == R.id.nav_changepassword) {
            if (fragment_changepassword != currentFragment) {
                replaceFragment(new ChangePasswordFragment());
                currentFragment = fragment_changepassword;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        //Change Fragment
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Change Fragment
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment);
        fragmentTransaction.commit();
    }
    //Change Fragment
}