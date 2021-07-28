package com.example.our_stories.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.User;

import java.util.List;

public class UserProfileViewModel extends ViewModel {
    private User selectedUser;

    public UserProfileViewModel() {
    }

    public User getUserById(String userId){
        selectedUser = null;
        Model.instance.getUserById(userId,new FirebaseModelUser.IGetUserByIdCallback() {
            @Override
            public void onComplete(User user) {
                selectedUser = user;
            }
        });

        return selectedUser;
    }
}