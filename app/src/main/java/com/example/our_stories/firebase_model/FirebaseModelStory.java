package com.example.our_stories.firebase_model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.our_stories.enums.Genre;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.Story;
import com.example.our_stories.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseModelStory extends FirebaseGeneral{
    private static final String STORIES_KEY = "Stories";
    private FirebaseFirestore db;
    private CollectionReference storiesReference;

    public FirebaseModelStory() {
        db = FirebaseFirestore.getInstance();
        storiesReference = db.collection(STORIES_KEY);
    }

    private LiveData<List<Story>> storiesList;

    public interface IGetStory
    {
        void onComplete(Story story);
    }

    public void getStoryById(String storyId, FirebaseModelStory.IGetStory listener) {
        if(storyId == null){
            listener.onComplete(null);
        }
        Log.d("TAG", "getting user by id from firebase");
        storiesReference.document(storyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Story story = null;
                if (task.isSuccessful()){
                    story = Story.create(task.getResult().getData());
                }
                listener.onComplete(story);
            }
        });
    }

    public interface IGetStories
    {
        void onComplete(List<Story> stories);
    }

    public void getAllStories(IGetStories listener) {
        storiesReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Story> storiesList = new ArrayList<Story>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot storyDocument : task.getResult()) {
                        storiesList.add(Story.create(storyDocument.getData()));
                    }
                }

                listener.onComplete(storiesList);
            }
        });
    }

    public void getStoriesByUserId(String userId, IGetStories listener) {
        storiesReference.whereEqualTo("authorId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Story> storiesList = new ArrayList<Story>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot storyDocument : task.getResult()) {
                        storiesList.add(Story.create(storyDocument.getData()));
                    }
                }

                listener.onComplete(storiesList);
            }
        });
    }

    public interface IAddStory
    {
        void onComplete();
    }

    public String getNextId(){
        return storiesReference.document().getId();
    }

    public void addStory(Story newStory, Bitmap mainImage, FirebaseModelStory.IAddStory listener) {
        uploadImage(mainImage, STORIES_KEY, newStory.getId(), new UploadImageListener() {
            @Override
            public void onComplete(String uri) {
                newStory.setImagePath(uri);
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "add new story");
                db.collection(STORIES_KEY).document(newStory.getId())
                        .set(newStory.toJson());
                listener.onComplete();
            }
        });
    }

//    public LiveData<List<Story>> getData() {
//        return storiesList;
//    }
}
