package com.example.quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.adapters.ResultAdapter;
import com.example.quiz.models.UserResult;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ResultFragment extends Fragment {

    RecyclerView recyclerView;
    ResultAdapter resultAdapter;
    Button back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_fragment, container, false);
        recyclerView = view.findViewById(R.id.rv);
        back = view.findViewById(R.id.backRes);
        back.setOnClickListener(v-> replaceFragment(new MenuFragment()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<UserResult> options =
                new FirebaseRecyclerOptions.Builder<UserResult>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), UserResult.class)
                        .build();
        resultAdapter = new ResultAdapter(options);
        recyclerView.setAdapter(resultAdapter);
        return view;
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container_view,fragment).commit();
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//    }
    @Override
    public void onStart() {
        super.onStart();
        resultAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        resultAdapter.stopListening();
    }


}
