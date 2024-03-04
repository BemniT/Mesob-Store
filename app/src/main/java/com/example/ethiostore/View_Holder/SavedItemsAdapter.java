package com.example.ethiostore.View_Holder;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ethiostore.App_Game_DetailActivity;
import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.Model.Games;
import com.example.ethiostore.R;
import com.example.ethiostore.Saved_content_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> savedItems;
    private Context context;

    private static final int VIEW_TYPE_APP = 1;
    private static final int VIEW_TYPE_GAME = 2;
    private OnItemClickListener listener;

    public SavedItemsAdapter(List<Object> savedItems, Context context) {
        this.savedItems = savedItems;
        this.context = context;
    }
    public interface OnItemClickListener{
        void onItemClick(Object item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case VIEW_TYPE_APP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_card_vertical, parent, false);
                viewHolder = new AppViewHolder(view);
                break;
            case VIEW_TYPE_GAME:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card_vertical, parent, false);
                viewHolder = new GameViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = savedItems.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemClick(item);
                }
            }
        });

        if (item instanceof Apps) {
            Apps app = (Apps) item;
            ((AppViewHolder) holder).bind(app);
        } else if (item instanceof Games) {
            Games game = (Games) item;
            ((GameViewHolder) holder).bind(game);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = savedItems.get(position);
        if (item instanceof Apps) {
            return VIEW_TYPE_APP;
        } else if (item instanceof Games) {
            return VIEW_TYPE_GAME;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return savedItems.size();
    }

    private class AppViewHolder extends RecyclerView.ViewHolder {
        // Implement ViewHolder for Apps
        public TextView app_name, app_size;
        public ImageView app_image;
        public AppViewHolder(View itemView) {
            super(itemView);
            app_name = itemView.findViewById(R.id.app_name_vertical);
            app_image = itemView.findViewById(R.id.app_Image_vertical);
            app_size = itemView.findViewById(R.id.app_size_vertical);
        }

        public void bind(Apps app) {


            app_name.setText(app.getApp_name());
            Picasso.get().load(app.getApp_icon_url())
                    .resize(140,140)
                    .centerCrop()
                    .into(app_image);
            // Bind other app data as needed
        }
    }

    private class GameViewHolder extends RecyclerView.ViewHolder {
        // Implement ViewHolder for Games
        private TextView game_name, game_size;
        private ImageView game_image, game_icon;
        public GameViewHolder(View itemView) {
            super(itemView);
            game_image = itemView.findViewById(R.id.game_Image_vertical);
            game_name = itemView.findViewById(R.id.game_name_vertical);
            game_size = itemView.findViewById(R.id.game_size_vertical);
        }

        public void bind(Games game) {
            game_name.setText(game.getApp_name());
            Picasso.get().load(game.getApp_icon_url())
                    .resize(180,180)
                    .into(game_image);
            game_size.setText(game.getApp_size());

            // Bind other game data as needed
        }
    }
}