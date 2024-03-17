package org.utours.ace.profile.gacha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;

import org.utours.ace.R;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.dialog.NotEnoughTicketDialog;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.GachaService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GachaFragment extends Fragment {

    public static final int MAX_CARD_CAROUSEL = 5;

    private final CardService cardService;
    private final GachaService gachaService;
    private final NavigationActivity navigationActivity;
    private Map<String, ImageCard> imageCards;
    private List<Card> showcaseCards;
    private PullConfirmationDialog singlePullConfirmationDialog;
    private PullConfirmationDialog multiPullConfirmationDialog;

    public GachaFragment(CardService cardService,
                         GachaService gachaService,
                         NavigationActivity navigationActivity) {

        this.cardService = cardService;
        this.gachaService = gachaService;
        this.navigationActivity = navigationActivity;
        this.showcaseCards = new ArrayList<>();
        this.imageCards = new HashMap<>();
    }

    public CompletableFuture<Void> setup() {
        return CompletableFuture
                .runAsync(this::onSetup)
                .thenAccept(unused -> provideImages());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gacha, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Setup
        Context context = view.getContext();
        RecyclerView carouselTopCards = view.findViewById(R.id.carousel_top_cards);
        Button singlePullButton = view.findViewById(R.id.btn_single_pull);
        Button multiPullButton = view.findViewById(R.id.btn_multi_pull);

        // Manage carousel.
        GachaCarouselAdapter gachaCarouselAdapter = new GachaCarouselAdapter(showcaseCards, imageCards);
        CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(new MultiBrowseCarouselStrategy());
        CarouselSnapHelper carouselSnapHelper = new CarouselSnapHelper();

        carouselSnapHelper.attachToRecyclerView(carouselTopCards);
        carouselTopCards.setLayoutManager(carouselLayoutManager);
        carouselTopCards.setAdapter(gachaCarouselAdapter);

        // Manage pull confirmation.
        multiPullConfirmationDialog = new PullConfirmationDialog(context);
        singlePullConfirmationDialog = new PullConfirmationDialog(context);

        onPressPullDialogButton(context, multiPullConfirmationDialog, GachaService.MULTI_QUANTITY);
        onPressPullDialogButton(context, singlePullConfirmationDialog, GachaService.SINGLE_QUANTITY);

        singlePullButton.setOnClickListener(v -> singlePullConfirmationDialog.show());
        multiPullButton.setOnClickListener(v -> multiPullConfirmationDialog.show());
    }

    private void onSetup() {
        this.showcaseCards = cardService.stochasticCards(MAX_CARD_CAROUSEL, true);
    }

    private void onPressPullDialogButton(Context context, PullConfirmationDialog dialog, int quantity) {
        dialog.getPullButton().setOnClickListener(v -> {
            dialog.dismiss();
            startPullingTransaction(context, quantity);
        });
    }

    private void onNotEnoughTicket(Context context) {
        navigationActivity.runOnUiThread(() -> {
            NotEnoughTicketDialog notEnoughTicketDialog = new NotEnoughTicketDialog(context, navigationActivity);
            notEnoughTicketDialog.show();
        });
    }

    private void startPullingTransaction(Context context, int quantity) {
        CompletableFuture
                .supplyAsync(() -> gachaService.pullCards(quantity))
                .thenApply(r -> r.mapValue(cards -> {
                    GachaPullActivity.setCards(cards);
                    return cardService.provideImageCards(cards);
                }))
                .thenCompose(r -> {
                    if (r.isError()) {
                        onNotEnoughTicket(context);
                        return CompletableFuture.completedFuture(null);
                    }
                    return r.getValue();
                })
                .thenAccept(imageCards -> {
                    if (imageCards == null) return;
                    GachaPullActivity.setImageCards(imageCards);
                    launchPullingActivity(context);
                })
                .exceptionally(throwable -> {
                    Log.e("PULL EXCEPTION", throwable.getMessage(), throwable);
                    return null;
                });
    }

    private void launchPullingActivity(Context context) {
        Intent intent = new Intent(context, GachaPullActivity.class);
        startActivity(intent);
    }

    private void fillImageCardsReferences(List<ImageCard> refs) {
        imageCards = cardService.getMapReferences(showcaseCards, refs);
    }

    private void provideImages() {
        cardService
                .provideImageCards(showcaseCards)
                .thenAccept(this::fillImageCardsReferences);
    }
}