package org.utours.ace.game;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.utours.ace.R;
import org.utours.ace.common.Selectable;
import org.utours.ace.game.use_case.PlayerOnPressCardViewBoard;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.port.PlayingTurn;
import org.utours.ace.game.use_case.BotGamePlayingTurn;
import org.utours.ace.game.use_case.CardViewSelectable;
import org.utours.ace.game.use_case.EmplaceCardOnBoardProxy;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameFragment extends Fragment {

    private NavigationActivity navigationActivity;
    private PlayerAccountService playerAccountService;
    private CardService cardService;
    private Map<String, ImageCard> imageCardsReferences;
    private Selectable<View> selectedEmptyCardView;
    private Selectable<View> selectedCardView;
    private GameParty gameParty;
    private GameService gameService;
    private PlayingTurn playingTurn;
    private OnPressViewBoard onPressEmptyViewBoard;
    private OnPressViewBoard onPressViewBoard;

    public GameFragment() { }

    public static GameFragment newInstance(NavigationActivity navigationActivity,
                                           CardService cardService,
                                           PlayerAccountService playerAccountService) {

        Deck deck = playerAccountService.currentPlayerAccount().deck();
        List<Card> cards = cardService.getCardsStochastic(deck.cards(), Player.HAND_CAPACITY, true);

        Player player = new Player("1", List.copyOf(cards));
        Player opponent = new Player("2", List.copyOf(cards));

        GameFragment gameFragment = new GameFragment();
        gameFragment.cardService = cardService;
        gameFragment.navigationActivity = navigationActivity;
        gameFragment.playerAccountService = playerAccountService;
        gameFragment.gameService = new GameService(cardService);
        gameFragment.gameParty = GameParty.Builder.newInstance()
                .withPlayer(player)
                .withOpponent(opponent)
                .withCurrentPlayer(player)
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        selectedCardView = new CardViewSelectable(
                v -> startRotation(v, 20.0f),
                v -> startRotation(v, 0.0f)
        );

        selectedEmptyCardView = new CardViewSelectable(
                v -> startDefaultObjectAnimatorTransY(v, -50.0f),
                v -> startDefaultObjectAnimatorTransY(v, 0.0f)
        );

        LinearLayout boardLayout = view.findViewById(R.id.layout_board);

        // Player
        LinearLayout playerCardsLayout = view.findViewById(R.id.layout_playerCards);
        GridLayout playerBoardLayout = boardLayout.findViewById(R.id.layout_playerBoard);
        setOnClickBindCardView(playerBoardLayout);
        var playerCardViews = mapCardsLayout(gameParty.player(), playerCardsLayout);

        // Opponent
        LinearLayout opponentCardsLayout = view.findViewById(R.id.layout_opponentCards);
        GridLayout opponentBoardLayout = boardLayout.findViewById(R.id.layout_opponentBoard);
        var opponentCardView = mapCardsLayout(gameParty.opponent(), opponentCardsLayout);

        // Binding animation card.
        playerCardViews.forEach((v, card) -> v.setOnClickListener(selectedEmptyCardView::bind));

        // Bot playing turn
        playingTurn = new BotGamePlayingTurn(gameService,
                opponentCardView,
                playerCardViews,
                opponentBoardLayout,
                this::getImageCardByCard);

        onPressViewBoard = new PlayerOnPressCardViewBoard(
                playerCardViews,
                opponentCardView,
                this::getImageCardByCard
        );

        // On pressed board to place a card.
        onPressEmptyViewBoard = new EmplaceCardOnBoardProxy(gameService,
                playerCardViews,
                opponentCardView,
                this::getImageCardByCard);
    }

    private void setOnClickBindCardView(GridLayout layout) {
        IntStream.range(0, layout.getChildCount())
                .forEach(i -> {
                    var child = layout.getChildAt(i);
                    child.setOnClickListener(this::onPressedEmptyCardBoard);
                });
    }

    private void onPressedCardBoard(View v) {
        if (gameParty.isPlayerTurn()) {
            gameParty = onPressViewBoard.onPress(gameParty, selectedCardView.getValue(), v);
        }
        if (gameParty.isOpponentTurn()) {
            gameParty = gameService.botTurn(playingTurn, gameParty);
        }
    }

    private void onPressedEmptyCardBoard(View v) {
        if (gameParty.isPlayerTurn()) {
            gameParty = onPressEmptyViewBoard.onPress(gameParty, selectedEmptyCardView.getValue(), v);
        }
        if (gameParty.isOpponentTurn()) {
            gameParty = gameService.botTurn(playingTurn, gameParty);
        }
    }

    private void provideImages() {
        cardService
                .provideImageCards(gameParty.player().hand())
                .thenAccept(this::setImageCardsReferences);
    }

    private void setImageCardsReferences(List<ImageCard> imageCards) {
        List<Card> hand = gameParty.player().hand();
        imageCardsReferences = cardService.getMapReferences(hand, imageCards);
    }

    private Map<View, Card> mapCardsLayout(Player player, LinearLayout layout) {
        return IntStream.range(0, layout.getChildCount())
                .mapToObj(i -> bindCardLayout(player, layout, i))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<View, Card> bindCardLayout(Player player, LinearLayout layout, int index) {
        Card card = player.hand().get(index);
        View view = layout.getChildAt(index);
        PlayerCardView playerCardView = new PlayerCardView(view);
        playerCardView.bind(card, getImageCardByCard(card));
        return Map.entry(view, card);
    }

    private void startDefaultObjectAnimatorTransY(View view, float value) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY", value);
        animation.setDuration(200);
        animation.start();
    }

    private void startRotation(View view, float value) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotation", value);
        animation.setDuration(200);
        animation.start();
    }

    private ImageCard getImageCardByCard(Card card) {
        ImageCard imageCard = imageCardsReferences.get(card.pictureId());
        assert imageCard != null;
        return imageCard;
    }



}
