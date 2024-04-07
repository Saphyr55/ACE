package org.utours.ace.profile.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.dialog.CustomizeProfileDialog;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.ImageCardReference;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class ProfileFragment extends Fragment {

    private NavigationActivity navigationActivity;
    private PlayerAccountService accountService;
    private CardService cardService;
    private ImageCardReference imageCardReference;
    private View viewFragment;

    public ProfileFragment(){

    }

    public static ProfileFragment newInstance(NavigationActivity navigationActivity, CardService cardService, PlayerAccountService accountService) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.accountService = accountService;
        fragment.cardService = cardService;
        fragment.navigationActivity = navigationActivity;
        fragment.imageCardReference = ImageCardReference.create(cardService);


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
        viewFragment = view;
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

        customizeButton.setOnClickListener(v -> onCustomizeButton(navigationActivity,descriptionText) );

    }

    private void onCustomizeButton(NavigationActivity navigationActivity, TextView descriptionText) {

        CustomizeProfileDialog customizeProfileDialog = new CustomizeProfileDialog(navigationActivity,accountService, cardService, imageCardReference, (cardView, card) -> {
            refresh(descriptionText, cardView, card);
        });
        customizeProfileDialog.show();

    }

    public void refresh(TextView descriptionText, CardView cardView, Card card) {

        String description = String.valueOf(accountService.currentPlayerAccount().description());
        descriptionText.setText(description);
        if(cardView == null)
            return;
        CardView userPictureCardView = viewFragment.findViewById(R.id.profile_user_picture);
        ImageView userPictureImageview = userPictureCardView.findViewById(R.id.image_card);

        ImageCard imageCard = imageCardReference.get(card);
        assert imageCard != null;
        byte[] content = imageCard.content();
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);
        userPictureImageview.setImageBitmap(bitmap);

    }

    public CompletableFuture<Void> setup() {
        return imageCardReference
                .provideImageCards(accountService.currentPlayerAccount().cards())
                .thenAccept(stringImageCardMap -> {});
    }

    public void changeView (Bitmap bitmap){
        CardView userPictureCardView = viewFragment.findViewById(R.id.profile_user_picture);
        ImageView userPictureImageView = userPictureCardView.findViewById(R.id.image_card);
        userPictureImageView.setImageBitmap(bitmap);

    }

    public Bitmap loadFromUri(Uri uri) {
        Bitmap bitmap = null;

        try {
            ImageDecoder.Source source = ImageDecoder.createSource(navigationActivity.getContentResolver(), uri);
            bitmap = ImageDecoder.decodeBitmap(source);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}