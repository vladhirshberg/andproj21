package com.example.our_stories.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.our_stories.R;
import com.example.our_stories.firebase_model.FirebaseModelUser;
import com.example.our_stories.model.Model;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import static java.lang.Thread.sleep;


public class LoginFragment extends Fragment {

    EditText email;
    EditText password;
    Button loginBtn;
    Button registerBtn;
    View view;
    ProgressBar progress;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        Model.instance.getCurrentUser(new FirebaseModelUser.IGetUserListener() {
            @Override
            public void onComplete() {

            }
        });

//        if(Model.getInstance().UserId != null){
//            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainPage);
//        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        progress.setVisibility(View.INVISIBLE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        email.setText("");
        password.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        email = this.view.findViewById(R.id.login_fragment_username);
        password = this.view.findViewById(R.id.login_fragment_pass);
        registerBtn = this.view.findViewById(R.id.login_fragment_register_btn);
        loginBtn = this.view.findViewById(R.id.login_fragment_login_btn);
        progress = this.view.findViewById(R.id.login_fragment_prog_bar);

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                clickRegister();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                clickLogin();
            }
        });

        return view;
    }

    // This function handles login of existing user
    public void clickLogin (){
        progress.setVisibility(View.VISIBLE);
        Model.instance.userLogin(email.getText().toString(), password.getText().toString(), new FirebaseModelUser.IGetUserListener() {
            @Override
            public void onComplete() {
                progress.setVisibility(View.INVISIBLE);
                if (Model.instance.userId != null) {
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainPage);
                } else {
                    Toast.makeText(getActivity(), "Invalid parameters for login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // This function opens the Add new member activity
    public void clickRegister () {
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
    }

    public void googleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(this, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

}