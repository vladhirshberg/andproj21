package com.example.our_stories.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.our_stories.R;
import com.example.our_stories.adapters.ChapterRecyclerAdapter;
import com.example.our_stories.adapters.StoryRecyclerAdapter;
import com.example.our_stories.firebase_model.FirebaseModelStory;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.Story;

import java.util.List;

public class StoriesFragment extends Fragment {

    private RecyclerView storyList;
    StoryRecyclerAdapter storyAdapter;
    List<Story> stories;


    public static StoriesFragment newInstance() {
        return new StoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stories_fragment, container, false);
        Model.firebaseModel.getAllStories(new FirebaseModelStory.IGetStories() {
            @Override
            public void onComplete(List<Story> allStories) {
                stories = allStories;
                storyList = view.findViewById(R.id.stories_recycler_view);
                storyList.setLayoutManager(new LinearLayoutManager(getActivity()));
                storyAdapter = new StoryRecyclerAdapter(stories, getLayoutInflater());
                storyList.setAdapter(storyAdapter);
                storyAdapter.setOnItemClickListener(new StoryRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(View chapterView, int position) {
                        Bundle args = new Bundle();
                        args.putString("storyId", stories.get(position).getId());
                        Navigation.findNavController(view).navigate(R.id.action_storiesFragment_to_storyPage, args);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}