package org.utours.ace.profile.port;


import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.ImageProfileCard;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Port
public interface ImageProfileCardProvider {

    CompletableFuture<Optional<ImageProfileCard>> provide(String uid);

}
