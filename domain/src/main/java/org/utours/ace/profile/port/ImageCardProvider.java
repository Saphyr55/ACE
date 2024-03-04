package org.utours.ace.profile.port;


import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.ImageCard;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ImageCardProvider {

    CompletableFuture<Optional<ImageCard>> provide(String source);

}
