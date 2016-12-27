package com.unilorin.vividmotion.pre_cbtapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;

import java.util.List;

import static com.unilorin.vividmotion.pre_cbtapp.fragments.AddCourseFragment.OnCourseSelectedListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnCourseSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AddCourseRecyclerViewAdapter extends RecyclerView.Adapter<AddCourseRecyclerViewAdapter.ViewHolder> {

    private final List<Course> mValues;
    private final List<Course> mValuesClone;
    private final OnCourseSelectedListener mListener;

    public AddCourseRecyclerViewAdapter(List<Course> items, OnCourseSelectedListener listener) {
        mValues = items;
        mValuesClone = mValues;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCourseCodeView.setText(holder.mItem.getCourseCode());
        holder.mCourseTitleView.setText(holder.mItem.getCourseTitle());
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCourseSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCourseCodeView;
        public final TextView mCourseTitleView;
        public Course mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCourseCodeView = (TextView) view.findViewById(R.id.courseCodeTextView);
            mCourseTitleView = (TextView) view.findViewById(R.id.courseTitleTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCourseTitleView.getText() + "'";
        }
    }
}
