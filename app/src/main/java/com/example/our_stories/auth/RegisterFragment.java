package com.example.our_stories.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.example.our_stories.R;

public class RegisterFragment extends Fragment {

    private View view;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private Button register;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;


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

        username = view.findViewById(R.id.register_fragment_username);
        email = view.findViewById(R.id.register_fragment_email);
        password = view.findViewById(R.id.register_fragment_pass);
        passwordConfirm = view.findViewById(R.id.register_fragment_pass_confirm);
        register = view.findViewById(R.id.register_fragment_register_btn);

        return view;
    }
}