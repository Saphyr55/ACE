package org.utours.ace.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.utours.ace.R;


public class ErrorFragment extends Fragment {

    private String errorMessage;

    public ErrorFragment(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        var view = inflater.inflate(R.layout.fragment_error, container, false);

        TextView errorTextView = view.findViewById(R.id.text_error);
        errorTextView.setText(errorMessage);

        return view;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}