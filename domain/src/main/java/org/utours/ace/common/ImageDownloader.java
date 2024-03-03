package org.utours.ace.common;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public interface ImageDownloader {

    CompletableFuture<byte[]> download(URL url);

}
