package com.example.our_stories.model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.our_stories.firebase_model.FirebaseModel;
import com.example.our_stories.firebase_model.FirebaseModelStory;
import com.example.our_stories.firebase_model.FirebaseModelUser;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static String userId = null;
    FirebaseModel firebaseModel;

    public static Model instance = new Model();

    private Model(){
        firebaseModel = new FirebaseModel();
    }

    public static Model getInstance() {
        return instance;
    }

    public void addUser(User newUser, Bitmap avatar, FirebaseModelUser.IGetUserListener listener) {
        firebaseModel.addUser(newUser, avatar, listener);
    }

    public void userLogin(String email, String password, FirebaseModelUser.IGetUserListener listener) {
        firebaseModel.userLogin(email, password,new FirebaseModelUser.IGetUserListener() {
            @Override
            public void onComplete() {
                Log.d("dev","onComplete Model userLogin");
                listener.onComplete();
            }
        });
    }

    public void getCurrentUser(FirebaseModelUser.IGetUserListener listener) {
        firebaseModel.getCurrentUser(new FirebaseModelUser.IGetUserListener() {
            @Override
            public void onComplete() {
                Log.d("dev","onComplete Model userLogin");
                listener.onComplete();
            }
        });
    }

    public void getUserById(String userId, FirebaseModelUser.IGetUserByIdCallback listener) {
        firebaseModel.getUserById(userId, new FirebaseModelUser.IGetUserByIdCallback() {
            @Override
            public void onComplete(User user) {
                Log.d("dev","onComplete Model userLogin");
                listener.onComplete(user);
            }
        });
    }

    MutableLiveData<List<Story>> stories = new MutableLiveData<List<Story>>(new LinkedList<Story>());

    public void addStory(Story newStory, Bitmap mainImage, FirebaseModelStory.IAddStory listener) {
        firebaseModel.addStory(newStory, mainImage, listener);
    }

//    public LiveData<List<Story>> getAllStories() {
//        FirebaseModel.getAllStories((storiesResult) -> {
//            stories.setValue(storiesResult);
//        });
//        return allStudents;
//    }
}
