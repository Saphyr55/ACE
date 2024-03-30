package org.utours.ace.game;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.utours.ace.R;
import org.utours.ace.common.Selectable;
import org.utours.ace.game.actions.ActiveSkillViewTreatment;
import org.utours.ace.game.actions.EmplaceCardOnBoardViewTreatment;
import org.utours.ace.common.SelectableValue;
import org.utours.ace.game.actions.OnPullCardViewTreatment;
import org.utours.ace.game.base.SlotCardSelectable;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.GamePartyFinder;
import org.utours.ace.game.port.GamePartyUpdater;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.infra.game.GameContainer;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameFragment extends Fragment {

    private NavigationActivity navigationActivity;
    private PlayerAccountService playerAccountService;
    private CardService cardService;
    private GameService gameService;
    private GamePartyFinder gamePartyFinder;
    private Map<String, ImageCard> imageCardsReferences;
    private Selectable<Integer> firstSelectedCardSlotView;
    private SlotCardSelectable secondSelectedCardSlotView;
    private Selectable<Integer> selectedEmptySlotView;
    private Selectable<Integer> selectedHandCardView;
    private GameParty gameParty;
    private Player player;
    private List<View> playerCardViews;
    private List<View> opponentCardViews;
    private List<View> playerBoard;
    private List<View> opponentBoard;
    private Button endButton;
    private Button pullButton;
    private boolean isSelectedCardOpponent = false;

    public static GameFragment newInstance(NavigationActivity navigationActivity,
                                           CardService cardService,
                                           PlayerAccountService playerAccountService) {

        GameFragment gameFragment = new GameFragment();
        gameFragment.gamePartyFinder = GameContainer.INSTANCE.getGamePartyFinder(navigationActivity);
        gameFragment.navigationActivity = navigationActivity;
        gameFragment.playerAccountService = playerAccountService;
        gameFragment.cardService = cardService;
        gameFragment.gameService = new GameService(cardService);
        gameFragment.playerBoard = new ArrayList<>(GameParty.MAX_SLOT);
        gameFragment.opponentBoard = new ArrayList<>(GameParty.MAX_SLOT);

        return gameFragment;
    }

    public CompletableFuture<Void> setup() {
        return onSetup().thenRun(this::provideImages);
    }

    private CompletableFuture<Void> onSetup() {
        var cpa = playerAccountService.currentPlayerAccount();
        return gamePartyFinder.join(cpa, this::setPlayer, this::refresh).thenCompose(gp -> {
            gameParty = gp;
            selectedHandCardView = new SelectableValue<>(
                    i -> {
                        secondSelectedCardSlotView.unselect();
                        firstSelectedCardSlotView.unselect();
                        startDefaultObjectAnimatorTransY(playerCardViews.get(i), -50.0f);
                    },
                    i -> startDefaultObjectAnimatorTransY(playerCardViews.get(i), 0.0f));
            secondSelectedCardSlotView = new SlotCardSelectable(playerBoard, opponentBoard, false);
            firstSelectedCardSlotView = new SlotCardSelectable(playerBoard, opponentBoard, false);
            selectedEmptySlotView = new SelectableValue<>();
            return CompletableFuture.completedFuture(null);
        });
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

        // Player.
        LinearLayout playerCardsLayout = view.findViewById(R.id.layout_playerCards);
        GridLayout playerBoardLayout = boardLayout.findViewById(R.id.layout_playerBoard);
        fillPlayerBoard(playerBoardLayout);
        playerCardViews = mapCardsLayout(player, playerCardsLayout);

        // Binding animation card.
        playerCardViews.forEach(v -> v.setOnClickListener(unused ->
                selectedHandCardView.bind(playerCardViews.lastIndexOf(v))));

        // Opponent.
        LinearLayout opponentCardsLayout = view.findViewById(R.id.layout_opponentCards);
        GridLayout opponentBoardLayout = boardLayout.findViewById(R.id.layout_opponentBoard);
        fillOpponentBoard(opponentBoardLayout);
        opponentCardViews = mapCardsLayout(gameParty.oppositePlayerOf(player), opponentCardsLayout);

        endButton = view.findViewById(R.id.btn_end);
        endButton.setOnClickListener(v -> {
            gameParty = gameParty.nextTurn();
            gameParty = sendRefresh(gameParty);
        });

        pullButton = view.findViewById(R.id.btn_pullCard);
        pullButton.setOnClickListener(v -> {
            var onPull = new OnPullCardViewTreatment(this::sendRefresh, cardService);
            gameParty = onPull.apply(gameParty);
        });

        endButton.setEnabled(gameParty.isCurrentPlayer(player));
        pullButton.setEnabled(gameParty.isCurrentPlayer(player));
    }

    private void fillPlayerBoard(GridLayout layout) {
        IntStream.range(0, layout.getChildCount()).forEach(i -> {
            View child = layout.getChildAt(i);
            child.setOnClickListener(this::onPressPlayerSlot);
            playerBoard.add(child);
        });
    }

    private void fillOpponentBoard(GridLayout layout) {
        IntStream.range(0, layout.getChildCount()).forEach(i -> {
            int newI = reverseIndexOpponentBoard(i);
            View child = layout.getChildAt(newI);
            child.setOnClickListener(this::onPressOpponentSlot);
            opponentBoard.add(child);
        });
    }

    private void onPressOpponentSlot(View view) {

        if (!gameParty.isCurrentPlayer(player) && !firstSelectedCardSlotView.isSelect())
            return;

        View emptySlotView = view.findViewById(R.id.view_emptySlot);
        int index = reverseIndexOpponentBoard(opponentBoard.indexOf(view));

        if (emptySlotView.getVisibility() == View.GONE) {
            isSelectedCardOpponent = true;
            selectCardToActivateSkill(index);
            gameParty = onPressSlot(gameParty);
        }

    }

    private void onPressPlayerSlot(View view) {
        if (gameParty.isCurrentPlayer(player)) {

            View emptySlotView = view.findViewById(R.id.view_emptySlot);
            int index = playerBoard.indexOf(view);

            if (emptySlotView.getVisibility() == View.VISIBLE) {
                selectedEmptySlotView.select(index);
            } else {
                isSelectedCardOpponent = false;
                selectCardToActivateSkill(index);
            }

            gameParty = onPressSlot(gameParty);
        }
    }

    private GameParty onPressSlot(GameParty gameParty) {

        PlayerAction playerAction = determinePlayerAction();

        gameParty = playerAction.apply(gameParty);

        selectedEmptySlotView.unselect();
        selectedHandCardView.unselect();

        return sendRefresh(gameParty);
    }

    private int reverseIndexOpponentBoard(int index) {
        return index < 4 ? index + 4 : index - 4;
    }

    private PlayerAction determinePlayerAction() {

        if (selectedHandCardView.isSelect() && selectedEmptySlotView.isSelect()) {

            int cardPos = selectedHandCardView.getValue();
            int slotPos = selectedEmptySlotView.getValue();

            return new EmplaceCardOnBoardViewTreatment(
                    player,
                    this::sendRefresh,
                    playerCardViews,
                    playerBoard,
                    this::getImageCardByCard,
                    cardPos,
                    slotPos);

        } else if (firstSelectedCardSlotView.isSelect() && secondSelectedCardSlotView.isSelect()) {

            var fromPos = firstSelectedCardSlotView;
            var toPos = secondSelectedCardSlotView;

            return new ActiveSkillViewTreatment(
                    navigationActivity,
                    this::sendRefresh,
                    gameService,
                    playerBoard,
                    opponentBoard,
                    this::getImageCardByCard,
                    getGamePartyUpdater(),
                    fromPos, toPos,
                    isSelectedCardOpponent
            );
        }

        return PlayerAction.noAction();
    }

    private void selectCardToActivateSkill(int index) {

        if (!firstSelectedCardSlotView.isSelect()) {
            firstSelectedCardSlotView.select(index);

        } else if (!firstSelectedCardSlotView.getValue().equals(index) ||
                (isSelectedCardOpponent && !firstSelectedCardSlotView.getValue()
                        .equals(reverseIndexOpponentBoard(index)))) {

            if (isSelectedCardOpponent) index = reverseIndexOpponentBoard(index);
            secondSelectedCardSlotView.setOpponent(isSelectedCardOpponent);
            secondSelectedCardSlotView.select(index);

        } else {
            secondSelectedCardSlotView.unselect();
            firstSelectedCardSlotView.unselect();
        }
    }

    private void provideImages() {
        cardService.provideImageCards(Stream.concat(
                            gameParty.firstPlayer().deck().cards().stream(),
                            gameParty.secondPlayer().deck().cards().stream())
                        .collect(Collectors.toList()))
                .thenAccept(this::setImageCardsReferences);
    }

    private void setImageCardsReferences(List<ImageCard> imageCards) {
        List<Card> cards = Stream
                .concat(gameParty.firstPlayer().deck().cards().stream(),
                        gameParty.secondPlayer().deck().cards().stream())
                .collect(Collectors.toList());
        imageCardsReferences = cardService.getMapReferences(cards, imageCards);
    }

    private List<View> mapCardsLayout(Player player, LinearLayout layout) {
        return IntStream.range(0, layout.getChildCount())
                .mapToObj(i -> bindCardLayout(player, layout, i))
                .collect(Collectors.toList());
    }

    private View bindCardLayout(Player player, LinearLayout layout, int index) {
        Card card = player.hand().get(index);
        View view = layout.getChildAt(index);
        CardViewBinding cardViewBinding = new CardViewBinding(view);
        cardViewBinding.bind(card, getImageCardByCard(card));
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

    public GamePartyUpdater getGamePartyUpdater() {
        return mapper -> refresh(mapper.map(gameParty));
    }

    private void refresh(GameParty gp) {
        gameParty = gp;
        if (!gameParty.isCurrentPlayer(player) && playerCardViews != null) {
            endButton.setEnabled(false);
            refreshBoard(gameParty.currentPlayerBoard(), opponentBoard);
            refreshBoard(gameParty.opponentPlayerBoard(), playerBoard);
            refreshHand(gameParty.currentPlayer(), opponentCardViews);
            refreshHand(gameParty.opponentPlayer(), playerCardViews);
        } else if (endButton != null) {
            endButton.setEnabled(true);
        }
    }

    private void refreshHand(Player player, List<View> views) {
        for (int i = 0; i < views.size(); i++) {
            View cardView = views.get(i);
            Card card = player.hand().get(i);
            if (card == null) {
                cardView.setVisibility(View.GONE);
            } else {
                CardViewBinding binding = new CardViewBinding(cardView);
                binding.bind(card, getImageCardByCard(card));
            }
        }
    }

    private void refreshBoard(Board board, List<View> views) {
        for (int i = 0; i < board.slots().size(); i++) {
            Slot slot = board.slots().get(i);
            View view = views.get(i);
            var emptyView = view.findViewById(R.id.view_emptySlot);
            var frameLayout = view.findViewById(R.id.view_cardSlot);
            if (slot instanceof Slot.SCard sCard) {
                CardViewBinding binding = new CardViewBinding(frameLayout);
                binding.bind(sCard.card(), getImageCardByCard(sCard.card()));
                emptyView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
            }
        }
    }

    private GameParty sendRefresh(GameParty gameParty) {
        return GameContainer.INSTANCE.getBackendGamePartyMapper().map(gameParty);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
