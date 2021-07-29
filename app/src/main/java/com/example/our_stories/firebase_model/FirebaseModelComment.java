package com.example.our_stories.firebase_model;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseModelComment {
    private static final String COMMENTS_KEY = "Comments";
    private FirebaseFirestore db;
    private CollectionReference commentReference;

    public FirebaseModelComment() {
        db = FirebaseFirestore.getInstance();
        commentReference = db.collection(COMMENTS_KEY);
    }
}
