package com.unilorin.vividmotion.pre_cbtapp.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;

import java.util.ArrayList;
import java.util.List;

import static com.unilorin.vividmotion.pre_cbtapp.fragments.CourseQuizFragment.OnCourseQuizSelectedListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnCourseQuizSelectedListener}.
 */
public class CourseQuizRecyclerViewAdapter extends RecyclerView.Adapter<CourseQuizRecyclerViewAdapter.ViewHolder> {

    private final List<Course> mValues;
    private final OnCourseQuizSelectedListener mListener;
    private final List<Course> mValuesClone = new ArrayList<>();

    public CourseQuizRecyclerViewAdapter(List<Course> items, OnCourseQuizSelectedListener listener) {
        mValues = items;
        mValuesClone.addAll(mValues);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.take_quiz_courses_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCourseCodeView.setText(holder.mItem.getCourseCode());
        //holder.mCourseTitleView.setText(holder.mItem.getCourseTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCourseQuizSelected(holder.mItem);
                }
            }
        });
    }

    public void filter(String newText) {
        mValues.clear();
        if(newText.isEmpty()){
            mValues.addAll(mValuesClone);
        } else{
            newText = newText.toLowerCase();
            for(Course course: mValuesClone){
                if(course.getCourseCode().toLowerCase().contains(newText) || course.getCourseTitle().toLowerCase().contains(newText)){
                    mValues.add(course);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCourseCodeView;
        //public final TextView mCourseTitleView;
        public Course mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCourseCodeView = (TextView) view.findViewById(R.id.courseCodeTextView);
        //    mCourseTitleView = (TextView) view.findViewById(R.id.courseTitleTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCourseCodeView.getText() + "'";
        }
    }
}
