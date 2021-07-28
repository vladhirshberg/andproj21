package com.example.our_stories.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder=true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @PrimaryKey
    @NonNull
    public String id;
    public String username;
    public String email;
    public String password;
    public String imagePath;

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("id", this.id);
        json.put("username", this.username);
        json.put("email", this.email);
        json.put("imagePath", this.imagePath);
        return json;
    }

    public static User create(Map<String,Object> json) {
        User user = new User();
        user.id = (String)json.get("id");
        user.username = (String)json.get("username");
        user.email = (String)json.get("email");
        user.imagePath = (String)json.get("imagePath");
        return user;
    }
}
