package com.example.ethiostore.View_Holder;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ethiostore.Interface.ItemClickListener;
import com.example.ethiostore.R;


public class App_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;
    public TextView app_name, app_description;
    public ImageView app_image;

    public App_ViewHolder(@NonNull View itemView) {
        super(itemView);

        app_name = itemView.findViewById(R.id.app_name);
        app_image = itemView.findViewById(R.id.app_Image);
    }


    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}
