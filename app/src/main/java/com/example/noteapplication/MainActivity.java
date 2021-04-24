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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setNavigationViewListener();



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
        switch (item.getItemId()) {
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
        return true;
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
}