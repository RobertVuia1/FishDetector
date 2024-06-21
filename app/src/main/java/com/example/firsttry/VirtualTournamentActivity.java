package com.example.firsttry;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VirtualTournamentActivity extends AppCompatActivity {

    private TextView emailTop1, emailTop2, emailTop3, singleText;
    private EditText inputSpecie, inputGreutate, inputLungime;
    private Button addCaptureButton;
    private FirebaseAuth auth;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_tournament);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VirtualTournamentActivity.this, Tournaments.class));
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            currentUserEmail = user.getEmail();
        } else {
            Log.e(TAG, "User is null.");
            return;
        }

        emailTop1 = findViewById(R.id.emailTop1);
        emailTop2 = findViewById(R.id.emailTop2);
        emailTop3 = findViewById(R.id.emailTop3);
        singleText = findViewById(R.id.singleText);

        inputSpecie = findViewById(R.id.input1);
        inputGreutate = findViewById(R.id.input2);
        inputLungime = findViewById(R.id.input3);
        addCaptureButton = findViewById(R.id.addCaptureButton);

        addCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCatch();
            }
        });

        fetchTopUsers();
    }

    private void addNewCatch() {
        String specie = inputSpecie.getText().toString().trim();
        String greutate = inputGreutate.getText().toString().trim();
        String lungime = inputLungime.getText().toString().trim();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (specie.isEmpty() || greutate.isEmpty() || lungime.isEmpty()) {
            Toast.makeText(this, "Va rugam completati toate campurile", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference virtualTournamentRef = FirebaseDatabase.getInstance().getReference("VirtualTournament");
        DatabaseReference catchesRef = FirebaseDatabase.getInstance().getReference("Catches");
        String entryId = virtualTournamentRef.push().getKey(); // Generate a unique key for each entry

        Map<String, String> newCatch = new HashMap<>();
        Map<String, String> newCatch2 = new HashMap<>();
        newCatch.put("email", currentUserEmail);
        newCatch.put("specie", specie);
        newCatch.put("greutate", greutate);
        newCatch.put("lungime", lungime);
        newCatch.put("data", date);

        newCatch2.put("email", currentUserEmail);
        newCatch2.put("specie", specie);
        newCatch2.put("greutate", greutate);
        newCatch2.put("lungime", lungime);
        newCatch2.put("date", date);

        if (entryId != null) {
            // Save to VirtualTournament
            virtualTournamentRef.child(entryId).setValue(newCatch)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(VirtualTournamentActivity.this, "Inregistrarea a fost adaugata cu succes.", Toast.LENGTH_SHORT).show();
                        inputSpecie.setText("");
                        inputGreutate.setText("");
                        inputLungime.setText("");
                        fetchTopUsers();
                    })
                    .addOnFailureListener(e -> Toast.makeText(VirtualTournamentActivity.this, "Inregistrarea nu a putut fii realizata. Va rugam incercati mai tarziu. Eroare: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            catchesRef.child(entryId).setValue(newCatch2)
                    .addOnFailureListener(e -> Toast.makeText(VirtualTournamentActivity.this, "Inregistrarea nu a putut fii realizata in categoria Capturi. Eroare: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void fetchTopUsers() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("VirtualTournament");

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> userCatchCount = new HashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String email = snapshot.child("email").getValue(String.class);
                    String date = snapshot.child("data").getValue(String.class);
                    if (email != null && date != null && date.equals(currentDate)) {
                        userCatchCount.put(email, userCatchCount.getOrDefault(email, 0) + 1);
                    }
                }

                updateUI(userCatchCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void updateUI(Map<String, Integer> userCatchCount) {
        String[] topUsers = { "N/A", "N/A", "N/A" };
        int[] topCounts = { 0, 0, 0 };
        int currentUserCount = 0;

        for (Map.Entry<String, Integer> entry : userCatchCount.entrySet()) {
            String email = entry.getKey();
            int count = entry.getValue();

            for (int i = 0; i < 3; i++) {
                if (count > topCounts[i]) {
                    for (int j = 2; j > i; j--) {
                        topCounts[j] = topCounts[j - 1];
                        topUsers[j] = topUsers[j - 1];
                    }
                    topCounts[i] = count;
                    topUsers[i] = email;
                    break;
                }
            }

            if (email.equals(currentUserEmail)) {
                currentUserCount = count;
            }
        }

        emailTop1.setText(topUsers[0] + ", care are " + topCounts[0] + " capturi astazi.");
        emailTop2.setText(topUsers[1] + ", care are " + topCounts[1] + " capturi azi.");
        emailTop3.setText(topUsers[2] + ", which has " + topCounts[2] + " capturi azi.");
        singleText.setText("Dumneavoastra aveti " + currentUserCount + " capturi azi.");
    }
}
