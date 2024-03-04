package com.example.ethiostore.View_Holder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ethiostore.Model.Account;
import com.example.ethiostore.Model.Comment;
import com.example.ethiostore.R;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private List<Account> accountsList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_adapter, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private TextView commentText;
        private TextView commentTimestamp;
        private CircleImageView userProfile;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.commentUsername);
            commentText = itemView.findViewById(R.id.commentText);
            commentTimestamp = itemView.findViewById(R.id.commentTimestamp);
            userProfile = itemView.findViewById(R.id.profile_picture);
        }

        public void bind(Comment comment) {
            commentText.setText(comment.getComment());
            userName.setText(comment.getUserName());
//            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
//            String formattedDate = sdf.format(new Date(comment.getTimestamp()));
            commentTimestamp.setText(comment.getTimestamp());
            Picasso.get().load(comment.getUserImage()).into(userProfile);
        }
    }
}
