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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddCatches extends AppCompatActivity {

    EditText specie, lungime, greutate;
    Button btnAdd;

    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catches);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCatches.this, MainActivity.class));
                finish();
            }
        });

        specie = findViewById(R.id.numeSpecie);
        lungime = findViewById(R.id.lungimePeste);
        greutate = findViewById(R.id.greutatePeste);
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
        if (user != null) {
            map.put("email", user.getEmail());
        }
        map.put("specie", specie.getText().toString());
        map.put("greutate", greutate.getText().toString());
        map.put("lungime", lungime.getText().toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        map.put("date", currentDate);

        FirebaseDatabase.getInstance().getReference().child("Catches").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddCatches.this, "Captura a fost adăugată cu succes", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCatches.this, "Captura nu a putut fi adăugată. Vă rugăm încercați mai târziu", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                });
    }

    private void clearFields() {
        specie.setText("");
        greutate.setText("");
        lungime.setText("");
    }
}