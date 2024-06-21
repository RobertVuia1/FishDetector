package com.example.firsttry;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
                    finish();
                } else if (item.getItemId() == R.id.menuLogout){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(FishInformation.this, Login.class));
                    finish();
                } else if (item.getItemId() == R.id.menuProfile){
                    startActivity(new Intent(FishInformation.this, MainActivity.class));
                    finish();
                } else if (item.getItemId() == R.id.menuWeather){
                    startActivity(new Intent(FishInformation.this, Weather.class));
                    finish();
                } else if (item.getItemId() == R.id.menuUpload){
                    startActivity(new Intent(FishInformation.this, UploadPhoto.class));
                    finish();
                } else if (item.getItemId() == R.id.menuFavorite){
                    startActivity(new Intent(FishInformation.this, FavoritePlaces.class));
                    finish();
                } else if (item.getItemId() == R.id.menuTournaments){
                    startActivity(new Intent(FishInformation.this, Tournaments.class));
                    finish();
                } else if (item.getItemId() == R.id.menuCapture){
                    startActivity(new Intent(FishInformation.this, AddCatches.class));
                    finish();
                } else if (item.getItemId() == R.id.menuReports){
                    startActivity(new Intent(FishInformation.this, Reports.class));
                    finish();
                }

                // Close the navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        GridLayout gridLayout = findViewById(R.id.gridLayout);


        CardView cardAmur = findViewById(R.id.cardAmur);
        CardView cardCaras = findViewById(R.id.cardCaras);
        CardView cardCrap = findViewById(R.id.cardCrap);
        CardView cardBiban = findViewById(R.id.cardBiban);
        CardView cardSomn = findViewById(R.id.cardSomn);
        CardView cardStiuca = findViewById(R.id.cardStiuca);
        cardAmur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FishInformation.this, Amur.class));
            }
        });
        cardBiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FishInformation.this, Biban.class));
            }
        });
        cardCaras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FishInformation.this, Caras.class));
            }
        });
        cardCrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FishInformation.this, Crap.class));
            }
        });
        cardStiuca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FishInformation.this, Stiuca.class));
            }
        });
        cardSomn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FishInformation.this, Somn.class));
            }
        });
    }


}