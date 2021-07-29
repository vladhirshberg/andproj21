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
public class Art {
    private String imagePath;
    private String storyId;
    private String uploaderId;
    private Boolean isOfficial;

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("imagePath", this.imagePath);
        json.put("storyId", this.storyId);
        json.put("uploaderId", this.uploaderId);
        json.put("isOfficial", this.isOfficial);
        return json;
    }

    public static Art create(Map<String,Object> json) {
        Art art = new Art();
        art.storyId = (String)json.get("storyId");
        art.uploaderId = (String)json.get("uploaderId");
        art.imagePath = (String)json.get("imagePath");
        art.isOfficial = (Boolean)json.get("isOfficial");
        return art;
    }
}
