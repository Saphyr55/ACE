package org.utours.ace.profile.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.ImageProfileCard;

import java.util.concurrent.CompletableFuture;

@Port
public interface ImageProfileCardInsertion {

    CompletableFuture<ImageProfileCard> insert(ImageProfileCard imageProfileCard);

}
