package com.example.our_stories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_stories.R;
import com.example.our_stories.model.Chapter;

import java.util.List;

public class ChapterRecyclerAdapter extends RecyclerView.Adapter<ChapterRecyclerAdapter.ChapterViewHolder>{
    private final List<Chapter> chapters;
    private final LayoutInflater mInflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.listener = clickListener;
    }
    // data is passed into the constructor
    public ChapterRecyclerAdapter(List<Chapter> data , LayoutInflater inflater) {
        this.chapters = data;
        this.mInflater = inflater;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chapter_row, null, false);
        return new ChapterViewHolder(view, listener);
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return chapters.size();
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        final Chapter chapter = chapters.get(position);
//        ZoneId zoneId = ZoneId.of ( "GMT" );
//        ZonedDateTime zdt = ZonedDateTime.ofInstant ( Instant.ofEpochMilli(chapter.getDate()) , zoneId );
//        holder.date.setText(zdt.toString());
        holder.date.setText("");
        holder.chapterNum.setText(String.format("Chapter %d", chapter.getChapterNum()));
        holder.title.setText(chapter.getTitle());
        holder.chapterId = chapter.getChapterId();
    }

    // stores and recycles views as they are scrolled off screen
    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView chapterNum;
        TextView date;
        String chapterId;

        ChapterViewHolder(View itemView, final OnItemClickListener onClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.chapter_row_title);
            chapterNum = itemView.findViewById(R.id.chapter_row_number);
            date = itemView.findViewById(R.id.chapter_row_date);


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
    Chapter getItem(int id) {
        return chapters.get(id);
    }


    // parent activity will implement this method to respond to click events
    public interface chapterClickListener {
        void onItemClick(ChapterViewHolder view, int position);
    }
}
