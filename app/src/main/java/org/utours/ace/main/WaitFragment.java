package org.utours.ace.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.utours.ace.R;


public class WaitFragment extends Fragment {

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wait, container, false);
        progressBar = view.findViewById(R.id.progress_waiting);
        return view;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

}