package com.unilorin.vividmotion.pre_cbtapp.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.Course;
import com.unilorin.vividmotion.pre_cbtapp.network.services.HTTPCourseService;
import com.unilorin.vividmotion.pre_cbtapp.views.adapters.AddCourseRecyclerViewAdapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCourseSelectedListener}
 * interface.
 */
public class AddCourseFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static String TAG = "AddCourseFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private String mSearchTerm;
    private RecyclerView mRecyclerView;
    private List<Course> mItems;
    private AddCourseRecyclerViewAdapter mAdapter;
    private OnCourseSelectedListener mListener;

    RelativeLayout progressLayout;
    RelativeLayout noCoursesLayout;
    Button refreshButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AddCourseFragment() {}

    @SuppressWarnings("unused")
    public static AddCourseFragment newInstance(int columnCount) {
        AddCourseFragment fragment = new AddCourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCourseSelectedListener) {
            mListener = (OnCourseSelectedListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCourseSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        // Set the adapter
        if (view.findViewById(R.id.list) instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
            else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }

        progressLayout = (RelativeLayout) view.findViewById(R.id.progressLayout);
        noCoursesLayout = (RelativeLayout) view.findViewById(R.id.noCoursesLayout);
        refreshButton = (Button) view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart();
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        new LoadAvailableCoursesTask().execute();
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link Activity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_course_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(newText);
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCourseSelectedListener {
        // TODO: Update argument type and name
        void onCourseSelected(Course item);
    }

    private class LoadAvailableCoursesTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            progressLayout.setVisibility(View.VISIBLE);
            noCoursesLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            HTTPCourseService courseService = new HTTPCourseService(getActivity().getApplicationContext());
            mItems = courseService.getAvailableCourses();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressLayout.setVisibility(View.GONE);

            if (mItems != null && mItems.size() > 0){
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter = new AddCourseRecyclerViewAdapter(mItems, mListener);
                mRecyclerView.setAdapter(mAdapter);
            }else{
                noCoursesLayout.setVisibility(View.VISIBLE);
            }

        }
    }
}
