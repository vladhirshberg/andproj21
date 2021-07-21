package com.example.our_stories.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.our_stories.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginFragment extends Fragment {

    EditText username;
    EditText password;
    Button loginBtn;
    Button registerBtn;
    View view;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        username = this.view.findViewById(R.id.login_fragment_username);
        password = this.view.findViewById(R.id.login_fragment_pass);
        registerBtn = this.view.findViewById(R.id.login_fragment_register_btn);
        loginBtn = this.view.findViewById(R.id.login_fragment_login_btn);
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("TAG", "click on register btn");
                clickRegister(view);
            }
        });

        return view;
    }

    // This function handles login of existing user
    public void clickLogin (){
    }

    // This function opens the Add new member activity
    public void clickRegister (View view) {
        Log.d("TAG", "click on register btn");
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
    }


    public LoginFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void googleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
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