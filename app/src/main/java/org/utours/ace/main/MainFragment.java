package org.utours.ace.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.utours.ace.R;
import org.utours.ace.game.GameFragment;
import org.utours.ace.main.dialog.DeckNotCompleteDialog;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;

public class MainFragment extends Fragment {

    private CardService cardService;
    private PlayerAccountService playerAccountService;
    private NavigationActivity navActivity;
    private Context context;

    private MainFragment() { }

    public static MainFragment newInstance(PlayerAccountService accountService,
                                           CardService cardService,
                                           NavigationActivity navigationActivity) {

        MainFragment mainFragment = new MainFragment();
        mainFragment.cardService = cardService;
        mainFragment.playerAccountService = accountService;
        mainFragment.navActivity = navigationActivity;
        mainFragment.context = navigationActivity;

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

        if (playerAccountService.currentPlayerAccount().deck().cards().size() < 5) {
            DeckNotCompleteDialog.createAndShow(context, navActivity);
            return;
        }

        navActivity.fullyReplaceFragment(new WaitFragment());
        GameFragment gameFragment = GameFragment.newInstance(navActivity, cardService, playerAccountService);
        gameFragment.setup().thenRun(() -> navActivity.fullyReplaceFragment(gameFragment));
    }


}
