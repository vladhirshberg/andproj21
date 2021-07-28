package com.example.our_stories.firebase_model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.our_stories.model.Model;
import com.example.our_stories.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import static java.lang.Thread.sleep;

public class FirebaseModelUser extends FirebaseGeneral{
    private static final String USERS_KEY = "Users";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference usersReference;

    public FirebaseModelUser() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersReference = db.collection(USERS_KEY);
    }

    public interface IGetUserListener {
        void onComplete();
    }

    public void getCurrentUser(IGetUserListener listener) {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Model.instance.userId = null;
            listener.onComplete();
        }
        else {
            Log.d("dev","getCurrentUser ModelUserFirebase "+ firebaseUser.getUid());
            Model.instance.userId = firebaseUser.getUid();
        }
    }

    public interface IGetUserByIdCallback {
        void onComplete(User user);
    }

    public void getUserById(String id, final IGetUserByIdCallback listener) {
        if(id == null){
            listener.onComplete(null);
        }
        Log.d("TAG", "getting user by id from firebase");
        usersReference.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = null;
                if (task.isSuccessful()){
                    user = User.create(task.getResult().getData());
                }
               listener.onComplete(user);
            }
        });
    }

    public void addUser(final User newUser, Bitmap image, final IGetUserListener listener) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword()).
            addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        newUser.setId(firebaseUser.getUid());
                        uploadImage(image, USERS_KEY, newUser.getUsername(), new UploadImageListener() {
                            @Override
                            public void onComplete(String uri) {
                                newUser.setImagePath(uri);
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmailAndPassword:success");
                                db.collection(USERS_KEY).document(newUser.id)
                                        .set(newUser.toJson());
                                Model.instance.userId = firebaseUser.getUid();
                                listener.onComplete();
                            }
                        });
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                        listener.onComplete();
                    }
                }
            });
    }

    public void userLogin(String email, String password, final IGetUserListener listener) {
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Model.instance.userId = user.getUid();
                            listener.onComplete();
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Model.instance.userId = null;
                            listener.onComplete();
                        }
                    }
                });
        }
        else
        {
            listener.onComplete();
        }
    }

    public void signOut()
    {
        mAuth.signOut();
        Model.instance.userId = null;
    }
}
