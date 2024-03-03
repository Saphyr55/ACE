package org.utours.ace.infra.common.http;

import android.util.Log;

import org.json.JSONException;
import org.utours.ace.common.ImageDownloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class HttpImageDownloader implements ImageDownloader {

    @Override
    public CompletableFuture<byte[]> download(URL url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var httpURLConnectionImage = (HttpURLConnection) url.openConnection();
                httpURLConnectionImage.setRequestMethod("GET");
                var imageInput = httpURLConnectionImage.getInputStream();

                var imageOutput = new ByteArrayOutputStream();

                int n = imageInput.read();
                for (;n != -1; n = imageInput.read()) {
                    imageOutput.write(n);
                }

                imageOutput.close();
                imageInput.close();

                return imageOutput.toByteArray();
            } catch (MalformedURLException e) {
                Log.e("", "You've entered an invalid URL here: " + url.getPath(), e);
                throw new RuntimeException();
            } catch (IOException e) {
                Log.e("", "Error processing request.", e);
                throw new RuntimeException(e);
            }
        });
    }

}
