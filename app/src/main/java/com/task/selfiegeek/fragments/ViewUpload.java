package com.task.selfiegeek.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task.selfiegeek.R;
import com.task.selfiegeek.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUpload extends Fragment {

    private Toolbar toolbar;

    public ViewUpload() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_upload, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        return view;
    }
}
