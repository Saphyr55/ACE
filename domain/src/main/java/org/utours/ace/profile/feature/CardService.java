package org.utours.ace.profile.feature;

import org.utours.ace.common.FunctionalUtils;
import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.CardRarity;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.ImageCardDownloader;
import org.utours.ace.profile.port.ImageCardInstaller;
import org.utours.ace.profile.port.ImageCardProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@DomainService
public class CardService {

    private final ImageCardInstaller imageCardInstaller;
    private final ImageCardProvider imageCardProvider;
    private final ImageCardDownloader imageCardDownloader;

    public CardService(ImageCardInstaller imageCardInstaller,
                       ImageCardProvider imageCardProvider,
                       ImageCardDownloader imageCardDownloader) {

        this.imageCardInstaller = imageCardInstaller;
        this.imageCardProvider = imageCardProvider;
        this.imageCardDownloader = imageCardDownloader;
    }

    public List<Card> sortedByRarity(Collection<Card> cards) {
        return cards.stream()
                .sorted((c1, c2) -> {
                    if (c1.rarity().ordinal() > c2.rarity().ordinal()) {
                        return 1;
                    } else if (c1.rarity().ordinal() < c2.rarity().ordinal()) {
                        return -1;
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }

    public List<Card> cardsDeckFirst(PlayerAccount playerAccount) {
        var deckCards = List.copyOf(playerAccount.deck().cards());
        var cards = sortedByRarity(deckCards);
        for (Card card : sortedByRarity(List.copyOf(playerAccount.cards()))) {
            if (!cards.contains(card)) {
                cards.add(card);
            }
        }
        return cards;
    }

    public CompletableFuture<ImageCard> installImageCard(ImageCard imageCard) {
        return imageCardProvider
                .provide(imageCard.source())
                .thenCompose(image ->
                        image.map(CompletableFuture::completedFuture)
                        .orElseGet(() -> imageCardInstaller.install(imageCard)));
    }

    public CompletableFuture<ImageCard> provideImageCard(String uid) {

        Supplier<CompletableFuture<ImageCard>> downloadAndInstall =
                () -> imageCardDownloader
                        .download(uid)
                        .thenCompose(this::installImageCard);

        return imageCardProvider
                .provide(uid)
                .thenCompose(imageCard -> imageCard
                        .map(CompletableFuture::completedFuture)
                        .orElseGet(downloadAndInstall));
    }

    public CompletableFuture<List<ImageCard>> provideImageCards(Collection<Card> cards) {

        var imageCards = cards.stream()
                .map(card -> provideImageCard(card.pictureId()))
                .collect(Collectors.toList());

        return FunctionalUtils.listOfFuturesToFutureList(imageCards, Function.identity());
    }

    public CompletableFuture<Void> downloadAndInstallAllImages() {
        return provideImageCards(CardRegistry.INSTANCE.CARDS).thenRun(() -> { });
    }

    public Map<String, ImageCard> getMapReferences(List<Card> cards, List<ImageCard> imageCards) {
        var result = new HashMap<String, ImageCard>();
        for (int i = 0; i < imageCards.size(); i++) {
            Card card = cards.get(i);
            result.put(card.pictureId(), imageCards.get(i));
        }
        return result;
    }

    public List<Card> getCardsStochastic(Collection<Card> collection, int max, boolean unique) {

        int size = collection.size();
        if (max > size) {
            throw new IllegalArgumentException();
        }

        var cards = new ArrayList<Card>();
        List<Card> registry = List.copyOf(collection);

        var random = new Random();

        Consumer<Integer> fillCards = s -> {
            int j = random.nextInt(s);
            var card = registry.get(j);
            if (!unique) {
                cards.add(card);
            } else if (!cards.contains(card)) {
                cards.add(card);
            }
        };

        if (max == 1) {
            fillCards.accept(max);
            return cards;
        }

        while (cards.size() != max) {
            fillCards.accept(size - 1);
        }

        return cards;
    }

    public List<Card> getCardsStochastic(int max, boolean unique) {
        return getCardsStochastic(CardRegistry.INSTANCE.CARDS, max, unique);
    }

    public List<Card> probabilistStochasticCards(int max) {

        List<Card> cards = new ArrayList<>();

        final int maxSize = CardRegistry.INSTANCE.CARDS.size();

        if (max > maxSize)
            throw new IllegalStateException();

        List<Card> registry = List.copyOf(CardRegistry.INSTANCE.CARDS);

        while (cards.size() != max) {
            int j = new Random().nextInt(maxSize);
            var card = registry.get(j);
            if (Math.random() >= 1 - cardRarityProbabilities(card.rarity())) {
                cards.add(card);
            }
        }

        return cards;
    }

    public double cardRarityProbabilities(CardRarity rarity) {
        return switch (rarity) {
            case UR -> 0.04;
            case SSR -> 0.16;
            case SR -> 0.35;
            case R -> 0.45;
        };
    }

}
