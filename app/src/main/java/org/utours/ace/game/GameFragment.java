package org.utours.ace.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.utours.ace.R;
import org.utours.ace.game.model.Player;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardRegistry;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GameFragment extends Fragment {

    private NavigationActivity navigationActivity;
    private CardService cardService;
    private PlayerAccountService playerAccountService;
    private Player player;
    private Player opponent;
    private Map<String, ImageCard> imageCardsReferences;

    public GameFragment() { }

    public static GameFragment newInstance(NavigationActivity navigationActivity,
                                           CardService cardService,
                                           PlayerAccountService playerAccountService) {

        GameFragment gameFragment = new GameFragment();

        gameFragment.cardService = cardService;
        gameFragment.navigationActivity = navigationActivity;
        gameFragment.playerAccountService = playerAccountService;

        var cards = List.of(
                CardRegistry.FERN_CARD.get(),
                CardRegistry.UBEL_CARD.get(),
                CardRegistry.HIMMEL_CARD.get(),
                CardRegistry.FRIEREN_CARD.get(),
                CardRegistry.SEIN_CARD.get());

        Player player = new Player(List.copyOf(cards));
        Player opponent = new Player(List.copyOf(cards));

        gameFragment.player = player;
        gameFragment.opponent = opponent;

        return gameFragment;
    }

    public CompletableFuture<Void> setup() {
        return CompletableFuture
                .runAsync(this::onSetup)
                .thenRun(this::provideImages);
    }

    private void onSetup() {  }

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

        RecyclerView playerCardsRecycler = view.findViewById(R.id.recycler_playerCards);
        RecyclerView opponentCardsRecycler = view.findViewById(R.id.recycler_opponentCards);

        PlayerCardsAdapter playerCardsAdapter = new PlayerCardsAdapter(player, imageCardsReferences);
        PlayerCardsAdapter opponentCardsAdapter = new PlayerCardsAdapter(opponent, imageCardsReferences);

        playerCardsRecycler.setAdapter(playerCardsAdapter);
        opponentCardsRecycler.setAdapter(opponentCardsAdapter);
    }

    private void provideImages() {
        cardService.provideImageCards(player.hand()).thenAccept(this::fillImages);
    }

    private void fillImages(List<ImageCard> imageCards) {
        imageCardsReferences = cardService.getMapReferences(player.hand(), imageCards);
    }

}
