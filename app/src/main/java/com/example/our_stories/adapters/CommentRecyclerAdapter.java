package com.example.our_stories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_stories.R;
import com.example.our_stories.model.Comment;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder>{
    private final List<Comment> comments;
    private final LayoutInflater mInflater;
    private CommentRecyclerAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public void setOnItemClickListener(CommentRecyclerAdapter.OnItemClickListener clickListener) {
        this.listener = clickListener;
    }
    // data is passed into the constructor
    public CommentRecyclerAdapter(List<Comment> data , LayoutInflater inflater) {
        this.comments = data;
        this.mInflater = inflater;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_row, null, false);
        return new CommentViewHolder(view, listener);
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return comments.size();
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final Comment Comment = comments.get(position);
        holder.comment.setText("Comment");
        holder.username.setText(Comment.getUserId());
    }

    // stores and recycles views as they are scrolled off screen
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView comment;
        TextView username;

        CommentViewHolder(View itemView, final CommentRecyclerAdapter.OnItemClickListener onClickListener) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment_row_comment);
            username = itemView.findViewById(R.id.comment_row_username);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.onItemClicked(view, position);
                        }
                    }
                }
            });
        }

    }

    // convenience method for getting data at click position
    Comment getItem(int id) {
        return comments.get(id);
    }


    // parent activity will implement this method to respond to click events
    public interface CommentClickListener {
        void onItemClick(CommentViewHolder view, int position);
    }

}
