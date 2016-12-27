package com.unilorin.vividmotion.pre_cbtapp.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;

import java.util.List;

/**
 * Created by Eloka Augustine on 27/12/2016.
 */
public class QuestionsRecyclerViewAdapter extends RecyclerView.Adapter<QuestionsRecyclerViewAdapter.ViewHolder> {
    private List<Question> mDataSet;
    private Context context;

    public QuestionsRecyclerViewAdapter(final List<Question> questions, final Context context) {
        mDataSet = questions;
        this.context = context;
    }

    @Override
    public QuestionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                      final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_question, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Question question = mDataSet.get(position);
        holder.questionNumberTextView.setText(position + 1);
        holder.questionStatementTextView.setText(question.getQuestion());
        holder.questionsOptionsRadioGroup.setOrientation(LinearLayout.VERTICAL);

        for (int i = 1; i <= question.getOptions().length; i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(i);
            radioButton.setText(question.getOptions()[i]);
            holder.questionsOptionsRadioGroup.addView(radioButton);
        }
        // TODO: 27/12/2016 Initialize radiogroup here
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View questionsView;
        public TextView questionNumberTextView;
        public TextView questionStatementTextView;
        public RadioGroup questionsOptionsRadioGroup;

        public ViewHolder(final View view) {
            super(view);
            questionsView = view;
            questionNumberTextView = (TextView) view.findViewById(R.id.questionNumberTextView);
            questionStatementTextView = (TextView) view.findViewById(R.id.questionStatementTextView);
            questionsOptionsRadioGroup = (RadioGroup) view.findViewById(R.id.optionsRadioGroup);
        }
    }
}
