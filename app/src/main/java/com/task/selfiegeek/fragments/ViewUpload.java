package com.task.selfiegeek.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task.selfiegeek.R;
import com.task.selfiegeek.activity.MainActivity;
import com.task.selfiegeek.adapter.ImageAdapter;
import com.task.selfiegeek.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUpload extends Fragment {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<String> imlocs;
    private File directory;
    private ImageAdapter imageAdapter;

    public ViewUpload() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_upload, container, false);
        imlocs = new ArrayList<>();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        directory = new File(Constants.imgLoc);
        if (directory.exists()) {
            File f[] = directory.listFiles();
            for (int i = 0; i < f.length; i++)
                imlocs.add(f[i].getPath());
        } else {

        }
        imageAdapter = new ImageAdapter(getActivity(), imlocs);
        recyclerView.setAdapter(imageAdapter);
        return view;
    }

    public void update() {
        if (directory.exists()) {
            File f[] = directory.listFiles();
            if (f.length > imlocs.size()) {
                for (int i = imlocs.size(); i < f.length; i++)
                    imlocs.add(f[i].getPath());
            }
            imageAdapter.notifyDataSetChanged();
        }

    }

}
