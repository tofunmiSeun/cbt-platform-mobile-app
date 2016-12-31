package com.unilorin.vividmotion.pre_cbtapp.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.models.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView emailAddressTextView = (TextView) view.findViewById(R.id.emailTextView);
        TextView phoneNumberTextView = (TextView) view.findViewById(R.id.phoneNumberTextView);
        TextView facultyTextView = (TextView) view.findViewById(R.id.facultyTextView);
        TextView departmentTextView = (TextView) view.findViewById(R.id.departmentTextView);
        TextView levelTextView = (TextView) view.findViewById(R.id.levelTextView);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
        User currentUser = new Gson().fromJson(sharedPreferences.getString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, null), User.class);

        nameTextView.setText(currentUser.getName());
        emailAddressTextView.setText(currentUser.getEmailAddress());

        facultyTextView.setText(currentUser.getStudentProfile().getFaculty().getName());
        departmentTextView.setText(currentUser.getStudentProfile().getDepartment().getName());
        levelTextView.setText(getLevelString(currentUser.getStudentProfile().getNumericalValueOfStudentLevel()));

        return view;
    }

    private String getLevelString(int level){
        return level + "00";
    }

}
