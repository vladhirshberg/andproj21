package com.example.our_stories.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_stories.R;
import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Comment;
import com.example.our_stories.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder>{
    private List<Comment> comments;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    CommentRecyclerAdapter(Context context, List<Comment> data) {
        this.mInflater = LayoutInflater.from(context);
        this.comments = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.author = null;
        FirebaseModelUser users = new FirebaseModelUser();
        users.getUserById(comment.getUserId(), new FirebaseModelUser.IGetUserByIdCallback() {
            @Override
            public void onComplete(User user) {
                holder.author = user;
            }
        });
        Picasso.get().load(holder.author.imagePath).placeholder(R.drawable.baseline_account_circle_24).error(R.drawable.baseline_account_circle_24).into(holder.avatar);
        holder.username.setText(holder.author.username);
        holder.comment.setText(comment.getContent());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return comments.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView comment;
        User author;
        TextView username;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.comment_row_avatar);
            comment = itemView.findViewById(R.id.comment_row_comment);
            username = itemView.findViewById(R.id.comment_row_username);
        }

    }

    // convenience method for getting data at click position
    Comment getItem(int id) {
        return comments.get(id);
    }
}
