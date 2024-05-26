package com.example.firsttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tournaments extends AppCompatActivity {

    private static final String TAG = "FavoritePlaces";

    RecyclerView recyclerView;
    List<TournamentDataClass> dataList;
    TournamentAdapter adapter;
    FloatingActionButton addFavorite;

    FirebaseAuth auth;
    DatabaseReference favoritesRef;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments);

        recyclerView = findViewById(R.id.TournamentRecycle);
        addFavorite = findViewById(R.id.TournamentfloatingActionButton);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
            favoritesRef = FirebaseDatabase.getInstance().getReference("Tournaments");
        } else {
            Log.e(TAG, "User is null.");
            return;
        }

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tournaments.this, MainActivity.class));
                finish();
            }
        });

        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tournaments.this, AddTournaments.class));
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Tournaments.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();

        fetchTournaments();
    }

    private void fetchTournaments() {
        favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot favoriteSnapshot : dataSnapshot.getChildren()) {
                    String title = favoriteSnapshot.child("title").getValue(String.class);
                    String location = favoriteSnapshot.child("location").getValue(String.class);
                    String description = favoriteSnapshot.child("description").getValue(String.class);
                    String date = favoriteSnapshot.child("date").getValue(String.class);
                    String hour = favoriteSnapshot.child("hour").getValue(String.class);
                    TournamentDataClass tournamentData = new TournamentDataClass(title, location, date, hour, description);
                    dataList.add(tournamentData);
                }
                Log.d(TAG, "Number of tournaments places fetched: " + dataList.size());
                if (adapter == null) {
                    adapter = new TournamentAdapter(Tournaments.this, dataList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching favorite places: " + databaseError.getMessage());
                Toast.makeText(Tournaments.this, "Failed to fetch favorite places", Toast.LENGTH_SHORT).show();
            }
        });
    }
}