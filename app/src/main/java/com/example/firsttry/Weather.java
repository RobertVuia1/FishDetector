package com.example.firsttry;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Weather extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV, wind, precip, humidity;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<WeatherRVModel> weatherRVModelArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;

    private String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_weather);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        weatherRV = findViewById(R.id.idRvWeather);
        wind = findViewById(R.id.idTVWind);
        precip = findViewById(R.id.idTVPrecipitation);
        humidity = findViewById(R.id.idTVHumidity);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        weatherRVModelArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModelArrayList);
        weatherRV.setAdapter(weatherRVAdapter);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Weather.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }


        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        getWeatherInfo(getCityName(10,20));

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEdt.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(Weather.this, "Va rog introduceti numele orasului", Toast.LENGTH_SHORT).show();
                } else{
                    cityNameTV.setText(cityName);
                    getWeatherInfo(city);
                }
            }
        });

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity or show a dialog for Card 1
                startActivity(new Intent(Weather.this, MainActivity.class));
                finish();
            }
        });

        Button more = findViewById(R.id.moreDaysButton);
        more.setOnClickListener(v -> {
            // Start a new activity or show a dialog for Card 1
            String data = cityEdt.getText().toString();

            Intent i = new Intent(Weather.this, Weather7Days.class);
            i.putExtra("City", data);
            startActivity(i);
            finish();
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permisiunile au fost acordate", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Va rog acceptati permisiunile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Weather.this, MainActivity.class));
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude){
        String cityName = "Not found";

        String longi = String.valueOf(longitude);
        String lat = String.valueOf(latitude);


        Log.i("date", longi);
        Log.i("date", lat);

        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address adr : addresses){
                if (adr != null){
                    String city = adr.getLocality();
                    if (city != null && !city.equals("")){
                        cityName = city;
                    }else {
                        cityName = "Timisoara";
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return cityName;
    }

    public static String extractFileName(String url) {

        int lastSlashIndex = url.lastIndexOf('/');


        String fileNameWithExtension = url.substring(lastSlashIndex + 1);


        int dotIndex = fileNameWithExtension.lastIndexOf('.');


        String fileName = fileNameWithExtension.substring(0, dotIndex);


        String timeOfDay = url.contains("/day/") ? "day_" : "night_";

        // Concatenate the time of day with the filename
        return timeOfDay + fileName;
    }

    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=81a1100b61934d85b9694322241503&q=" + cityName + "&days=7&aqi=yes&alerts=yes";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(Weather.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                weatherRVModelArrayList.clear();
                try {
                    String windS = response.getJSONObject("current").getString("wind_kph");
                    wind.setText("Vant: " + windS + "km/h");
                    String precipitations = response.getJSONObject("current").getString("precip_mm");
                    precip.setText("Precipitatii: " + precipitations);
                    String hum = response.getJSONObject("current").getString("humidity");
                    humidity.setText("Umiditatea: " + hum + "%");
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTV.setText(temperature + "°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    String fileName = extractFileName(conditionIcon);


                    String imagePath = fileName;

                    int resID = getResources().getIdentifier(imagePath, "drawable", getPackageName());

                    if (resID != 0) {
                        iconIV.setImageResource(resID);
                    } else {
                        iconIV.setImageResource(R.drawable.day_113);
                    }


                    conditionTV.setText(condition);
                    if (isDay==1){

                        backIV.setImageResource(R.drawable.day2);
                    }else{

                        backIV.setImageResource(R.drawable.night);
                    }

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecast0.getJSONArray("hour");

                    for (int i = 0; i < hourArray.length(); i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherRVModelArrayList.add(new WeatherRVModel(time, temper, img, wind));
                    }

                    weatherRVAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Weather.this, "Incercati sa introduceti un alt oras", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}