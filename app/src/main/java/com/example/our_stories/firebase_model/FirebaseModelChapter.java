package com.example.our_stories.firebase_model;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.our_stories.model.Chapter;
import com.example.our_stories.model.Story;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseModelChapter{
    private static final String CHAPTERS_KEY = "Chapters";
    private FirebaseFirestore db;
    private CollectionReference chaptersReference;

    public FirebaseModelChapter() {
        db = FirebaseFirestore.getInstance();
        chaptersReference = db.collection(CHAPTERS_KEY);
    }

    public interface IAddChapterListener
    {
        void onComplete(Long chapterNum);
    }

    public String getChapterNextId(){
        return chaptersReference.document().getId();
    }

    public void uploadChapter(String chapterId, Chapter newChapter, Uri contentURI, IAddChapterListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Query query = chaptersReference.whereEqualTo("storyId", newChapter.getStoryId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                    Long nextChapter = 0L;
                    for (QueryDocumentSnapshot chapterDocument : task.getResult()) {
                        if(Chapter.create(chapterDocument.getData()).getChapterNum() > nextChapter){
                            nextChapter = Chapter.create(chapterDocument.getData()).getChapterNum();
                        }
                    }
                    nextChapter += 1;
                    newChapter.setChapterNum(nextChapter);
                }
                final StorageReference chapterRef = storage.getReference().child("chapters").child(chapterId);
                UploadTask uploadTask = chapterRef.putFile(contentURI);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        listener.onComplete(null);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        chapterRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(Uri uri) {
                                newChapter.setContentPath(uri.getPath());
                                Map<String, Object> nestedData = new HashMap<>();
                                nestedData.put(newChapter.getChapterNum().toString(), newChapter.toJson());
                                db.collection(CHAPTERS_KEY).document(chapterId).set(newChapter.toJson());
                                listener.onComplete(newChapter.getChapterNum());
                            }
                        });
                    }
                });
            }
        });

    }

    public interface IGetChaptersListener
    {
        void onComplete(List<Chapter> chapters);
    }

    public void getChaptersByStoryId(String storyId, IGetChaptersListener listener) {
        chaptersReference.whereEqualTo("storyId", storyId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Chapter> chapters = new ArrayList<Chapter>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot chapterDocument : task.getResult()) {
                        chapters.add(Chapter.create(chapterDocument.getData()));
                    }
                }
                listener.onComplete(chapters);
            }
        });
    }

    public interface IGetChapterListener
    {
        void onComplete(Chapter chapter);
    }

    public interface IGetChapterContentListener
    {
        void onComplete(String path);
    }

    public void getChapterContentById(String chapterId, IGetChapterContentListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference chapterRef = storage.getReference().child("chapters").child(chapterId);
        File localFile = null;
        try {
            localFile = File.createTempFile(chapterId, "txt");
        } catch (IOException e) {
            listener.onComplete(null);
        }

        File finalLocalFile = localFile;
        chapterRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                listener.onComplete(finalLocalFile.getAbsolutePath());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                finalLocalFile.deleteOnExit();
                listener.onComplete(null);
            }
        });
    }


}
