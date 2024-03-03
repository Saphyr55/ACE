package org.utours.ace.infra.profile.jikan;

import android.util.Log;

import org.json.JSONException;
import org.utours.ace.common.ImageDownloader;
import org.utours.ace.infra.common.jikan.JikanJsonParser;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.port.ImageCardDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class JikanImageCardDownloader implements ImageCardDownloader {

    static String URL = "https://api.jikan.moe/v4/characters/%s/pictures";

    private final ImageDownloader imageDownloader;
    private final JikanJsonParser jikanJsonParser;

    public JikanImageCardDownloader(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
        this.jikanJsonParser = new JikanJsonParser();
    }

    @Override
    public CompletableFuture<ImageCard> download(String source) {
        return CompletableFuture
                .supplyAsync(() -> connectURL(String.format(URL, source)))
                .thenCompose(imageDownloader::download)
                .thenApply(bytes -> new ImageCard(source, bytes));
    }

    private URL connectURL(String completeUrlStr) {
        try {
            var url = new URL(completeUrlStr);
            var connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Log.i("", "Connection to '" + completeUrlStr + "'.");

            if (connection.getResponseCode() != 200)
                 throw new Exception("Http Error : " + connection.getResponseCode());

            var json = readData(connection);

            Thread.sleep(300);

            var pictureURL = jikanJsonParser.pictureURL(json);

            return new URL(pictureURL);

        } catch (MalformedURLException e) {
            Log.e("", "You've entered an invalid URL here: " + completeUrlStr, e);
            throw new RuntimeException();
        } catch (IOException e) {
            Log.e("", "Error processing request.", e);
            throw new RuntimeException(e);
        } catch (JSONException e) {
            Log.e("", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readData(HttpURLConnection connection) throws IOException {
        Log.i("", "Read data from '" + connection.getURL().getPath() + "'.");
        try (var input = connection.getInputStream()) {
            var json = new StringBuilder();
            int c;
            while ((c = input.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        }
    }

}
