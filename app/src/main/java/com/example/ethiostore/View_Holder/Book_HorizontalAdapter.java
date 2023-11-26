package com.example.ethiostore.View_Holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ethiostore.Model.Books;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Book_HorizontalAdapter extends RecyclerView.Adapter<Book_HorizontalAdapter.ViewHolder>
{
    private List<Books> horizontalItems;
    @NonNull
    @Override
    public Book_HorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card_horizontal, parent, false);
        Book_HorizontalAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Book_HorizontalAdapter.ViewHolder holder, int position)
    {
        Books books = horizontalItems.get(position);
        holder.boo_name.setText(books.getSname());
        Picasso.get().load(books.getImage()).into(holder.book_image);
    }

    @Override
    public int getItemCount() {
        return horizontalItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView boo_name;
        private ImageView book_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            boo_name = itemView.findViewById(R.id.book_name_horizontal);
            book_image = itemView.findViewById(R.id.book_image_horizontal);
        }
    }
}
