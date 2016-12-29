package com.unilorin.vividmotion.pre_cbtapp.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eloka Augustine on 27/12/2016.
 */
public class QuestionsRecyclerViewAdapter extends RecyclerView.Adapter<QuestionsRecyclerViewAdapter.ViewHolder> {
    private List<Question> mDataSet;
    private Context context;
    private boolean testFinished;
    private List<OnTestSubmittedListener> listeners;

    interface OnTestSubmittedListener{
        void testSubmitted();
    }


    public QuestionsRecyclerViewAdapter(final List<Question> questions, final Context context) {
        mDataSet = questions;
        this.context = context;
        listeners = new ArrayList<>();
    }

    @Override
    public QuestionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                      final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Question question = mDataSet.get(position);
        holder.questionNumberTextView.setText(String.valueOf(position + 1));
        holder.questionStatementTextView.setText(question.getQuestion());
        holder.questionsOptionsRadioGroup.setOrientation(LinearLayout.VERTICAL);
        holder.correctAnswerIndex = question.getCorrectAnswerIndex();

        for (int i = 0; i < question.getOptions().length; i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(i);
            radioButton.setText(question.getOptions()[i]);
            holder.questionsOptionsRadioGroup.addView(radioButton);
        }

        holder.questionsOptionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("TAG", String.valueOf(i));
                question.setSelectedAnswerIndex(i);
                holder.selectedAnswerIndex = i;
            }
        });

        listeners.add(holder);
        if (testFinished){
            holder.testSubmitted();
        }
    }

    public Map<String, Integer>  mark(){
        for (OnTestSubmittedListener l : listeners){
            l.testSubmitted();
        }

        testFinished = true;

        Map<String, Integer> params = new HashMap<>();
        int correctAnswers = 0;
        int answeredQuestions = 0;
        for (int i = 0; i < mDataSet.size(); i++){
            if (mDataSet.get(i).getSelectedAnswerIndex() != Question.NONE_SELECTED){
                answeredQuestions++;
                if (mDataSet.get(i).getSelectedAnswerIndex() == mDataSet.get(i).getCorrectAnswerIndex()){
                    correctAnswers++;
                }
            }
        }

        params.put("answeredQuestions", answeredQuestions);
        params.put("correctAnswers", correctAnswers);

        return params;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnTestSubmittedListener{
        public View questionsView;
        public TextView questionNumberTextView;
        public TextView questionStatementTextView;
        public RadioGroup questionsOptionsRadioGroup;
        public Integer correctAnswerIndex;
        public Integer selectedAnswerIndex = Question.NONE_SELECTED;

        public ViewHolder(final View view) {
            super(view);
            questionsView = view;
            questionNumberTextView = (TextView) view.findViewById(R.id.questionNumberTextView);
            questionStatementTextView = (TextView) view.findViewById(R.id.questionStatementTextView);
            questionsOptionsRadioGroup = (RadioGroup) view.findViewById(R.id.optionsRadioGroup);
        }

        public void showCorrectAnswer(){
            if (selectedAnswerIndex != Question.NONE_SELECTED) {
                ((RadioButton) questionsOptionsRadioGroup.getChildAt(selectedAnswerIndex))
                        .setTextColor(questionsView.getContext().getResources().getColor(R.color.colorAccent));
            }
            ((RadioButton) questionsOptionsRadioGroup.getChildAt(correctAnswerIndex))
                    .setTextColor(questionsView.getContext().getResources().getColor(R.color.colorCorrectAnswer));
        }

        public void disableQuestionOptionsSelection(){
            for (int i = 0; i < questionsOptionsRadioGroup.getChildCount(); i++){
                questionsOptionsRadioGroup.getChildAt(i).setEnabled(false);
            }
        }

        @Override
        public void testSubmitted() {
            showCorrectAnswer();
            disableQuestionOptionsSelection();
        }
    }
}
