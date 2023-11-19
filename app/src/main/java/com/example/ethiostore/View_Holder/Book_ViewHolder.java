package com.example.ethiostore.View_Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ethiostore.Interface.ItemClickListener;
import com.example.ethiostore.Model.Books;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Book_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ItemClickListener itemClickListener;

    public TextView book_name, book_description;

    public ImageView book_images;
    ImageSlider imageSlider;

    public Book_ViewHolder(@NonNull View itemView)
    {
        super(itemView);

        book_name = itemView.findViewById(R.id.book_name);
        book_description = itemView.findViewById(R.id.book_description);
        book_images = itemView.findViewById(R.id.book_Image);

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
