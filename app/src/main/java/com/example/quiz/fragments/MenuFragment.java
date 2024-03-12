package com.example.quiz.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuFragment extends Fragment {

    private Button letsQuizButton,resultsButton, logoutButton;
    TextView textView;
    FirebaseAuth auth;
    FirebaseUser user;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        logoutButton = view.findViewById(R.id.logout);
        letsQuizButton = view.findViewById(R.id.mButton);
        textView = view.findViewById(R.id.userDetails);
        letsQuizButton.setOnClickListener(v -> replaceFragment(new QuestionFragment()));
        resultsButton = view.findViewById(R.id.resultButton);
        resultsButton.setOnClickListener(v -> replaceFragment(new ResultFragment()));
        user = auth.getCurrentUser();
        if(user==null){
            replaceFragment(new LoginFragment());
        }else {
            textView.setText("Email: " + user.getEmail());
        }

        logoutButton.setOnClickListener(v-> {
            FirebaseAuth.getInstance().signOut();
            replaceFragment(new LoginFragment());
        });

        return view;
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container_view,fragment).commit();
    }


}
