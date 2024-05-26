package com.example.firsttry;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TournamentViewHolder extends RecyclerView.ViewHolder{
    TextView recTitle, recLocation, recHour, recDesc, recDate;
    CardView recCard;

    public TournamentViewHolder(@NonNull View itemView) {
        super(itemView);

        recTitle = itemView.findViewById(R.id.TourName);
        recLocation = itemView.findViewById(R.id.TourLocation);
        recDesc = itemView.findViewById(R.id.TourDesc);
        recHour = itemView.findViewById(R.id.TourHour);
        recCard = itemView.findViewById(R.id.TourCard);
        recDate = itemView.findViewById(R.id.TourDate);
    }
}
