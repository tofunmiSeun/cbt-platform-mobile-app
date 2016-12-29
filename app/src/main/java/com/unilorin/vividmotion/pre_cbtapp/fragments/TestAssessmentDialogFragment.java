package com.unilorin.vividmotion.pre_cbtapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.activities.DashboardActivity;
import com.unilorin.vividmotion.pre_cbtapp.models.TestResult;

/**
 * Created by Tofunmi on 29/12/2016.
 */

public class TestAssessmentDialogFragment extends DialogFragment implements View.OnClickListener{
    private static TestResult testResult;
    private static String courseCode;

    private TextView courseCodeTextView;
    private TextView totalQuestionsTextView;
    private TextView questionsAnsweredTextView;
    private TextView correctAnswersTextView;
    private Button closeDialogButton;
    private Button finishTestButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TestAssessmentDialogFragment getInstance(TestResult result, String code){
        testResult = result;
        courseCode = code;
        return new TestAssessmentDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_test_assessment_dialog, container, false);
        courseCodeTextView = (TextView)v.findViewById(R.id.courseTextView);
        totalQuestionsTextView = (TextView)v.findViewById(R.id.totalQuestionsTextView);
        questionsAnsweredTextView = (TextView)v.findViewById(R.id.questionsAnsweredTextView);
        correctAnswersTextView = (TextView)v.findViewById(R.id.correctAnswersTextView);
        closeDialogButton = (Button) v.findViewById(R.id.closeDialogButton);
        finishTestButton = (Button) v.findViewById(R.id.finishTestButton);

        courseCodeTextView.setText(courseCode);
        totalQuestionsTextView.setText(String.valueOf(testResult.getTotalQuestionsCount()));
        questionsAnsweredTextView.setText(String.valueOf(testResult.getQuestionsAnsweredCount()));
        correctAnswersTextView.setText(String.valueOf(testResult.getCorrectAnswersCount()));
        closeDialogButton.setOnClickListener(this);
        finishTestButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (view == closeDialogButton){
            getDialog().dismiss();
        }
        else if (view == finishTestButton){
            startActivity(new Intent(getActivity(), DashboardActivity.class));
        }
    }
}
