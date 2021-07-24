package com.example.our_stories.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.our_stories.firebase_model.FirebaseModel;
import com.example.our_stories.firebase_model.FirebaseModelUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static User currentUser = null;
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

}
