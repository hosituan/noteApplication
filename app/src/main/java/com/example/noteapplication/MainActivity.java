package com.example.noteapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.noteapplication.bean.Status;
import com.example.noteapplication.bean.User;
import com.example.noteapplication.database.MyDatabaseHelper;
import com.example.noteapplication.fragment.CategoryFragment;
import com.example.noteapplication.fragment.ChangePasswordFragment;
import com.example.noteapplication.fragment.EditProfileFragment;
import com.example.noteapplication.fragment.HomeFragment;
import com.example.noteapplication.fragment.NoteFragment;
import com.example.noteapplication.fragment.PriorityFragment;
import com.example.noteapplication.fragment.StatusFragment;
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
    private static final String PREFS_NAME = "USER_INFO";

    MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);
    //Change Fragment
    private static final int fragment_home = 1;
    private static final int fragment_category = 2;
    private static final int fragment_priority = 3;
    private static final int fragment_status = 4;
    private static final int fragment_note = 5;
    private static final int fragment_editProfile = 6;
    private static final int fragment_changePassword = 7;

    private int currentFragment = fragment_home;
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


        View headerView = navigationView.getHeaderView(0);

        TextView nameLabel = headerView.findViewById(R.id.name);
        TextView emailLabel =  headerView.findViewById(R.id.email);
        String email = getInfo();
        User user = db.getUser(email);
        nameLabel.setText(user.getName());
        emailLabel.setText(email);

        //Change Fragment
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        replaceFragment(new HomeFragment());

    }

    public String getInfo() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        return  email;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //Change Fragment
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (fragment_home != currentFragment) {
                replaceFragment(new HomeFragment());
                getSupportActionBar().setTitle("Home");
                currentFragment = fragment_home;
            }
        } else if (id == R.id.nav_category) {
            if (fragment_category != currentFragment) {
                replaceFragment(new CategoryFragment());
                getSupportActionBar().setTitle("Category");
                currentFragment = fragment_category;
            }
        } else if (id == R.id.nav_priority) {
            if (fragment_priority != currentFragment) {
                replaceFragment(new PriorityFragment());
                getSupportActionBar().setTitle("Priority");
                currentFragment = fragment_priority;
            }
        } else if (id == R.id.nav_status) {
            if (fragment_status != currentFragment) {
                replaceFragment(new StatusFragment());
                getSupportActionBar().setTitle("Status");
                currentFragment = fragment_status;
            }
        } else if (id == R.id.nav_note) {
            if (fragment_note != currentFragment) {
                replaceFragment(new NoteFragment());
                getSupportActionBar().setTitle("Notes");
                currentFragment = fragment_note;
            }
        } else if (id == R.id.nav_editprofile) {
            if (fragment_editProfile != currentFragment) {
                replaceFragment(new EditProfileFragment());
                getSupportActionBar().setTitle("Edit Profile");
                currentFragment = fragment_editProfile;
            }
        } else if (id == R.id.nav_changepassword) {
            if (fragment_changePassword != currentFragment) {
                replaceFragment(new ChangePasswordFragment());
                getSupportActionBar().setTitle("Change password");
                currentFragment = fragment_changePassword;
            }
        } else if (id == R.id.logout) {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(MainActivity.this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("Are you sure?")
                    .setMessage("Logout and clear your local information?")
                    .addButton("YES", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        SharedPreferences settings = (SharedPreferences) getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        settings.edit().clear().commit();
                        dialog.dismiss();
                        finish();
                    })
                    .addButton("CANCEL", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();
                    });

            builder.show();
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
}