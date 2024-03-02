package org.utours.ace.profile.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.ProfileCardImage;
import org.utours.ace.profile.port.ImageProfileCardInsertion;
import org.utours.ace.profile.port.ImageProfileCardProvider;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.concurrent.CompletableFuture;

@DomainService
public final class PlayerAccountService {


    private final ImageProfileCardInsertion imageProfileCardInsertion;
    private final ImageProfileCardProvider imageProfileCardProvider;
    private final PlayerAccountRepository playerAccountRepository;


    public PlayerAccountService(ImageProfileCardInsertion imageProfileCardInsertion,
                                ImageProfileCardProvider imageProfileCardProvider,
                                PlayerAccountRepository playerAccountRepository) {

        this.imageProfileCardInsertion = imageProfileCardInsertion;
        this.imageProfileCardProvider = imageProfileCardProvider;
        this.playerAccountRepository = playerAccountRepository;
    }

    public CompletableFuture<ProfileCardImage> insertCardImage(ProfileCardImage profileCardImage) {
        return imageProfileCardInsertion.insert(profileCardImage);
    }

    public CompletableFuture<ProfileCardImage> provideCardImage(String uid) {
        return imageProfileCardProvider.provide(uid);
    }




}
