package com.example.ethiostore.View_Holder;




import static com.google.android.material.internal.ContextUtils.getActivity;

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
import com.example.ethiostore.App_fragment;
import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.SoftView_Holder>
{

    private List<Apps> horizontalItem;
    private Activity AppActivity;


    public HorizontalAdapter(List<Apps> horizontalItem,Activity appActivity) {
        this.horizontalItem = horizontalItem;
        AppActivity = appActivity;
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
        holder.app_name.setText(apps.getApp_name());
        Picasso.get().load(apps.getApp_icon_url()).resize(200,200).into(holder.app_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (AppActivity != null){
                Intent intent = new Intent(AppActivity, App_Game_DetailActivity.class);
                intent.putExtra("type","Apps");
                intent.putExtra("sid",apps.getApp_id());
                AppActivity.startActivity(intent);
            }}
        });
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
