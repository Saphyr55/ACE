package org.utours.ace.profile.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.ProfileCard;
import org.utours.ace.profile.model.ImageProfileCard;
import org.utours.ace.profile.port.ImageDownloader;
import org.utours.ace.profile.port.ImageProfileCardInsertion;
import org.utours.ace.profile.port.ImageProfileCardProvider;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DomainService
public final class PlayerAccountService {


    private final ImageProfileCardInsertion imageProfileCardInsertion;
    private final ImageProfileCardProvider imageProfileCardProvider;
    private final ImageDownloader imageDownloader;

    public PlayerAccountService(ImageProfileCardInsertion imageProfileCardInsertion,
                                ImageProfileCardProvider imageProfileCardProvider,
                                ImageDownloader imageDownloader) {

        this.imageProfileCardInsertion = imageProfileCardInsertion;
        this.imageProfileCardProvider = imageProfileCardProvider;
        this.imageDownloader = imageDownloader;
    }

    public CompletableFuture<ImageProfileCard> insertCardImage(ImageProfileCard imageProfileCard) {
        return imageProfileCardInsertion.insert(imageProfileCard);
    }

    public CompletableFuture<Optional<ImageProfileCard>> provideCardImage(String uid) {

        return imageProfileCardProvider.provide(uid).thenCompose(imageProfileCard -> {

            if (imageProfileCard.isPresent()) {
                return CompletableFuture.completedFuture(imageProfileCard);
            }

            return imageDownloader.download(uid).thenCompose(bytes -> {
                var card = new ImageProfileCard(uid, bytes);
                return insertCardImage(card).thenApply(Optional::of);
            });

        });
    }


    public CompletableFuture<Map<String, ImageProfileCard>> provideAllCardImage(PlayerAccount playerAccount) {

        var cardImageFutures = new HashMap<String, CompletableFuture<Optional<ImageProfileCard>>>();

        for (ProfileCard card : playerAccount.cards()) {
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
