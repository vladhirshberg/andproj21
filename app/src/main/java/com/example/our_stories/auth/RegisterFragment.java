package com.example.our_stories.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Model;
import com.example.our_stories.model.User;


import com.example.our_stories.R;

import java.io.IOException;

public class RegisterFragment extends Fragment {
    static final int GALLERY_POSITION = 0;
    static final int CAMERA_POSITION = 1;
    static final int REQUEST_IMAGE_GALLERY = 1;
    static final int REQUEST_IMAGE_CAMERA = 2;
    private View view;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private ImageButton userImage;
    private ImageView avatar;
    private Bitmap avatarData;
    private Button registerBtn;
    ProgressBar progress;

    private User newUser;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        username = view.findViewById(R.id.register_fragment_username);
        email = view.findViewById(R.id.register_fragment_email);
        password = view.findViewById(R.id.register_fragment_pass);
        passwordConfirm = view.findViewById(R.id.register_fragment_pass_confirm);
        userImage = view.findViewById(R.id.register_fragment_edit_image_btn);
        avatar = view.findViewById(R.id.register_fragment_user_image);
        registerBtn = view.findViewById(R.id.register_fragment_register_btn);
        progress = view.findViewById(R.id.register_fragment_prog_bar);
        progress.setVisibility(View.INVISIBLE);


        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                clickRegister();
            }
        });

        userImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ShowImageDialog();
            }
        });

        return view;
    }

    private boolean allChecks(){
        return !(checkIfEmpty(username) || checkIfEmpty(email) || checkIfEmpty(password) || checkIfEmpty(passwordConfirm))
                && passCheck() && passwordLengthCheck();
    }

    private boolean passwordLengthCheck() {
        if (password.getText().toString().length() < 6){
            password.setError("Password must be 6 characters or longer");
            return false;
        }
        return true;
    }

    private void clickRegister() {
        if (allChecks()){
            progress.setVisibility(View.VISIBLE);
            newUser = User.builder().id("0").email(email.getText().toString()).username(username.getText().toString()).password(password.getText().toString()).build();
            Model.getInstance().addUser(newUser, avatarData, new FirebaseModelUser.IGetUserListener() {
                @Override
                public void onComplete() {
                    progress.setVisibility(View.INVISIBLE);
                    if(Model.getInstance().userId != null){
                        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_mainPage);
                    }
                }
            });
        }
    }

    private boolean passCheck() {
        boolean ret = true;
        if (!password.getText().toString().equals(passwordConfirm.getText().toString())){
            passwordConfirm.setError("Password do not match");
            ret = false;
        }
        return ret;
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
        builder.setTitle(R.string.image_edit_title).setItems(R.array.image_array,
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
                    avatarData = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    avatar.setImageBitmap(avatarData);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_IMAGE_CAMERA) {
            avatarData = (Bitmap) data.getExtras().get("data");
            avatar.setImageBitmap(avatarData);
        }
    }

}