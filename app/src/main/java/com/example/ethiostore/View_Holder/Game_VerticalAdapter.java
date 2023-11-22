package com.example.ethiostore.View_Holder;

import com.example.ethiostore.Model.Games;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Game_VerticalAdapter extends RecyclerView.Adapter<Game_VerticalAdapter.verticalViewHolder> {

    private List<Games> gamesListVertical;

    public Game_VerticalAdapter(List<Games> gamesListVertical) {
        this.gamesListVertical = gamesListVertical;
    }

    @NonNull
    @Override
    public Game_VerticalAdapter.verticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card_vertical, parent,false);
        verticalViewHolder holder = new verticalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Game_VerticalAdapter.verticalViewHolder holder, int position) {
        Games games = gamesListVertical.get(position);
        holder.game_name.setText(games.getSname());
        Picasso.get().load(games.getImage()).into(holder.game_image);
    }

    @Override
    public int getItemCount() {
        return gamesListVertical.size();
    }

    public class verticalViewHolder extends RecyclerView.ViewHolder {

        private TextView game_name, game_size;
        private ImageView game_image, game_icon;
        public verticalViewHolder(@NonNull View itemView) {
            super(itemView);

            game_image = itemView.findViewById(R.id.game_Image_vertical);
            game_name = itemView.findViewById(R.id.game_name_vertical);
            game_size = itemView.findViewById(R.id.game_size_vertical);
        }
    }
}
