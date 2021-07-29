package com.example.our_stories.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_stories.R;
import com.example.our_stories.adapters.ChapterRecyclerAdapter;
import com.example.our_stories.adapters.CommentRecyclerAdapter;
import com.example.our_stories.firebase_model.FirebaseModelChapter;
import com.example.our_stories.firebase_model.FirebaseModelStory;
import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Chapter;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.Story;
import com.example.our_stories.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class StoryPage extends Fragment {

    static final int REQUEST_LOCAL_FILE = 1;
    private static final int LOCAL_SELECT_POSITION = 0;
    private UserProfileViewModel mViewModel;
    private View view;
    private ImageView mainImage;
    private TextView title;
    private TextView author;
    private RecyclerView chapterList;
    private RecyclerView commentList;
    private TextView summary;
    private FloatingActionButton addChapterBtn;
    private ProgressBar progbar;
    Chapter newChapter;



    MutableLiveData<List<Chapter>> chapters;

    ChapterRecyclerAdapter chapterAdapter;
    CommentRecyclerAdapter CommentAdapter;

    private Story story;

    public static StoryPage newInstance(String userId) {
        return new StoryPage();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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
        String storyId = getArguments().getString("storyId");
        setStoryData(storyId);
        chapters = new MutableLiveData<List<Chapter>>(new LinkedList<Chapter>());

        addChapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChapter();
            }
        });

        chapterList.setLayoutManager(new LinearLayoutManager(getActivity()));
        Model.instance.firebaseModel.getChaptersByStoryId(storyId, new FirebaseModelChapter.IGetChaptersListener() {
            @Override
            public void onComplete(List<Chapter> chapterData) {
                chapters.setValue(chapterData);
                chapterAdapter = new ChapterRecyclerAdapter(chapters.getValue(), getLayoutInflater());
                chapterList.setAdapter(chapterAdapter);
                chapterAdapter.setOnItemClickListener(new ChapterRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(View chapterView, int position) {
                        Bundle args = new Bundle();
                        args.putString("storyId", story.getId());
                        args.putString("chapterId", chapters.getValue().get(position).getChapterId());
                        Navigation.findNavController(view).navigate(R.id.action_storyPage_to_chapterFragment, args);
                    }
                });
            }
        });

        return view;
    }

    private void addChapter() {
        showFileSelectDialog();
    }

    private void setStoryData(String storyId) {
        progbar.setVisibility(View.VISIBLE);
        Model.instance.firebaseModel.firebaseModelStory.getStoryById(storyId, new FirebaseModelStory.IGetStory() {
            @Override
            public void onComplete(Story storyData) {
                story = storyData;
                if (!storyData.getAuthorId().equals(Model.instance.userId)) {
                    addChapterBtn.setVisibility(View.GONE);
                    addChapterBtn.setOnClickListener(null);
                }
                Picasso.get().load(storyData.getImagePath()).placeholder(R.drawable.appicon).error(R.drawable.appicon).into(mainImage);
                title.setText(storyData.getTitle());
                summary.setText(storyData.getSummary());
                Model.instance.firebaseModel.firebaseModelUser.getUserById(storyData.getAuthorId(), new FirebaseModelUser.IGetUserByIdCallback() {
                    @Override
                    public void onComplete(User user) {
                        author.setText(user.getUsername());
                        progbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void showFileSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.file_select_title).setItems(R.array.file_array,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == LOCAL_SELECT_POSITION) {
                            localIntent();
                        }
                    }
                });

        builder.show();
    }

    private void localIntent() {
        String[] mimeTypes = {"application/vnd.google-apps.document","application/msword", "text/plain",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, REQUEST_LOCAL_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_LOCAL_FILE) {
            if (data != null) {
                Uri contentURI = data.getData();
                newChapter = Chapter.builder().storyId(story.getId()).chapterNum(1L).chapterId(Model.firebaseModel.getChapterNextId()).build();
                Model.instance.firebaseModel.uploadChapter(newChapter.getChapterId(), newChapter, contentURI, new FirebaseModelChapter.IAddChapterListener() {
                    @Override
                    public void onComplete(Long chapterNum) {
                        if(chapterNum != null){
                            Model.instance.firebaseModel.getChaptersByStoryId(story.getId(), new FirebaseModelChapter.IGetChaptersListener() {
                                @Override
                                public void onComplete(List<Chapter> chapterData) {
                                    chapters.setValue(chapterData);
                                    chapterAdapter = new ChapterRecyclerAdapter(chapters.getValue(), getLayoutInflater());
                                    chapterList.setAdapter(chapterAdapter);
                                    chapterAdapter.setOnItemClickListener(new ChapterRecyclerAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClicked(View chapterView, int position) {
                                            Bundle args = new Bundle();
                                            args.putString("storyId", story.getId());
                                            args.putString("chapterId", chapters.getValue().get(position).getChapterId());
                                            //Toast.makeText(getActivity(), "args:" + story.getId() + "," + chapterId.toString(), Toast.LENGTH_LONG).show();
                                            Navigation.findNavController(view).navigate(R.id.action_storyPage_to_chapterFragment, args);
                                        }
                                    });
                                }
                            });
                        } else {
                            Log.d("chapter", "fail");
                            newChapter = null;
                            Toast.makeText(getActivity(), "Failed to upload chapter", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }
    }
}
