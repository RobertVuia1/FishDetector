package com.example.firsttry;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Reports extends AppCompatActivity {

    private TextView totalCatchesYearTextView;
    private TextView totalCatchesMonthTextView;
    private TextView speciesCatchesYearTextView;
    private TextView speciesCatchesMonthTextView;
    private TextView percentageYearTextView;
    private TextView percentageMonthTextView;

    private TextView biggestCatch, longestcatch;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        totalCatchesYearTextView = findViewById(R.id.totalCatchesYear);
        totalCatchesMonthTextView = findViewById(R.id.totalCatchesMonth);
        speciesCatchesYearTextView = findViewById(R.id.speciesCatchesYear);
        speciesCatchesMonthTextView = findViewById(R.id.speciesCatchesMonth);
        percentageYearTextView = findViewById(R.id.percentageYear);
        percentageMonthTextView = findViewById(R.id.percentageMonth);
        biggestCatch = findViewById(R.id.biggestCatch);
        longestcatch = findViewById(R.id.longestCatch);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String currentUserEmail = user.getEmail();
            fetchCatchesData(currentUserEmail);
        } else {
            Log.e(TAG, "User is null.");
            return;
        }

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Reports.this, MainActivity.class));
                finish();
            }
        });
    }

    private void fetchCatchesData(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Catches");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

                int totalCatchesYear = 0;
                int totalCatchesMonth = 0;
                int otherUsersCatchesYear = 0;
                int otherUsersCatchesMonth = 0;
                int otherUsersCountYear = 0;
                int otherUsersCountMonth = 0;

                Map<String, Integer> speciesCountYear = new HashMap<>();
                Map<String, Integer> speciesCountMonth = new HashMap<>();

                double maxWeight = 0;
                double maxLength = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String entryEmail = snapshot.child("email").getValue(String.class);
                    if (entryEmail != null) {
                        String dateStr = getStringValue(snapshot.child("date"));
                        String species = getStringValue(snapshot.child("specie"));
                        String weight = getStringValue(snapshot.child("greutate"));
                        String length = getStringValue(snapshot.child("lungime"));

                        if (dateStr != null && species != null && weight != null && length != null) {
                            try {
                                Calendar catchDate = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                catchDate.setTime(sdf.parse(dateStr));

                                int catchYear = catchDate.get(Calendar.YEAR);
                                int catchMonth = catchDate.get(Calendar.MONTH) + 1;

                                if (entryEmail.equals(email)) {
                                    if (catchYear == currentYear) {
                                        totalCatchesYear++;
                                        speciesCountYear.put(species, speciesCountYear.getOrDefault(species, 0) + 1);

                                        if (catchMonth == currentMonth) {
                                            totalCatchesMonth++;
                                            speciesCountMonth.put(species, speciesCountMonth.getOrDefault(species, 0) + 1);
                                        }

                                        if (Double.parseDouble(weight) > maxWeight) {
                                            maxWeight = Double.parseDouble(weight);
                                        }

                                        if (Double.parseDouble(length) > maxLength) {
                                            maxLength = Double.parseDouble(length);
                                        }
                                    }
                                } else {
                                    if (catchYear == currentYear) {
                                        otherUsersCatchesYear++;
                                        otherUsersCountYear++;

                                        if (catchMonth == currentMonth) {
                                            otherUsersCatchesMonth++;
                                            otherUsersCountMonth++;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                displayResults(totalCatchesYear, totalCatchesMonth, speciesCountYear, speciesCountMonth, otherUsersCatchesYear, otherUsersCountYear, otherUsersCatchesMonth, otherUsersCountMonth, maxWeight, maxLength);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private String getStringValue(DataSnapshot snapshot) {
        Object value = snapshot.getValue();
        return value != null ? value.toString() : null;
    }

    private void displayResults(int totalCatchesYear, int totalCatchesMonth, Map<String, Integer> speciesCountYear, Map<String, Integer> speciesCountMonth,
                                int otherUsersCatchesYear, int otherUsersCountYear, int otherUsersCatchesMonth, int otherUsersCountMonth, double maxWeight, double maxLength) {
        totalCatchesYearTextView.setText("Capturile totale in anul curent: " + totalCatchesYear);
        totalCatchesMonthTextView.setText("Capturile totale in luna curenta: " + totalCatchesMonth);

        StringBuilder speciesYearBuilder = new StringBuilder("Speciile capturate in acest an::\n");
        for (Map.Entry<String, Integer> entry : speciesCountYear.entrySet()) {
            speciesYearBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        speciesCatchesYearTextView.setText(speciesYearBuilder.toString());

        StringBuilder speciesMonthBuilder = new StringBuilder("Speciile capturate in aceasta luna\n");
        for (Map.Entry<String, Integer> entry : speciesCountMonth.entrySet()) {
            speciesMonthBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        speciesCatchesMonthTextView.setText(speciesMonthBuilder.toString());

        double avgOtherUsersCatchesYear = otherUsersCountYear > 0 ? (double) otherUsersCatchesYear / otherUsersCountYear : 0;
        double avgOtherUsersCatchesMonth = otherUsersCountMonth > 0 ? (double) otherUsersCatchesMonth / otherUsersCountMonth : 0;

        double percentageYear = avgOtherUsersCatchesYear > 0 ? (totalCatchesYear / avgOtherUsersCatchesYear) * 100 : 0;
        double percentageMonth = avgOtherUsersCatchesMonth > 0 ? (totalCatchesMonth / avgOtherUsersCatchesMonth) * 100 : 0;

        percentageYearTextView.setText(String.format("In acest an, ai capturat cu %.2f%% mai multi pesti comparativ cu restul utilizatorilor", percentageYear));
        percentageMonthTextView.setText(String.format("In aceasta luna, ai capturat cu %.2f%% mai multi pesti comparativ cu restul utilizatorilor", percentageMonth));

        biggestCatch.setText(String.format("Cel mai mare peste: %.2f kg", maxWeight));
        longestcatch.setText(String.format("Cel mai lung peste: %.2f cm", maxLength));
    }
}
