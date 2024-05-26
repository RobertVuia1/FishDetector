package com.example.firsttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddTournaments extends AppCompatActivity {

    EditText title, location, description, date, hour;
    Button btnAdd;

    FirebaseUser user;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tournaments);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddTournaments.this, Tournaments.class));
                finish();
            }
        });

        title = findViewById(R.id.tournamentName);
        location = findViewById(R.id.locatieTurneu);
        date = findViewById(R.id.tournamentDate);
        description = findViewById(R.id.editTextDescription);
        hour = findViewById(R.id.locatieHour);
        btnAdd = findViewById(R.id.buttonSave);

        auth = FirebaseAuth.getInstance();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData() {
        Map<String, Object> map = new HashMap<>();
        user = auth.getCurrentUser();
        map.put("title", title.getText().toString());
        map.put("location", location.getText().toString());
        map.put("description", description.getText().toString());
        map.put("hour", hour.getText().toString());
        map.put("date", date.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Tournaments").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddTournaments.this, "Turneul a fost adăugat cu succes", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTournaments.this, "Turneul nu a putut fi adăugat. Vă rugăm încercați mai târziu", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                });
    }

    private void clearFields() {
        title.setText("");
        location.setText("");
        description.setText("");
    }
}