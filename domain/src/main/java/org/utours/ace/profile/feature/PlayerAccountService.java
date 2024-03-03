package org.utours.ace.profile.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.port.ImageCardDownloader;
import org.utours.ace.profile.port.ImageCardInstaller;
import org.utours.ace.profile.port.ImageCardProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@DomainService
public final class PlayerAccountService {

    private final ImageCardInstaller imageCardInstaller;
    private final ImageCardProvider imageCardProvider;
    private final ImageCardDownloader imageCardDownloader;

    public PlayerAccountService(ImageCardInstaller imageCardInstaller,
                                ImageCardProvider imageCardProvider,
                                ImageCardDownloader imageCardDownloader) {

        this.imageCardInstaller = imageCardInstaller;
        this.imageCardProvider = imageCardProvider;
        this.imageCardDownloader = imageCardDownloader;
    }

    public CompletableFuture<ImageCard> installImageCard(ImageCard imageCard) {
        return imageCardProvider.provide(imageCard.uid())
                .thenCompose(image ->
                        image.<CompletionStage<ImageCard>>map(CompletableFuture::completedFuture)
                        .orElseGet(() -> imageCardInstaller.install(imageCard))
                );
    }

    public CompletableFuture<Optional<ImageCard>> provideCardImage(String uid) {

        return imageCardProvider.provide(uid).thenCompose(imageCard -> {
            if (imageCard.isPresent()) {
                return CompletableFuture.completedFuture(imageCard);
            }
            return imageCardDownloader
                    .download(uid)
                    .thenCompose(card -> installImageCard(card)
                            .thenApply(Optional::of));

        });
    }


    public CompletableFuture<Map<String, ImageCard>> provideAllCardImage(PlayerAccount playerAccount) {

        var cardImageFutures = new HashMap<String, CompletableFuture<Optional<ImageCard>>>();

        for (Card card : playerAccount.cards()) {
            var future = provideCardImage(card.pictureAddress());
            cardImageFutures.put(card.pictureAddress(), future);
        }

        var allFutures = CompletableFuture
                .allOf(cardImageFutures.values().toArray(new CompletableFuture[]{ }));

        return allFutures.thenApply(ignored ->
                cardImageFutures.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                            var opt = entry.getValue().join();
                            if (opt.isPresent()) {
                                return opt.get();
                            }
                            throw new RuntimeException();
                        })));
    }


}
