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

public class AddFavoritePlaceActivity extends AppCompatActivity {

    EditText title, location, description;
    Button btnAdd;

    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite_place);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddFavoritePlaceActivity.this, FavoritePlaces.class));
                finish();
            }
        });

        title = findViewById(R.id.editTextPlaceName);
        location = findViewById(R.id.editTextLocation);
        description = findViewById(R.id.editTextDescription);
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
        map.put("title", title.getText().toString());
        map.put("location", location.getText().toString());
        map.put("description", description.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Favorites").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddFavoritePlaceActivity.this, "Locația favorită a fost adăugată cu succes", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddFavoritePlaceActivity.this, "Locația favorită nu a putut fi adăugată. Vă rugăm încercați mai târziu", Toast.LENGTH_SHORT).show();
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
