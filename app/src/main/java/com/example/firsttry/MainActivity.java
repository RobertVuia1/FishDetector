package com.example.firsttry;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapter postAdapter;

    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddButton.class));
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menuFish){
                    startActivity(new Intent(MainActivity.this, FishInformation.class));
                    finish();
                } else if (item.getItemId() == R.id.menuLogout){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                } else if (item.getItemId() == R.id.menuProfile){
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                } else if (item.getItemId() == R.id.menuWeather){
                    startActivity(new Intent(MainActivity.this, Weather.class));
                    finish();
                } else if (item.getItemId() == R.id.menuUpload){
                    startActivity(new Intent(MainActivity.this, UploadPhoto.class));
                    finish();
                } else if (item.getItemId() == R.id.menuFavorite){
                    startActivity(new Intent(MainActivity.this, FavoritePlaces.class));
                    finish();
                } else if (item.getItemId() == R.id.menuTournaments){
                    startActivity(new Intent(MainActivity.this, Tournaments.class));
                    finish();
                } else if (item.getItemId() == R.id.menuCapture){
                    startActivity(new Intent(MainActivity.this, AddCatches.class));
                    finish();
                } else if (item.getItemId() == R.id.menuReports){
                    startActivity(new Intent(MainActivity.this, Reports.class));
                    finish();
                }

                // Close the navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<PostModel> options =
                new FirebaseRecyclerOptions.Builder<PostModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Posts"), PostModel.class)
                        .build();

        postAdapter = new PostAdapter(options);
        recyclerView.setAdapter(postAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        postAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }
}

