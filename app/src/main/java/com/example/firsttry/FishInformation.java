package com.example.firsttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class FishInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_information);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menuFish){
                    startActivity(new Intent(FishInformation.this, FishInformation.class));
                } else if (item.getItemId() == R.id.menuLogout){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(FishInformation.this, Login.class));
                    finish();
                } else if (item.getItemId() == R.id.menuProfile){
                    startActivity(new Intent(FishInformation.this, MainActivity.class));
                    finish();
                } else if (item.getItemId() == R.id.menuWeather){
                    startActivity(new Intent(FishInformation.this, WeatherInput.class));
                    finish();
                }

                // Close the navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Set OnClickListener for each CardView
        CardView cardAmur = findViewById(R.id.cardAmur);
        cardAmur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity or show a dialog for Card 1
                startActivity(new Intent(FishInformation.this, Amur.class));
            }
        });
    }


}