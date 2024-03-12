package com.example.quiz.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quiz.R;
import com.example.quiz.models.Option;
import com.example.quiz.models.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class QuestionFragment extends Fragment {
    TextView questionText;
    Button optionOneText, optionTwoText, optionThreeText, optionFourText;
    int numberOfQuestion = 0;
    int resultPoints;
    Button buttonNext;
    Button toMenuButton;
    List<Button> list = new ArrayList<>();
    List<Question> questionList = new ArrayList<>();
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_fragment, container, false);
        questionText = view.findViewById(R.id.question);
        optionOneText = view.findViewById(R.id.v1);
        optionTwoText = view.findViewById(R.id.v2);
        optionThreeText = view.findViewById(R.id.v3);
        optionFourText = view.findViewById(R.id.v4);
        buttonNext = view.findViewById(R.id.nextQuestion);
        toMenuButton = view.findViewById(R.id.toMenu);
        toMenuButton.setOnClickListener(v -> replaceFragment(new MenuFragment()));
//        addData();
//        setData(numberOfQuestion);

        readData();
        setOnClickTrue();
        buttonNext.setOnClickListener(v -> {

                    if (numberOfQuestion == questionList.size() - 1) {
                        replaceFragment(new CurrentResultFragment(resultPoints));
                    } else {
                        for (Button b : list) {
                            b.setBackgroundColor(0x5ED8E8);
                        }
                        numberOfQuestion++;
                        setData(numberOfQuestion);
                        setOnClickTrue();
                    }
                }
        );
        return view;
    }

    private void onClickAction(Button button) {
        for (Option o : questionList.get(numberOfQuestion).getOptions()) {
            if (button.getText().equals(o.getAnswer())) {
                if (o.isRight()) {
                    button.setBackgroundColor(0xFF00FF00);
                    resultPoints++;
                } else {
                    button.setBackgroundColor(Color.rgb(255, 0, 0));
                }
            }
            for (Button b : list) {
                if (b.getText().equals(o.getAnswer())) {
                    if (o.isRight()) {
                        b.setBackgroundColor(0xFF00FF00);
                    }
                }
            }
        }
    }

    private void readData() {
        reference = FirebaseDatabase.getInstance().getReference("Questions");

        for (int i = 1; i < 11; i++) {
            reference.child("q"+i).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
//                            questionList.add((Question) task.getResult().getValue());

                            DataSnapshot dataSnapshot = task.getResult();
                            String questionText = String.valueOf(dataSnapshot.child("qText").getValue());
//                            List<Option> optionList = (List<Option>) dataSnapshot.child("Options").getValue();
                            List<Option> optionList = new ArrayList<>();
                            for (int j = 1; j < 5; j++) {
//                                System.out.println(Objects.requireNonNull(dataSnapshot.child("Options").child("o" + j).getValue()).toString());
                                Option option = new Option();
                                option.setAnswer(String.valueOf(dataSnapshot.child("Options").child("o"+j).child("answer").getValue()));
                                option.setRight((boolean) dataSnapshot.child("Options").child("o"+j).child("isRight").getValue());
//                                optionList.add((Option) dataSnapshot.child("Options").child("o" + j).getValue());
                                optionList.add(option);
                            }
                            Question question = new Question();
                            question.setqText(questionText);
                            question.setOptions(optionList);
                            questionList.add(question);
                            setData(numberOfQuestion);

                        } else {
                            Toast.makeText(getContext(), "Fail to read question", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Fail to read", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        list.add(optionOneText);
        list.add(optionTwoText);
        list.add(optionThreeText);
        list.add(optionFourText);
    }

    private void addData() {
        Question question1 = new Question();
        question1.setqText("Как дела слабаки 2?");
        Option optionOne1 = new Option();
        optionOne1.setAnswer("Нормально 2");
        optionOne1.setRight(false);
        Option optionTwo1 = new Option();
        optionTwo1.setAnswer("Круто 2");
        optionTwo1.setRight(true);
        Option optionThree1 = new Option();
        optionThree1.setAnswer("Круто 2");
        optionThree1.setRight(false);
        Option optionFour1 = new Option();
        optionFour1.setAnswer("Круто 2");
        optionFour1.setRight(false);
        List<Option> list1 = Arrays.asList(optionOne1, optionTwo1,optionThree1,optionFour1);
        question1.setOptions(Arrays.asList(optionOne1, optionTwo1,optionThree1,optionFour1));
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference("Questions");
        for (int i = 7; i < 11; i++) {
            databaseReference.child("q"+i).child("qText").setValue(question1.getqText());
            for (int j = 1; j < 5; j++) {
                databaseReference.child("q"+i).child("Options").child("o"+j).child("answer").setValue(list1.get(j-1).getAnswer());
                databaseReference.child("q"+i).child("Options").child("o"+j).child("isRight").setValue(list1.get(j-1).isRight());
            }
        }
    }

    private void setData(int number) {
        questionText.setText(questionList.get(number).getqText());
        optionOneText.setText(questionList.get(number).getOptions().get(0).getAnswer());
        optionTwoText.setText(questionList.get(number).getOptions().get(1).getAnswer());
        optionThreeText.setText(questionList.get(number).getOptions().get(2).getAnswer());
        optionFourText.setText(questionList.get(number).getOptions().get(3).getAnswer());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container_view, fragment).commit();
    }

    private void setOnClickTrue() {
        optionOneText.setOnClickListener(v -> {
            onClickAction(optionOneText);
            setOnClickFalse();
        });
        optionTwoText.setOnClickListener(v -> {
            onClickAction(optionTwoText);
            setOnClickFalse();

        });
        optionThreeText.setOnClickListener(v -> {
            onClickAction(optionThreeText);
            setOnClickFalse();

        });
        optionFourText.setOnClickListener(v -> {
            onClickAction(optionFourText);
            setOnClickFalse();

        });
    }

    private void setOnClickFalse() {
        optionTwoText.setOnClickListener(null);
        optionOneText.setOnClickListener(null);
        optionThreeText.setOnClickListener(null);
        optionFourText.setOnClickListener(null);
    }


}
