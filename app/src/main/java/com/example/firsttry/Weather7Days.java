package com.example.firsttry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Weather7Days extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SevenDaysAdapter adapter;

    private TextView name_city;
    private ArrayList<SevenDaysDataClass> dataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather7_days);

        recyclerView = findViewById(R.id.sevenDaysRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataArrayList = new ArrayList<>();
        adapter = new SevenDaysAdapter(this, dataArrayList);
        recyclerView.setAdapter(adapter);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Weather7Days.this, Weather.class));
                finish();
            }
        });

        // Retrieve city name from intent
        String city = getIntent().getStringExtra("City");
        if (city != null && !city.isEmpty()) {
            getWeatherInfo(city);
        } else {
            getWeatherInfo("Timisoara");
        }
    }

    private void getWeatherInfo(String cityName) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=81a1100b61934d85b9694322241503&q=" + cityName + "&days=7&aqi=yes&alerts=yes";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    name_city = findViewById(R.id.cityNameTextView);
                    name_city.setText("Localitatea: " + cityName);

                    JSONArray forecastDayArray = response.getJSONObject("forecast").getJSONArray("forecastday");

                    for (int i = 0; i < forecastDayArray.length(); i++) {
                        JSONObject forecastDayObj = forecastDayArray.getJSONObject(i);
                        String date = forecastDayObj.getString("date");

                        JSONObject dayObj = forecastDayObj.getJSONObject("day");
                        String maxTemp = dayObj.getString("maxtemp_c");
                        String minTemp = dayObj.getString("mintemp_c");
                        String maxWind = dayObj.getString("maxwind_kph");
                        String humidity = dayObj.getString("avghumidity");
                        String chance_rain = dayObj.getString("daily_chance_of_rain");
                        String sunrise = forecastDayObj.getJSONObject("astro").getString("sunrise");
                        String sunset = forecastDayObj.getJSONObject("astro").getString("sunset");
                        String moon = forecastDayObj.getJSONObject("astro").getString("moon_phase");

                        JSONObject conditionObj = dayObj.getJSONObject("condition");
                        String iconUrl = conditionObj.getString("icon");
                        String iconFileName = extractFileName(iconUrl);

                        dataArrayList.add(new SevenDaysDataClass(date, iconFileName, maxTemp, minTemp, maxWind, humidity, chance_rain, sunrise, sunset, moon));
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Weather7Days.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Weather7Days.this, "Error fetching weather information", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private String extractFileName(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        String fileNameWithExtension = url.substring(lastSlashIndex + 1);
        int dotIndex = fileNameWithExtension.lastIndexOf('.');
        String fileName = fileNameWithExtension.substring(0, dotIndex);
        String timeOfDay = url.contains("/day/") ? "day_" : "night_";
        return timeOfDay + fileName;
    }
}
