package com.example.ethiostore.View_Holder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ethiostore.App_Game_DetailActivity;
import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
        holder.app_name.setText(apps.getApp_name());
        Picasso.get().load(apps.getApp_icon_url())
                .resize(140,140)
                .centerCrop()
                .into(holder.app_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (appActivity != null){
                    Intent intent = new Intent(appActivity, App_Game_DetailActivity.class);
                    intent.putExtra("type","Apps");
                    intent.putExtra("sid",apps.getApp_id());
                    appActivity.startActivity(intent);
                }
            }
        });
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
