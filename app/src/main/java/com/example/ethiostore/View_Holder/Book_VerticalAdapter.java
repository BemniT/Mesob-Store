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

import com.example.ethiostore.Model.Books;
import com.example.ethiostore.Model.Games;
import com.example.ethiostore.R;
import com.example.ethiostore.Software_Detail_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Book_VerticalAdapter extends RecyclerView.Adapter<Book_VerticalAdapter.ViewHolder>
{

    private List<Books> verticalItems;
    private Activity bookActivity;

    public Book_VerticalAdapter(List<Books> verticalItems, Activity bookActivity) {
        this.verticalItems = verticalItems;
        this.bookActivity = bookActivity;
    }

    @NonNull
    @Override
    public Book_VerticalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card_vertical, parent, false);
        Book_VerticalAdapter.ViewHolder holder = new Book_VerticalAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Book_VerticalAdapter.ViewHolder holder, int position)
    {
        Books books = verticalItems.get(position);
        holder.book_name.setText(books.getSname());
        Picasso.get().load(books.getImage())
                .resize(230, 300)
                .into(holder.book_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (bookActivity != null)
                {
                    Intent intent = new Intent(bookActivity, Software_Detail_Activity.class);
                    intent.putExtra("sid", books.getSid());
                    bookActivity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return verticalItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView book_name, book_size;
        public ImageView book_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            book_image = itemView.findViewById(R.id.book_Image_vertical);
            book_name = itemView.findViewById(R.id.book_name_vertical);
            book_size = itemView.findViewById(R.id.book_size_vertical);

        }
    }
}
