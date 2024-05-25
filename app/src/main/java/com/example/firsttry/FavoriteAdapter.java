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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<FavoriteDataClass> dataList;

    public FavoriteAdapter(Context context, List<FavoriteDataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_recycler_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.recTitle.setText(dataList.get(position).getTitle());
        holder.recLocation.setText(dataList.get(position).getLocation());
        holder.recDesc.setText(dataList.get(position).getDescription());


        Random rand = new Random();
        int randomNumber = rand.nextInt(4) + 1;
        int imageResource;
        switch (randomNumber) {
            case 1:
                imageResource = R.drawable.imaginelac;
                break;
            case 2:
                imageResource = R.drawable.imaginelac2;
                break;
            case 3:
                imageResource = R.drawable.imaginelac3;
                break;
            case 4:
                imageResource = R.drawable.imaginelac4;
                break;
            default:

                imageResource = R.drawable.imaginelac;
                break;
        }
        holder.recImg.setImageResource(imageResource);

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FavoriteDetailActivity.class);
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("Location", dataList.get(holder.getAdapterPosition()).getLocation());
                intent.putExtra("Desc", dataList.get(holder.getAdapterPosition()).getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView recTitle, recLocation, recDesc;
        ImageView recImg;
        CardView recCard;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            recTitle = itemView.findViewById(R.id.FavTitle);
            recLocation = itemView.findViewById(R.id.FavLocation);
            recDesc = itemView.findViewById(R.id.FavDetails);
            recImg = itemView.findViewById(R.id.FavImg);
            recCard = itemView.findViewById(R.id.FavCard);
        }
    }
}
