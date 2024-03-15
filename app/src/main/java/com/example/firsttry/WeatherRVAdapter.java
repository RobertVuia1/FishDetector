package com.example.firsttry;


import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRVModel> weatherRVModelArrayList;

    public static String extractFileName(String url) {
        // Find the last occurrence of '/' in the URL
        int lastSlashIndex = url.lastIndexOf('/');

        // Extract the substring after the last '/'
        String fileNameWithExtension = url.substring(lastSlashIndex + 1);

        // Find the position of the file extension separator '.'
        int dotIndex = fileNameWithExtension.lastIndexOf('.');

        // Extract the substring before the file extension
        String fileName = fileNameWithExtension.substring(0, dotIndex);

        // Determine whether it's "day" or "night" based on the URL
        String timeOfDay = url.contains("/day/") ? "day_" : "night_";

        // Concatenate the time of day with the filename
        return timeOfDay + fileName;
    }

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModel> weatherRVModelArrayList) {
        this.context = context;
        this.weatherRVModelArrayList = weatherRVModelArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        WeatherRVModel model = weatherRVModelArrayList.get(position);
        holder.temperatureTV.setText(model.getTemperature() + "°C");
        holder.windTV.setText(model.getWindSpeed()+"Km/h");

        String fileName = extractFileName(model.getIcon());


        String imagePath = fileName;

        int resID = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());

        if (resID != 0) {
            holder.conditionIV.setImageResource(resID);
        } else {
            holder.conditionIV.setImageResource(R.drawable.day_113);
        }

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try{
            Date t = input.parse(model.getTime());
            holder.timeTV.setText(output.format(t));
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRVModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView windTV, temperatureTV, timeTV;
        private ImageView conditionIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTV = itemView.findViewById(R.id.idTVWindSpeed);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            timeTV = itemView.findViewById(R.id.idTVTime);
            conditionIV = itemView.findViewById(R.id.idIVCondition);
        }
    }


}


