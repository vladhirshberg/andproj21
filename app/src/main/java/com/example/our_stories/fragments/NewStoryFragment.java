package com.example.our_stories.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.our_stories.R;
import com.example.our_stories.enums.Genre;
import com.example.our_stories.firebase_model.FirebaseModelStory;
import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.Story;
import com.example.our_stories.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewStoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewStoryFragment extends Fragment {

    static final int GALLERY_POSITION = 0;
    static final int CAMERA_POSITION = 1;
    static final int REQUEST_IMAGE_GALLERY = 1;
    static final int REQUEST_IMAGE_CAMERA = 2;
    private View view;
    private EditText title;
    private EditText summary;
    private ImageButton imageEditBtn;
    private ImageView imageV;
    private Bitmap mainImage;
    private Button createBtn;
    ProgressBar progress;
    FirebaseModelStory fb = new FirebaseModelStory();

    //Checkboxes
    private CheckBox actionCB;
    private CheckBox adventureCB;
    private CheckBox horrorCB;
    private CheckBox fictionCB;
    private CheckBox romanceCB;
    private CheckBox mysteryCB;

    private List<CheckBox> cbl;
    private Story newStory;

    public NewStoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewStoryFragment newInstance(String authorId) {
        NewStoryFragment fragment = new NewStoryFragment();
        Bundle args = new Bundle();
        args.putString("authorId", authorId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String authorId = getArguments().getString("authorId");
            newStory = Story.builder().id("0").title("none").authorId(authorId).build();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_story, container, false);
        imageV = view.findViewById(R.id.fragment_new_story_image);
        imageEditBtn = view.findViewById(R.id.fragment_new_story_edit_image_btn);
        createBtn = view.findViewById(R.id.fragment_new_story_create_btn);
        progress = view.findViewById(R.id.fragment_new_story_prog_bar);
        title = view.findViewById(R.id.fragment_new_story_title);
        summary = view.findViewById(R.id.fragment_new_story_summary);

        actionCB = view.findViewById(R.id.fragment_new_story_action_checkBox);
        adventureCB = view.findViewById(R.id.fragment_new_story_adventure_checkBox);
        horrorCB = view.findViewById(R.id.fragment_new_story_horror_checkBox);
        fictionCB = view.findViewById(R.id.fragment_new_story_fiction_checkBox);
        romanceCB = view.findViewById(R.id.fragment_new_story_romance_checkBox);
        mysteryCB = view.findViewById(R.id.fragment_new_story_mystery_checkBox);
        cbl = new ArrayList<CheckBox>(Arrays.asList(actionCB,adventureCB,horrorCB,fictionCB,romanceCB,mysteryCB));
        cbl.forEach(checkBox -> checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckboxClicked(view);
            }
        }));

        createBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addStory();
            }
        });

        imageEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                ShowImageDialog();
            }
        });

        return view;
    }

    public void onCheckboxClicked(View checkbox){
        // Is the view now checked?
        boolean checked = ((CheckBox) checkbox).isChecked();
        if (checked) {
            newStory.addGenre(Genre.valueOf(((CheckBox) checkbox).getText().toString()));
        } else {
            newStory.removeGenre(Genre.valueOf(((CheckBox) checkbox).getText().toString()));
        }
    }

    private void addStory() {
        if (!checkIfEmpty(title)){
            progress.setVisibility(View.VISIBLE);
            newStory.setTitle(title.getText().toString());
            newStory.setId(fb.getNextId());
            Model.instance.addStory(newStory, mainImage, new FirebaseModelStory.IAddStory() {
                @Override
                public void onComplete() {
                    progress.setVisibility(View.INVISIBLE);
                    Bundle args = new Bundle();
                    args.putString("storyId", newStory.getId());
                    Navigation.findNavController(view).navigate(R.id.action_newStoryFragment_to_storyPage, args);
                }
            });
        }
    }

    private boolean checkIfEmpty(EditText edt) {
        String str = edt.getText().toString().trim();
        boolean ret = TextUtils.isEmpty(str) || str.length() == 0;
        if (ret){
            edt.setError(edt.getHint() + " must not be empty");
        }
        return ret;
    }

    // This function manages the picking image - opens a dialog for choosing between
    // gallery and camera
    private void ShowImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.story_cover_title).setItems(R.array.image_array,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == GALLERY_POSITION) {
                            galleryIntent();
                        } else if (which == CAMERA_POSITION) {
                            cameraIntent();
                        }
                    }
                });

        builder.show();
    }

    // This function creates an intent for taking picture by camera
    public void cameraIntent() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA);
        }
    }

    // This function creates an intent for taking picture from gallery
    public void galleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    // This function handling the result of galleryIntent or cameraIntent
    // and updates the user's image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    mainImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    imageV.setImageBitmap(mainImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_IMAGE_CAMERA) {
            mainImage = (Bitmap) data.getExtras().get("data");
            imageV.setImageBitmap(mainImage);
        }
    }

}