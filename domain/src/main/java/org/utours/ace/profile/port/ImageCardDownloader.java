package org.utours.ace.profile.port;

import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ImageCardDownloader {

    CompletableFuture<ImageCard> download(String source);

}
