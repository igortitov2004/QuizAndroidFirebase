package com.example.quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CurrentResultFragment extends Fragment {

    int numberOfPoints;

    TextView textView;
    Button buttonToMenu;
    Button buttonSave;
    FirebaseAuth auth;
    FirebaseUser user;
    public CurrentResultFragment(){

    }
    public CurrentResultFragment(int numberOfPoints){
        this.numberOfPoints=numberOfPoints;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_result_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        textView = view.findViewById(R.id.resultText);
        buttonToMenu = view.findViewById(R.id.toMenuButton);
        buttonSave = view.findViewById(R.id.buttonSave);
        if(savedInstanceState!=null) {
            numberOfPoints = savedInstanceState.getInt("result");
        }
        textView.setText("Ваш результат: "+numberOfPoints);
        buttonToMenu.setOnClickListener(v-> replaceFragment(new MenuFragment()));
        buttonSave.setOnClickListener(v-> {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference reference = db.getReference("Users");
            user = auth.getCurrentUser();
            if(user==null){
                replaceFragment(new LoginFragment());
            }else {
                reference.child(user.getUid()).child("result").setValue(numberOfPoints);
                reference.child(user.getUid()).child("username").setValue(user.getEmail());
                replaceFragment(new MenuFragment());
            }
        });
        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container_view, fragment).commit();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState){
        saveInstanceState.putInt("result",numberOfPoints);
        super.onSaveInstanceState(saveInstanceState);
    }
}
