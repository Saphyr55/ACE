package org.utours.ace.infra.profile.jikan;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.utours.ace.profile.port.ImageDownloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class JikanImageDownloader implements ImageDownloader {

    static String USER_API = "https://api.jikan.moe/v4/characters/%s/pictures";
    private final Map<String, byte[]> cache;

    public JikanImageDownloader() {
        cache = new HashMap<>();
    }

    @Override
    public CompletableFuture<byte[]> download(String id) {

        var completeUrlStr = String.format(USER_API, id);

        if (cache.containsKey(id)) {
            return CompletableFuture.completedFuture(cache.get(id));
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                var url = new URL(completeUrlStr);
                var httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                int responseCode = httpURLConnection.getResponseCode();
                System.out.println("HTTP status: " + responseCode);
                System.out.println("Users returned in request: ");

                System.out.println("Headers:");

                httpURLConnection
                        .getHeaderFields()
                        .forEach((header, value) -> System.out.println(header + " = " + String.join(", ", value)));

                var imageInput = getBufferedInputStream(url);
                var imageOutput = new ByteArrayOutputStream();

                int n = imageInput.read();
                for (;n != -1; n = imageInput.read()) {
                    imageOutput.write(n);
                }

                imageOutput.close();
                imageInput.close();
                return imageOutput.toByteArray();
            }
            catch (MalformedURLException e) {
                throw new RuntimeException("You've entered an invalid URL here: " + completeUrlStr);
            }
            catch (IOException e) {
                throw new RuntimeException("Error processing request", e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static BufferedInputStream getBufferedInputStream(URL url) throws IOException, JSONException {
        var in = new BufferedInputStream(url.openStream());

        var json = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            json.append((char) c);
        }

        var jsonObject = new JSONObject(json.toString());
        var array = (JSONArray) jsonObject.get("data");
        var object = (JSONObject) array.get(0);

        var imageURL = new URL((String) object.get("image_url"));
        return new BufferedInputStream(imageURL.openStream());
    }

}
