package org.utours.ace.game;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.utours.ace.R;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.ImageCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GameFragment extends Fragment {

    private NavigationActivity navigationActivity;
    private CardService cardService;
    private PlayerAccountService playerAccountService;
    private Map<String, ImageCard> imageCardsReferences;
    private View selectedCardView;
    private Map<View, Card> playerCardViews;
    private Map<View, Card> opponentCardView;
    private GameParty gameParty;


    public GameFragment() { }

    public static GameFragment newInstance(NavigationActivity navigationActivity,
                                           CardService cardService,
                                           PlayerAccountService playerAccountService) {

        Deck deck = playerAccountService.currentPlayerAccount().deck();
        List<Card> cards = cardService.getCardsStochastic(deck.cards(), Player.HAND_CAPACITY, false);

        GameFragment gameFragment = new GameFragment();
        gameFragment.cardService = cardService;
        gameFragment.navigationActivity = navigationActivity;
        gameFragment.playerAccountService = playerAccountService;
        gameFragment.gameParty = GameParty.Builder.newInstance()
                .withPlayer(new Player(List.copyOf(cards)))
                .withOpponent(new Player(List.copyOf(cards)))
                .build();

        return gameFragment;
    }

    public CompletableFuture<Void> setup() {
        return CompletableFuture
                .runAsync(this::onSetup)
                .thenRun(this::provideImages);
    }

    private void onSetup() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        LinearLayout playerCardsLayout = view.findViewById(R.id.layout_playerCards);
        LinearLayout opponentCardsLayout = view.findViewById(R.id.layout_opponentCards);
        LinearLayout boardLayout = view.findViewById(R.id.layout_board);

        bindFrontPlayerCards(boardLayout);

        playerCardViews = bindCardsLayout(playerCardsLayout);
        opponentCardView = bindCardsLayout(opponentCardsLayout);

        playerCardViews.forEach((v, card) -> v.setOnClickListener(this::bindAnimationCard));
    }

    private void bindFrontPlayerCards(LinearLayout boardLayout) {
        GridLayout playerBoardLayout = boardLayout.findViewById(R.id.layout_playerBoard);
        int size = playerBoardLayout.getChildCount();
        for (int i = 0; i < size; i++) {
            View view = playerBoardLayout.getChildAt(i);
            view.setOnClickListener(this::onPressedEmptyCardBoard);
        }
    }

    private void onPressedEmptyCardBoard(View v) {
        View emptyView = v.findViewById(R.id.view_emptyGameBoardCard);
        if (checkCanPutCard(emptyView)) {

            FrameLayout frameLayout = v.findViewById(R.id.layout_gameBoardCard);
            PlayerCardView playerCardView = new PlayerCardView(frameLayout);

            Card card = playerCardViews.remove(selectedCardView);
            assert card != null;

            gameParty = gameParty.emplacePlayerCardInBoard(card);

            selectedCardView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            ImageCard imageCard = imageCardsReferences.get(card.pictureId());
            assert imageCard != null;

            playerCardView.bind(card, imageCard);

            selectedCardView = null;
        }
    }

    private boolean checkCanPutCard(View v) {
        return !playerCardViews.isEmpty()
                && selectedCardView != null
                && v.getVisibility() == View.VISIBLE;
    }

    private void provideImages() {
        cardService
                .provideImageCards(gameParty.player().hand())
                .thenAccept(this::setImageCardsReferences);
    }

    private void setImageCardsReferences(List<ImageCard> imageCards) {
        imageCardsReferences = cardService.getMapReferences(gameParty.player().hand(), imageCards);
    }

    private void bindAnimationCard(View view) {
        if (selectedCardView == view || selectedCardView != null) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(
                    selectedCardView, "translationY", 0);
            animation.setDuration(200);
            animation.start();
        }
        if (selectedCardView != view) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(
                    view, "translationY", -50.f);
            animation.setDuration(200);
            animation.start();
            selectedCardView = view;
        } else {
            selectedCardView = null;
        }
    }

    private Map<View, Card> bindCardsLayout(LinearLayout layout) {
        Map<View, Card> views = new HashMap<>();
        for (int i = 0; i < layout.getChildCount(); i++) {

            View view = layout.getChildAt(i);
            Card card = gameParty.player().hand().get(i);
            ImageCard imageCard = imageCardsReferences.get(card.pictureId());
            PlayerCardView playerCardView = new PlayerCardView(view);

            assert imageCard != null;
            views.put(view, card);
            playerCardView.bind(card, imageCard);
        }
        return views;
    }

}
