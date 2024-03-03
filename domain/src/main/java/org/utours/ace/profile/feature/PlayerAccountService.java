package org.utours.ace.profile.feature;

import org.utours.ace.common.FunctionalUtils;
import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.port.ImageCardDownloader;
import org.utours.ace.profile.port.ImageCardInstaller;
import org.utours.ace.profile.port.ImageCardProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
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
        return imageCardProvider.provide(imageCard.source()).thenCompose(image ->
                image.map(CompletableFuture::completedFuture)
                .orElseGet(() -> imageCardInstaller.install(imageCard)));
    }

    public CompletableFuture<ImageCard> provideCardImage(String uid) {
        return imageCardProvider.provide(uid).thenCompose(imageCard ->
                imageCard.map(CompletableFuture::completedFuture)
                    .orElseGet(() -> imageCardDownloader.download(uid)
                            .thenCompose(this::installImageCard)));
    }


    public CompletableFuture<List<ImageCard>> provideAllCardImage(PlayerAccount playerAccount) {

        var imageCards = playerAccount.cards().stream()
                .map(card -> provideCardImage(card.pictureAddress()))
                .collect(Collectors.toList());

        return FunctionalUtils.listOfFuturesToFutureList(imageCards, Function.identity());
    }



}
