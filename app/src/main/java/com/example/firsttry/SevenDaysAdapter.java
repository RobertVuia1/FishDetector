package com.example.firsttry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SevenDaysAdapter extends RecyclerView.Adapter<SevenDaysAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SevenDaysDataClass> sevenDaysDataList;

    public SevenDaysAdapter(Context context, ArrayList<SevenDaysDataClass> sevenDaysDataList) {
        this.context = context;
        this.sevenDaysDataList = sevenDaysDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sevendays_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SevenDaysDataClass data = sevenDaysDataList.get(position);

        holder.dateTextView.setText(data.getDate());
        holder.maxTempTextView.setText("Temperatura maxima: " + data.getMaxTemp());
        holder.minTempTextView.setText("Temperatura minima: " + data.getMinTemp());
        holder.maxWindTextView.setText("Viteza vantului: " + data.getMaxWind() + "km/h");
        holder.avgHumidityTextView.setText("Umiditatea: " + data.getHumidity() + "%");
        holder.dailyChanceOfRainTextView.setText("Sansele de ploaie: " + data.getChance_rain());
        holder.sunriseTextView.setText("Ora rasaritului: " + data.getSunrise());
        holder.sunsetTextView.setText("Ora apusului: " + data.getSunset());
        holder.moonIlluminationTextView.setText("Faza lunii: " + data.getMoon());

        // Set icon using Picasso library
        String iconUrl = data.getIcon();
        String iconName = extractFileName(iconUrl);
        int resID = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        if (resID != 0) {
            holder.iconImageView.setImageResource(resID);
        } else {
            holder.iconImageView.setImageResource(R.drawable.day_116); // Default icon if not found
        }
    }

    @Override
    public int getItemCount() {
        return sevenDaysDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView, maxTempTextView, minTempTextView, maxWindTextView,
                avgHumidityTextView, dailyChanceOfRainTextView, sunriseTextView,
                sunsetTextView, moonIlluminationTextView;
        private ImageView iconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.idTVDate);
            maxTempTextView = itemView.findViewById(R.id.idTvMaxTemp);
            minTempTextView = itemView.findViewById(R.id.idTVMintemp);
            maxWindTextView = itemView.findViewById(R.id.idTVMaxWind);
            avgHumidityTextView = itemView.findViewById(R.id.idTVAvgHumidity);
            dailyChanceOfRainTextView = itemView.findViewById(R.id.idTVDailyChanceOfRain);
            sunriseTextView = itemView.findViewById(R.id.idTVSunrise);
            sunsetTextView = itemView.findViewById(R.id.idTVSunset);
            moonIlluminationTextView = itemView.findViewById(R.id.idTVMoonIllumination);
            iconImageView = itemView.findViewById(R.id.idIVCondition);
        }
    }

    private String extractFileName(String url) {
        if (url == null || url.isEmpty()) {
            return ""; // or handle the case appropriately
        }

        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex == -1 || lastSlashIndex == url.length() - 1) {
            return ""; // or handle the case appropriately
        }

        String fileNameWithExtension = url.substring(lastSlashIndex + 1);
        int dotIndex = fileNameWithExtension.lastIndexOf('.');
        if (dotIndex == -1) {
            return ""; // or handle the case appropriately
        }

        String fileName = fileNameWithExtension.substring(0, dotIndex);
        String timeOfDay = url.contains("/day/") ? "day_" : "night_";
        return timeOfDay + fileName;
    }

}
