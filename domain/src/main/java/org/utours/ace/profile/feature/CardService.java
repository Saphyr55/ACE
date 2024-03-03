package org.utours.ace.profile.feature;

import org.utours.ace.common.FunctionalUtils;
import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.ImageCardDownloader;
import org.utours.ace.profile.port.ImageCardInstaller;
import org.utours.ace.profile.port.ImageCardProvider;

import java.util.ArrayList;
import java.util.List;
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

    public List<Card> sortedByRarity(List<Card> cards) {
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
        List<Card> cards = new ArrayList<>();
        cards.addAll(0, sortedByRarity(playerAccount.deck().cards()));
        for (Card card : sortedByRarity(playerAccount.cards())) {
            if (!cards.contains(card)) {
                cards.add(card);
            }
        }
        return cards;
    }

    public CompletableFuture<ImageCard> installImageCard(ImageCard imageCard) {
        return imageCardProvider.provide(imageCard.source())
                .thenCompose(image ->
                        image.map(CompletableFuture::completedFuture)
                        .orElseGet(() -> imageCardInstaller.install(imageCard)));
    }

    public CompletableFuture<ImageCard> provideCardImage(String uid) {

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


    public CompletableFuture<List<ImageCard>> provideAllCardImage(PlayerAccount playerAccount) {

        var imageCards = playerAccount.cards().stream()
                .map(card -> provideCardImage(card.pictureAddress()))
                .collect(Collectors.toList());

        return FunctionalUtils.listOfFuturesToFutureList(imageCards, Function.identity());
    }


}
