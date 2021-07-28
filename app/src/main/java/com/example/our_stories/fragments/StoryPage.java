package com.example.our_stories.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_stories.R;
import com.example.our_stories.firebase_model.FirebaseModelStory;
import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.Story;
import com.example.our_stories.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class StoryPage extends Fragment {

    private UserProfileViewModel mViewModel;
    private View view;
    private ImageView mainImage;
    private TextView title;
    private TextView author;
    private Button addChapterButton;
    private RecyclerView chapterList;
    private RecyclerView commentList;
    private TextView summary;
    private FloatingActionButton addChapterBtn;
    private ProgressBar progbar;

    private Story story;

    public static StoryPage newInstance(String userId) {
        return new StoryPage();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String storyId = getArguments().getString("storyId");
        setStoryData(storyId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_story_page, container, false);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        mainImage = view.findViewById(R.id.story_fragment_image_iv);
        title = view.findViewById(R.id.story_fragment_title_tv);
        author = view.findViewById(R.id.story_fragment_author_tv);
        summary = view.findViewById(R.id.story_fragment_summary);
        chapterList = view.findViewById(R.id.chapters_recycler_view);
        commentList = view.findViewById(R.id.comments_recycler_view);
        addChapterBtn = view.findViewById(R.id.story_fragment_add_chapter);
        progbar = view.findViewById(R.id.story_fragment_prog_bar);
        progbar.setVisibility(View.GONE);

        addChapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChapter(story.getId());
            }
        });
        return view;
    }

    private void addChapter(String storyId) {
        Bundle bundle = new Bundle();
        bundle.putString("storyId", storyId);
        //TODO fix navigation
        // Navigation.findNavController(view).navigate(R.id.action_userProfile_to_newStoryFragment, bundle);
    }


    private void setStoryData(String storyId){
        progbar.setVisibility(View.VISIBLE);
        Model.instance.firebaseModel.firebaseModelStory.getStoryById(storyId, new FirebaseModelStory.IGetStory() {
            @Override
            public void onComplete(Story story) {
                Picasso.get().load(story.getImagePath()).placeholder(R.drawable.appicon).error(R.drawable.appicon).into(mainImage);
                title.setText(story.getTitle());
                summary.setText(story.getSummary());
                Model.instance.firebaseModel.firebaseModelUser.getUserById(story.getAuthorId(), new FirebaseModelUser.IGetUserByIdCallback() {
                    @Override
                    public void onComplete(User user) {
                        author.setText(user.getUsername());
                        progbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

}