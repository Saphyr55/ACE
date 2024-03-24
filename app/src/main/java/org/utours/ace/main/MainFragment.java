package org.utours.ace.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.game.GameFragment;
import org.utours.ace.infra.profile.PlayerAccountContainer;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;

public class MainFragment extends Fragment {


    private NavigationActivity navActivity;

    private MainFragment() { }

    public static MainFragment newInstance(NavigationActivity navigationActivity) {
        MainFragment mainFragment = new MainFragment();
        mainFragment.navActivity = navigationActivity;
        return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView textView = view.findViewById(R.id.text_findParty);
        textView.setOnClickListener(v -> onPressFindParty());
    }

    private void onPressFindParty() {
        navActivity.fullyReplaceFragment(new WaitFragment());
        CardService cardService = PlayerAccountContainer.INSTANCE.getCardService(navActivity);
        PlayerAccountService playerAccountService = PlayerAccountContainer.INSTANCE.getService();
        GameFragment gameFragment = GameFragment.newInstance(navActivity, cardService, playerAccountService);
        gameFragment.setup().thenRun(() -> navActivity.fullyReplaceFragment(gameFragment));
    }


}
