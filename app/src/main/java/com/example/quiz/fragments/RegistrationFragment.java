package com.example.quiz.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationFragment extends Fragment {
    private Button backButton,registerButton;
    private EditText eTLogin,eTPass;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            replaceFragment(new MenuFragment());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        backButton = view.findViewById(R.id.back);
        eTLogin = view.findViewById(R.id.loginRegister);
        eTPass = view.findViewById(R.id.passwordRegister);
        registerButton = view.findViewById(R.id.completeRegister);
        progressBar = view.findViewById(R.id.progressBar);
        backButton.setOnClickListener(v -> replaceFragment(new LoginFragment()));
        registerButton.setOnClickListener(v -> {

            String login,pass;
            login = String.valueOf(eTLogin.getText());
            pass = String.valueOf(eTPass.getText());
            if(login.isEmpty()){
                Toast.makeText(getContext(),"Введите логин",Toast.LENGTH_SHORT).show();
                return;
            }
            if(pass.isEmpty()){
                Toast.makeText(getContext(),"Введите пароль",Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Account created",
                                        Toast.LENGTH_SHORT).show();
                                replaceFragment(new LoginFragment());
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        return view;
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container_view,fragment).commit();
    }
}
