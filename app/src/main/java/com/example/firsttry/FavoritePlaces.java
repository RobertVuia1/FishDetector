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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class FavoritePlaces extends AppCompatActivity {

    private static final String TAG = "FavoritePlaces";

    RecyclerView recyclerView;
    List<FavoriteDataClass> dataList;
    FavoriteAdapter adapter;
    FloatingActionButton addFavorite;

    FirebaseAuth auth;
    DatabaseReference favoritesRef;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);

        recyclerView = findViewById(R.id.FavoriteRecycle);
        addFavorite = findViewById(R.id.floatingActionButton);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
            String encodedEmail = userEmail;
            favoritesRef = FirebaseDatabase.getInstance().getReference("Favorites");
        } else {
            Log.e(TAG, "User is null.");
            return;
        }

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritePlaces.this, MainActivity.class));
                finish();
            }
        });

        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritePlaces.this, AddFavoritePlaceActivity.class));
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(FavoritePlaces.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();

        fetchFavoritePlaces();
    }

    private void fetchFavoritePlaces() {
        favoritesRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot favoriteSnapshot : dataSnapshot.getChildren()) {
                    // Get the favorite place details
                    String title = favoriteSnapshot.child("title").getValue(String.class);
                    String location = favoriteSnapshot.child("location").getValue(String.class);
                    String description = favoriteSnapshot.child("description").getValue(String.class);
                    FavoriteDataClass favoriteData = new FavoriteDataClass(title, location, description);
                    dataList.add(favoriteData);
                }
                Log.d(TAG, "Number of favorite places fetched: " + dataList.size());
                // Initialize or update the adapter
                if (adapter == null) {
                    adapter = new FavoriteAdapter(FavoritePlaces.this, dataList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching favorite places: " + databaseError.getMessage());
                Toast.makeText(FavoritePlaces.this, "Failed to fetch favorite places", Toast.LENGTH_SHORT).show();
            }
        });
    }





}
