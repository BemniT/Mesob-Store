package com.example.ethiostore.View_Holder;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ethiostore.App_Game_DetailActivity;
import com.example.ethiostore.Model.Games;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Game_HorizontalAdapter extends RecyclerView.Adapter<Game_HorizontalAdapter.ViewHolder>
{
    private List<Games> horizontalItems;
    private Activity gameActivity;

    public Game_HorizontalAdapter(List<Games> horizontalItems, Activity gameActivity) {
        this.horizontalItems = horizontalItems;
        this.gameActivity = gameActivity;
    }

    @NonNull
    @Override
    public Game_HorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card_horizontal, parent, false);
        ViewHolder  holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Game_HorizontalAdapter.ViewHolder holder, int position) {

        Games games = horizontalItems.get(position);
        holder.game_name.setText(games.getSname());
        Picasso.get().load(games.getImage())
                .resize(500,250)
                .into(holder.game_image);
        Picasso.get().load(games.getIcon())
                .resize(130,130)
                .into(holder.game_icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (gameActivity != null)
                {
                    Intent intent = new Intent(gameActivity, App_Game_DetailActivity.class);
                    intent.putExtra("sid",games.getSid());
                    intent.putExtra("type","Games");
                    gameActivity.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView game_name, game_size;
        public ImageView game_image, game_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            game_image = itemView.findViewById(R.id.game_Image_horizontal);
            game_name = itemView.findViewById(R.id.game_name_horizontal);
            game_size = itemView.findViewById(R.id.game_size_horizontal);
            game_icon = itemView.findViewById(R.id.game_icon_horizontal);
        }
    }
}
