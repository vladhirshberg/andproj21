package com.example.our_stories.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.our_stories.R;
import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class UserProfile extends Fragment {

    private UserProfileViewModel mViewModel;
    private View view;
    private ImageView avatarIv;
    private TextView usernameTv;
    private Button addStoryBtn;
    private RecyclerView userStoriesList;
    private User user;

    public static UserProfile newInstance(String userId) {
        return new UserProfile();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String userId = getArguments().getString("userId");
        if(userId == null){
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        setProfileInfo(userId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        avatarIv = view.findViewById(R.id.profile_fragment_avatar_iv);
        usernameTv = view.findViewById(R.id.profile_fragment_username_tv);
        addStoryBtn = view.findViewById(R.id.profile_fragment_add_story_btn);
        userStoriesList = view.findViewById(R.id.profile_fragment_user_stories);

        addStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStory(user.id);
            }
        });
        return view;
    }

    private void addStory(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString("authorId", userId);
        Navigation.findNavController(view).navigate(R.id.action_userProfile_to_newStoryFragment, bundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    private void setProfileInfo(String userId){
        Model.getInstance().getUserById(userId, new FirebaseModelUser.IGetUserByIdCallback() {
            @Override
            public void onComplete(User result) {
                if (result != null){
                    user = result;
                    usernameTv.setText(user.username);
                    avatarIv.setImageResource(R.drawable.baseline_account_circle_24);
                    Picasso.get().load(user.imagePath).placeholder(R.drawable.baseline_account_circle_24).error(R.drawable.baseline_account_circle_24).into(avatarIv);
                }
//                if (!userId.equals(Model.getInstance().userId)){
//                    addStoryBtn.setVisibility(View.GONE);
//                }
            }
        });
    }

}