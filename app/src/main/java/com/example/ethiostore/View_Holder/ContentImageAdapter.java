package com.example.ethiostore.View_Holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ethiostore.Model.AdditionalImages;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContentImageAdapter extends RecyclerView.Adapter<ContentImageAdapter.ViewHolder> {
    private List<String> imageUrls;

    public ContentImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ContentImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentImageAdapter.ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
