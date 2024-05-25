package com.example.firsttry;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {

    TextView recTitle, recLocation, recDesc;
    CardView recCard;

    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);

        recTitle = itemView.findViewById(R.id.FavTitle);
        recLocation = itemView.findViewById(R.id.FavLocation);
        recDesc = itemView.findViewById(R.id.FavDetails);
        recCard = itemView.findViewById(R.id.FavCard);
    }
}
