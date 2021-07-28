package com.example.our_stories.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.our_stories.enums.Genre;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Story {
    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private String authorId;
    @NonNull
    private String title;
    private String summary;
    private String imagePath;
    private List<Genre> genres;
    private List<Chapter> chapters;
    private List<Comment> comments;
    private String date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Map<String,Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", this.id);
        json.put("authorId", this.authorId);
        json.put("name", this.title);
        json.put("summary", this.summary);
        json.put("imagePath", this.imagePath);
        json.put("date", LocalDateTime.now().toString());
        List<String> genreString = new ArrayList<>();
        this.genres.forEach(genre -> genreString.add(genre.name()));
        Gson genreJson = new GsonBuilder().create();
        json.put("ganres", genreJson.toJson(this.genres));
        return json;
    }

    public void addGenre(Genre genre){
        if (this.genres == null){
            this.genres = new ArrayList<Genre>();
        }
        if(!this.genres.contains(genre)){
            this.genres.add(genre);
        }
    }

    public void removeGenre(Genre genre){
        if(this.genres.contains(genre)){
            this.genres.remove(genre);
        }
    }

    public static Story create(Map<String,Object> json){
        Story story = new Story();
        story.id = (String)json.get("id");
        story.authorId = (String)json.get("authorId");
        story.title = (String)json.get("name");
        story.summary = (String)json.get("summary");
        story.imagePath = (String)json.get("imagePath");
        story.date = (String)json.get("date");
        List<Genre> genres = new ArrayList<Genre>();
        story.genres = genres;
        return story;
    }
}
