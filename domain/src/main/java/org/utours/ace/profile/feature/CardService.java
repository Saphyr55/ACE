package org.utours.ace.profile.feature;

import org.utours.ace.common.FunctionalUtils;
import org.utours.ace.common.ProbabilityUtils;
import org.utours.ace.common.Quantity;
import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.CardRarity;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.StarDust;
import org.utours.ace.profile.port.ImageCardDownloader;
import org.utours.ace.profile.port.ImageCardInstaller;
import org.utours.ace.profile.port.ImageCardProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.CompletableFuture;
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

    public List<Card> stochasticCards(int max, boolean unique) {

        int size = CardRegistry.INSTANCE.CARDS.size();
        if (max > size)
            throw new IllegalStateException();

        var cards = new ArrayList<Card>();
        var registry = List.copyOf(CardRegistry.INSTANCE.CARDS);

        var random = new Random();

        while (cards.size() != max) {
            int j = random.nextInt(size - 1);
            var card = registry.get(j);
            if (!unique) {
                cards.add(card);
            } else if (!cards.contains(card)) {
                cards.add(card);
            }
        }

        return cards;
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

    public Quantity<StarDust> determineStarDustConversion(Card card) {
        return switch (card.rarity()) {
            case R -> new Quantity<>(100);
            case SR -> new Quantity<>(300);
            case SSR -> new Quantity<>(1000);
            case UR -> new Quantity<>(3000);
        };
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
