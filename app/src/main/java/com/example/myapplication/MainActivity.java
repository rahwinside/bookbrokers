package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
    }
private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment =null;

                switch (menuItem.getItemId()){

                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_add:
                        selectedFragment = new AddFragment();
                        break;
                    case R.id.navigation_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.navigation_notifications:
                        selectedFragment = new NotificationFragment();
                        break;
                    case R.id.navigation_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;

            }
        };
};

