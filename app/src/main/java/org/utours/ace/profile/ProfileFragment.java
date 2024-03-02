package org.utours.ace.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.utours.ace.R;
import org.utours.ace.auth.ConnectionActivity;
import org.utours.ace.infra.profile.PlayerAccountContainer;


public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getName();


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        var playerAccount = PlayerAccountContainer.INSTANCE
                .getCurrentPlayerAccount()
                .orElseThrow(this::errorMissingCurrentPlayer);

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private RuntimeException errorMissingCurrentPlayer() {
        var msg = "Current player account doesn't exist, and entire in ProfileActivity.";
        var e = new RuntimeException(msg);
        Log.e(TAG, e.getMessage(), e);
        return e;
    }

}