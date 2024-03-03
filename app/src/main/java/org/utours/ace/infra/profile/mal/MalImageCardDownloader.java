package org.utours.ace.infra.profile.mal;

import android.util.Log;

import org.utours.ace.common.ImageDownloader;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.port.ImageCardDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MalImageCardDownloader implements ImageCardDownloader {

    private static final String URL = "https://cdn.myanimelist.net/images/characters/%s/%s.jpg";
    private static final int MAX_SEARCH = 20;
    private final ImageDownloader imageDownloader;
    private final Map<String, ImageCard> cache;

    public MalImageCardDownloader(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
        this.cache = new HashMap<>();
    }

    @Override
    public CompletableFuture<ImageCard> download(String source) {

        if (cache.containsKey(source)) {
            return CompletableFuture.completedFuture(cache.get(source));
        }

        return CompletableFuture
                .supplyAsync(() -> connectURL(source))
                .thenCompose(imageDownloader::download)
                .thenApply(bytes -> {
                    var image = new ImageCard(source, bytes);
                    cache.put(source, image);
                    return image;
                });
    }

    private URL connectURL(String source) {
        try {
            var url = new URL(source);
            var connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() != 200) {
                Log.e("MAL", "Not found an image at '" + "'.");
                throw new RuntimeException();
            }

            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
