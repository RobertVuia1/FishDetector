package com.example.firsttry;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder>{

    private Context context;
    private List<TournamentDataClass> dataList;

    public TournamentAdapter(Context context, List<TournamentDataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public TournamentAdapter.TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_recycler_item, parent, false);
        return new TournamentAdapter.TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentAdapter.TournamentViewHolder holder, int position) {
        holder.recTitle.setText(dataList.get(position).getTitle());
        holder.recLocation.setText(dataList.get(position).getLocation());
        holder.recHour.setText(dataList.get(position).getHour());
        holder.recDesc.setText(dataList.get(position).getDescription());


        Random rand = new Random();
        int randomNumber = rand.nextInt(4) + 1;
        int imageResource;
        switch (randomNumber) {
            case 1:
                imageResource = R.drawable.concurs1;
                break;
            case 2:
                imageResource = R.drawable.concurs2;
                break;
            case 3:
                imageResource = R.drawable.concurs3;
                break;
            case 4:
                imageResource = R.drawable.concurs4;
                break;
            default:

                imageResource = R.drawable.concurs1;
                break;
        }
        holder.recImg.setImageResource(imageResource);

    }



    public int getItemCount() {
        return dataList.size();
    }

    static class TournamentViewHolder extends RecyclerView.ViewHolder {
        TextView recTitle, recLocation, recDesc, recHour, recDate;
        ImageView recImg;
        CardView recCard;

        TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            recTitle = itemView.findViewById(R.id.TourName);
            recLocation = itemView.findViewById(R.id.TourLocation);
            recDesc = itemView.findViewById(R.id.TourDesc);
            recImg = itemView.findViewById(R.id.TourImg);
            recCard = itemView.findViewById(R.id.TourCard);
            recHour = itemView.findViewById(R.id.TourHour);
            recDate = itemView.findViewById(R.id.TourDate);
        }
    }
}
