package com.example.ethiostore.View_Holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.Vertical_ViewHolder>
{

    private List<Apps> verticalItems;
    private Activity appActivity;

    public VerticalAdapter(List<Apps> verticalItems, Activity appActivity) {
        this.verticalItems = verticalItems;
        this.appActivity = appActivity;
    }

    @NonNull
    @Override
    public VerticalAdapter.Vertical_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_card_vertical,parent,false);
        Vertical_ViewHolder holder = new Vertical_ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalAdapter.Vertical_ViewHolder holder, int position)
    {
        Apps apps = verticalItems.get(position);
        holder.app_name.setText(apps.getSname());
        Picasso.get().load(apps.getImage()).into(holder.app_image);
    }

    @Override
    public int getItemCount() {
        return verticalItems.size();
    }

    public class Vertical_ViewHolder extends RecyclerView.ViewHolder {

        public TextView app_name, app_size;
        public ImageView app_image;
        public Vertical_ViewHolder(@NonNull View itemView) {
            super(itemView);


            app_name = itemView.findViewById(R.id.app_name_vertical);
            app_image = itemView.findViewById(R.id.app_Image_vertical);
            app_size = itemView.findViewById(R.id.app_size_vertical);
        }
    }
}
