package com.example.our_stories.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
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
public class Comment {
    @NonNull
    @PrimaryKey
    private String id;
    @NonNull
    private String storyId;
    private Long chapterNum;
    private String content;
    private String userId;
    private String date;


    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("chapterNum", this.chapterNum);
        json.put("storyId", this.storyId);
        json.put("content", this.content);
        json.put("userId", this.userId);
        json.put("date", this.date);
        return json;
    }

    public static Comment create(Map<String,Object> json) {
        Comment comment = new Comment();
        comment.chapterNum = (Long)json.get("chapterNum");
        comment.storyId = (String)json.get("storyId");
        comment.userId = (String)json.get("userId");
        comment.content = (String)json.get("content");
        comment.date = (String)json.get("date");
        return comment;
    }
}
