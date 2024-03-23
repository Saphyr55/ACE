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
import org.utours.ace.game.base.SlotCardSelectable;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.game.base.CardSelectable;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.use_case.ActiveSkillViewTreatment;
import org.utours.ace.game.use_case.EmplaceCardOnBoardViewTreatment;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.ImageCard;

import java.util.ArrayList;
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
    private Selectable<Integer> selectedCardSlotView;
    private Selectable<Integer> selectedEmptySlotView;
    private Selectable<Integer> selectedHandCardView;
    private GameParty gameParty;
    private GameService gameService;
    private List<View> playerCardViews;
    private List<View> opponentCardView;
    private List<View> playerBoard;

    public static GameFragment newInstance(NavigationActivity navigationActivity,
                                           CardService cardService,
                                           PlayerAccountService playerAccountService) {

        GameFragment gameFragment = new GameFragment();
        gameFragment.navigationActivity = navigationActivity;
        gameFragment.playerAccountService = playerAccountService;
        gameFragment.cardService = cardService;
        gameFragment.gameService = new GameService(cardService);
        gameFragment.playerBoard = new ArrayList<>(GameParty.MAX_SLOT);

        return gameFragment;
    }

    public CompletableFuture<Void> setup() {
        return CompletableFuture
                .runAsync(this::onSetup)
                .thenRun(this::provideImages);
    }

    private void onSetup() {

        Deck deck = playerAccountService.currentPlayerAccount().deck();
        List<Card> cards = cardService.getCardsStochastic(deck.cards(), Player.HAND_CAPACITY, true);

        Player player = Player.Builder.of()
                .withPlayerID("1")
                .withHand(List.copyOf(cards))
                .build();

        Player opponent = Player.Builder.of()
                .withPlayerID("2")
                .withHand(List.copyOf(cards))
                .build();

        gameParty = GameParty.Builder.newInstance()
                .withPlayer(player)
                .withOpponent(opponent)
                .withCurrentPlayer(player)
                .build();

        selectedHandCardView = new CardSelectable<>(
                i -> {
                    selectedCardSlotView.unselect();
                    startDefaultObjectAnimatorTransY(playerCardViews.get(i), -50.0f);
                },
                i -> startDefaultObjectAnimatorTransY(playerCardViews.get(i), 0.0f));

        selectedCardSlotView = new SlotCardSelectable(playerBoard);
        selectedEmptySlotView = new CardSelectable<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        LinearLayout boardLayout = view.findViewById(R.id.layout_board);

        // Player
        LinearLayout playerCardsLayout = view.findViewById(R.id.layout_playerCards);
        GridLayout playerBoardLayout = boardLayout.findViewById(R.id.layout_playerBoard);
        bindPlayerBoard(playerBoardLayout);
        playerCardViews = mapCardsLayout(gameParty.player(), playerCardsLayout);

        // Binding animation card.
        playerCardViews.forEach(v -> v.setOnClickListener(unused ->
                selectedHandCardView.bind(playerCardViews.lastIndexOf(v))));

        // Opponent
        LinearLayout opponentCardsLayout = view.findViewById(R.id.layout_opponentCards);
        GridLayout opponentBoardLayout = boardLayout.findViewById(R.id.layout_opponentBoard);
        opponentCardView = mapCardsLayout(gameParty.opponent(), opponentCardsLayout);
    }

    private void bindPlayerBoard(GridLayout layout) {
        IntStream.range(0, layout.getChildCount()).forEach(i -> {
            View child = layout.getChildAt(i);
            child.setOnClickListener(this::onPressPlayerSlot);
            playerBoard.add(child);
        });
    }

    private void onPressPlayerSlot(View view) {

        var emptySlotView = view.findViewById(R.id.view_emptySlot);

        if (emptySlotView.getVisibility() == View.VISIBLE) {
            selectedEmptySlotView.select(playerBoard.indexOf(view));
        } else {
            selectedCardSlotView.select(playerBoard.indexOf(view));
        }

        var playerAction = determinePlayerAction();

        if (gameParty.isPlayerTurn()) {
            gameParty = playerAction.apply(gameParty);
        }

        if (gameParty.isOpponentTurn()) {
            gameParty = gameService.botAction(playerAction, gameParty);
        }

        selectedEmptySlotView.unselect();
        selectedHandCardView.unselect();
    }

    private PlayerAction determinePlayerAction() {
        if (selectedHandCardView.isSelect() && selectedEmptySlotView.isSelect()) {
            int cardPos = selectedHandCardView.getValue();
            int slotPos = selectedEmptySlotView.getValue();
            return new EmplaceCardOnBoardViewTreatment(
                    playerCardViews, playerBoard,
                    this::getImageCardByCard,
                    cardPos, slotPos);
        } else if (selectedCardSlotView.isSelect()) {
            return new ActiveSkillViewTreatment(navigationActivity);
        }
        return PlayerAction.noAction();
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

    private List<View> mapCardsLayout(Player player, LinearLayout layout) {
        return IntStream.range(0, layout.getChildCount())
                .mapToObj(i -> bindCardLayout(player, layout, i))
                .collect(Collectors.toList());
    }

    private View bindCardLayout(Player player, LinearLayout layout, int index) {
        Card card = player.hand().get(index);
        View view = layout.getChildAt(index);
        PlayerCardView playerCardView = new PlayerCardView(view);
        playerCardView.bind(card, getImageCardByCard(card));
        return view;
    }

    private void startDefaultObjectAnimatorTransY(View view, float value) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY", value);
        animation.setDuration(200);
        animation.start();
    }


    private ImageCard getImageCardByCard(Card card) {
        ImageCard imageCard = imageCardsReferences.get(card.pictureId());
        assert imageCard != null;
        return imageCard;
    }



}
