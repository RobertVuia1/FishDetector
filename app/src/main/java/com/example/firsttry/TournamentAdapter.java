package com.example.firsttry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
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
        holder.recLocation.setText("Locatia: " + dataList.get(position).getLocation());
        holder.recHour.setText("Ora: " + dataList.get(position).getHour());
        holder.recDesc.setText("Descriere: " + dataList.get(position).getDescription());
        holder.recDate.setText("Data: " + dataList.get(position).getDate());


        Random rand = new Random();
        int randomNumber = rand.nextInt(4) + 1;
        int imageResource;
        if (Objects.equals(dataList.get(position).getLocation(), "Online")){

            imageResource = R.drawable.turneuvirtual;

            if (!isSunday()){
                holder.recDate.setTextColor(Color.RED);
                holder.recCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = holder.itemView.getContext();
                        new AlertDialog.Builder(context)
                                .setTitle("Concursul Virtual")
                                .setMessage("Concursul virtual este deschis doar duminica. Va rugam incercati atunci!")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                });
            } else{
                holder.recDate.setTextColor(Color.GREEN);
                holder.recCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = holder.itemView.getContext();
                        context.startActivity(new Intent(context, VirtualTournamentActivity.class));
                    }
                });
            }


        } else{
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
            holder.recCard.setOnClickListener(null);
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

    public boolean isSunday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
}
