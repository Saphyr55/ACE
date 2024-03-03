package org.utours.ace.profile.port;

import java.util.concurrent.CompletableFuture;

public interface ImageDownloader {

    CompletableFuture<byte[]> download(String uid);

}
