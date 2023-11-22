package com.example.ethiostore.View_Holder;

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

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.SoftView_Holder>
{

    private List<Apps> horizontalItem;

    public HorizontalAdapter(List<Apps> horizontalItem) {
        this.horizontalItem = horizontalItem;
    }

    @NonNull
    @Override
    public SoftView_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_card_horizontal, parent,false);
        return new SoftView_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoftView_Holder holder, int position) {
        Apps apps = horizontalItem.get(position);
        holder.app_name.setText(apps.getSname());
        Picasso.get().load(apps.getImage()).into(holder.app_image);
    }

    @Override
    public int getItemCount() {
        return horizontalItem.size();
    }

    public class SoftView_Holder extends RecyclerView.ViewHolder
    {
        public TextView app_name;
        public ImageView app_image;
        public SoftView_Holder(@NonNull View itemView) {
            super(itemView);

            app_name = itemView.findViewById(R.id.app_name_horizontal);
            app_image = itemView.findViewById(R.id.app_image_horizontal);
        }
    }
}
