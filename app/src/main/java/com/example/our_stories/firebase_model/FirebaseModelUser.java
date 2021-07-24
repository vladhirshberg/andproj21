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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseModelUser extends FirebaseGeneral{
    private static final String USERS_KEY = "Users";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private DatabaseReference usersReference;
    ValueEventListener eventListener;

    public FirebaseModelUser() {
        mAuth = FirebaseAuth.getInstance();
        database =  FirebaseDatabase.getInstance();
        usersReference = database.getReference(USERS_KEY);
        db = FirebaseFirestore.getInstance();
    }

    public interface IGetUserListener {
        void onComplete();
    }

    public void getCurrentUser(IGetUserListener listener) {
        if (Model.getInstance().currentUser != null) {
            listener.onComplete();
        }
        else {
            final FirebaseUser firebaseUser = mAuth.getCurrentUser();
            Log.d("dev","getCurrentUser ModelUserFirebase ");

            if (firebaseUser == null) {
                Model.getInstance().currentUser = null;
                listener.onComplete();
            }
            else {
                Log.d("dev","getCurrentUser ModelUserFirebase "+ firebaseUser.getUid());
                getUserById(firebaseUser.getUid(), new IGetUserByIdCallback() {
                    @Override
                    public void onComplete(User user) {
                        Model.getInstance().currentUser = user.toBuilder().build();
                        Model.getInstance().currentUser.setId(firebaseUser.getUid());
                        listener.onComplete();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        }
    }

    // Get user by ID
    interface IGetUserByIdCallback {
        void onComplete(User user);
        void onCancel();
    }
    public void getUserById(String id, final IGetUserByIdCallback listener) {
        usersReference.child(USERS_KEY).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Model.getInstance().currentUser = dataSnapshot.getValue(User.class);
                listener.onComplete(Model.getInstance().currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onCancel();
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
                        Model.getInstance().currentUser = newUser;
                        uploadImage(image, USERS_KEY, Model.getInstance().currentUser.getUsername(), new UploadImageListener() {
                            @Override
                            public void onComplete(String uri) {
                                Model.getInstance().currentUser.setImagePath(uri);
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmailAndPassword:success");
                                db.collection(USERS_KEY).document(newUser.id)
                                        .set(newUser.toJson());
                                usersReference.push();
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
                                Model.getInstance().currentUser = User.builder().id(user.getUid()).build();
                                getCurrentUser(new IGetUserListener() {
                                    @Override
                                    public void onComplete() {
                                        listener.onComplete();
                                    }
                                });
                            } else {
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Model.getInstance().currentUser = null;
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

//    public interface GetAllUsersListener{
//        public void onComplete(List<User> students);
//    }
//
//    public static void getAllUsers(GetAllUsersListener listener){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(USERS_KEY)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        List<User> list = new LinkedList<User>();
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                list.add(User.create(document.getData()));
//                            }
//                        } else {
//
//                        }
//                        listener.onComplete(list);
//                    }
//                });
//    }

    public void signOut()
    {
        mAuth.signOut();
        Model.getInstance().currentUser = null;
    }
}
