package com.example.our_stories.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
@Entity
@Builder
public class Chapter {
    @NonNull
    @PrimaryKey
    private Long chapterNum;
    @NonNull
    @PrimaryKey
    private String storyId;
    private String title;
    private String contentPath;
    private List<Comment> comments;
    private Long date;

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("chapterNum", this.chapterNum);
        json.put("storyId", this.storyId);
        json.put("title", this.title);
        json.put("contentPath", this.contentPath);
        json.put("date", System.currentTimeMillis());
        return json;
    }

    public static Chapter create(Map<String,Object> json) {
        Chapter chapter = new Chapter();
        chapter.chapterNum = (Long)json.get("chapterNum");
        chapter.storyId = (String)json.get("storyId");
        chapter.title = (String)json.get("title");
        chapter.contentPath = (String)json.get("contentPath");
        chapter.date = (Long)json.get("date");
        return chapter;
    }
}
