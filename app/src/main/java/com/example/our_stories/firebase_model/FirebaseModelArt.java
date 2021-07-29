package com.example.our_stories.firebase_model;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseModelArt extends FirebaseGeneral{
    private static final String ART_KEY = "Art";
    private FirebaseFirestore db;
    private CollectionReference artReference;

    public FirebaseModelArt() {
        db = FirebaseFirestore.getInstance();
        artReference = db.collection(ART_KEY);
    }
}
