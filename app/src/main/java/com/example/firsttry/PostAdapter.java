package com.example.firsttry;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends FirebaseRecyclerAdapter<PostModel, PostAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostAdapter(@NonNull FirebaseRecyclerOptions<PostModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull PostModel model) {
        holder.email.setText("Email: " + model.getEmail());
        holder.text.setText(model.getText());

        // Load image from Firebase Storage URL
        String imgUrl = model.getImgUrl();
        if (imgUrl != null && imgUrl.startsWith("https://firebasestorage.googleapis.com")) {
            // Load image from Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl);
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Load image into ImageView using Glide
                Glide.with(holder.img.getContext())
                        .load(uri)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(holder.img);
            }).addOnFailureListener(e -> {
            });
        } else {
            Glide.with(holder.img.getContext())
                    .load(imgUrl)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.img);
        }
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView email, text;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.postImg);
            email = (TextView)itemView.findViewById(R.id.postEmail);
            text = (TextView)itemView.findViewById(R.id.postText);
        }
    }
}
