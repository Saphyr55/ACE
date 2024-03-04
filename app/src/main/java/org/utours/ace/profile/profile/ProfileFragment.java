package org.utours.ace.profile.profile;

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



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


}