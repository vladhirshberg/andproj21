package com.example.our_stories.firebase_model;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.our_stories.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseModel {

    ValueEventListener eventListener;

    private FirebaseModelUser firebaseModelUser;

    public FirebaseModel(){
        firebaseModelUser = new FirebaseModelUser();
    }

    public void addUser(User newUser, Bitmap image, final FirebaseModelUser.IGetUserListener listener) {
        firebaseModelUser.addUser(newUser, image, listener);
    }

    public void userLogin(String email, String password, final FirebaseModelUser.IGetUserListener listener) {
        firebaseModelUser.userLogin(email, password, new FirebaseModelUser.IGetUserListener() {
            @Override
            public void onComplete() {
                Log.d("dev","onComplete FirebaseModel userLogin");
                listener.onComplete();
            }
        });
    }

    public void getCurrentUser(FirebaseModelUser.IGetUserListener listener) {
        firebaseModelUser.getCurrentUser(new FirebaseModelUser.IGetUserListener() {
            @Override
            public void onComplete() {
                Log.d("dev","onComplete FirebaseModel GetCurrentUser");
                listener.onComplete();
            }
        });
    }

    public void getUserById(String userId, FirebaseModelUser.IGetUserByIdCallback listener) {
        firebaseModelUser.getUserById(userId, new FirebaseModelUser.IGetUserByIdCallback() {
            @Override
            public void onComplete(User user) {
                Log.d("dev","onComplete Model userLogin");
                listener.onComplete(user);
            }
        });
    }
}
