package org.utours.ace.profile.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.profile.dialog.CustomizeProfileDialog;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class ProfileFragment extends Fragment {

    private PlayerAccountService accountService;
    private CardService cardService;
    private Map<String, ImageCard> cardImages;

    public ProfileFragment(){

    }

    public static ProfileFragment newInstance(CardService cardService, PlayerAccountService accountService) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.accountService = accountService;
        fragment.cardService = cardService;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Context context = view.getContext();
        TextView levelNumberText = view.findViewById(R.id.text_level);
        TextView uuidNumberText = view.findViewById(R.id.text_uuid);
        TextView descriptionText = view.findViewById(R.id.text_description);
        TextView usernameText = view.findViewById(R.id.text_username);
        Button customizeButton = view.findViewById(R.id.btnCustomize_profile);

        String level = String.valueOf(accountService.currentPlayerAccount().level());
        String uid = String.valueOf(accountService.currentPlayerAccount().uid());
        String description = String.valueOf(accountService.currentPlayerAccount().description());
        String username = String.valueOf(accountService.currentPlayerAccount().username());

        levelNumberText.setText(level);
        uuidNumberText.setText(uid);
        descriptionText.setText(description);
        usernameText.setText(username);

        customizeButton.setOnClickListener(v -> onCustomizeButton(context,descriptionText) );

    }

    private void onCustomizeButton(Context context, TextView descriptionText) {

        CustomizeProfileDialog customizeProfileDialog = new CustomizeProfileDialog(context,accountService,() -> refresh(descriptionText), cardService, cardImages);
        customizeProfileDialog.show();

    }

    public void refresh(TextView descriptionText) {

        String description = String.valueOf(accountService.currentPlayerAccount().description());

        descriptionText.setText(description);

    }

    public CompletableFuture<Void> setup() {
        return cardService
                .provideImageCards(accountService.currentPlayerAccount().cards())
                .thenAccept(this::setImageCardReferences);
    }

    private void setImageCardReferences(List<ImageCard> imageCards) {
        var cards = List.copyOf(accountService.currentPlayerAccount().cards());
        cardImages = cardService.getMapReferences(cards, imageCards);
    }

}