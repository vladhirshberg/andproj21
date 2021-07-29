package com.example.our_stories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_stories.R;
import com.example.our_stories.model.Story;
import com.example.our_stories.model.Story;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryRecyclerAdapter.StoryViewHolder>{
    private final List<Story> storys;
    private final LayoutInflater mInflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.listener = clickListener;
    }
    // data is passed into the constructor
    public StoryRecyclerAdapter(List<Story> data , LayoutInflater inflater) {
        this.storys = data;
        this.mInflater = inflater;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.story_list_row, null, false);
        return new StoryViewHolder(view, listener);
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return storys.size();
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(StoryViewHolder holder, int position) {
        final Story story = storys.get(position);
        Picasso.get().load(story.getImagePath()).placeholder(R.drawable.appicon).error(R.drawable.appicon).into(holder.storyImage);
        holder.title.setText(story.getTitle());
        holder.storyId = story.getId();
    }

    // stores and recycles views as they are scrolled off screen
    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView storyImage;
        String storyId;

        StoryViewHolder(View itemView, final OnItemClickListener onClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.story_row_title);
            storyImage = itemView.findViewById(R.id.story_row_image);


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
    Story getItem(int id) {
        return storys.get(id);
    }


    // parent activity will implement this method to respond to click events
    public interface storyClickListener {
        void onItemClick(StoryViewHolder view, int position);
    }
}
