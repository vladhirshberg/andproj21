package com.example.our_stories.fragments;

import androidx.lifecycle.ViewModel;

import com.example.our_stories.firebase_model.FirebaseModelStory;
import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.Story;
import com.example.our_stories.model.User;

public class StoryViewModel extends ViewModel {
    private Story selectedUser;

    public StoryViewModel() {
    }

    public Story getStoryById(String storyId){
        Story selectedStory = null;
        Model.instance.firebaseModel.firebaseModelStory.getStoryById(storyId, new FirebaseModelStory.IGetStory() {
            @Override
            public void onComplete(Story story) {
                selectedUser = story;
            }
        });

        return selectedUser;
    }
}