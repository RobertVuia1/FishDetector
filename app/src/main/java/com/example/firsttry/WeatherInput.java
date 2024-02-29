package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WeatherInput extends AppCompatActivity {

    EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_input);

        Button back = findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity or show a dialog for Card 1
                startActivity(new Intent(WeatherInput.this, MainActivity.class));
                finish();
            }
        });



        Button afiseaza = findViewById(R.id.buttonAfiseaza);
        afiseaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEdit   = findViewById(R.id.input);
                String data = mEdit.getText().toString();

                Intent i = new Intent(WeatherInput.this, Weather.class);
                i.putExtra("City", data);
                startActivity(i);
            }
        });
    }
}