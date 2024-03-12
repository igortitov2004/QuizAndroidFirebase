package com.example.quiz.fragments;

import android.os.Bundle;
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

public class LoginFragment extends Fragment {
    private Button loginButton, registerButton;
    private EditText eTLogin, eTPass;

    FirebaseAuth mAuth;
    ProgressBar progressBar;

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        loginButton = view.findViewById(R.id.enter);
        registerButton = view.findViewById(R.id.register);
        eTLogin = view.findViewById(R.id.loginAuth);
        eTPass = view.findViewById(R.id.passwordAuth);
        progressBar = view.findViewById(R.id.progressBar);
        registerButton.setOnClickListener(v -> replaceFragment(new RegistrationFragment()));
        loginButton.setOnClickListener(v -> {

            String login, pass;
            login = String.valueOf(eTLogin.getText());
            pass = String.valueOf(eTPass.getText());
            if (login.isEmpty()) {
                Toast.makeText(getContext(), "Введите логин", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.isEmpty()) {
                Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Login successful.",
                                        Toast.LENGTH_SHORT).show();
                                replaceFragment(new MenuFragment());
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

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container_view, fragment).commit();
    }
}
