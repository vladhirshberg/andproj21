package com.example.our_stories.fragments;

import android.net.http.SslError;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.our_stories.R;
import com.example.our_stories.firebase_model.FirebaseModelChapter;
import com.example.our_stories.model.Model;
import com.google.firebase.storage.FirebaseStorage;
import com.example.our_stories.ReadFile;

import static com.example.our_stories.ReadFile.readChapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChapterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChapterFragment extends Fragment {
    View view;
    TextView content;
    TextView title;
    TextView nextTop;
    TextView nextBot;
    TextView prevTop;
    TextView prevBot;
    String chapterId;
    Long chapterNum;
    String storyId;


    public ChapterFragment() {
    }

    public static ChapterFragment newInstance() {return new ChapterFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chapterId = getArguments().getString("chapterId");
            storyId = getArguments().getString("storyId");
            getChapterContent();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.chapter_fragment, container, false);
        content = view.findViewById(R.id.chapter_fragment_content_tv);
        return view;
    }

    private void getChapterContent(){
        Model.firebaseModel.getChapterContentById(this.chapterId, new FirebaseModelChapter.IGetChapterContentListener() {
            @Override
            public void onComplete(String path) {
                if (path != null){
                    content.setText(readChapter(path));
                }
            }
        });
    }

}