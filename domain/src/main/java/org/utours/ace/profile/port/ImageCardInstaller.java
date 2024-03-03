package org.utours.ace.profile.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.ImageCard;

import java.util.concurrent.CompletableFuture;

@Port
public interface ImageCardInstaller {

    CompletableFuture<ImageCard> install(ImageCard imageCard);

}
