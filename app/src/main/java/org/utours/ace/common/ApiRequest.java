package org.utours.ace.common;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

public class ApiRequest {

    public static Builder builder() {
        return new Builder();
    }

    public enum Method {
        GET
    }

    private final Function<String, String> specification;
    private final Method method;
    private final String url;


    public ApiRequest(String url, Function<String, String> specification, Method method) {
        this.method = method;
        this.specification = specification;
        this.url = url;
    }

    public String getContent() {
        try {
            var connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method.name());

            Log.i("", "Connection to '" + url + "'.");

            if (connection.getResponseCode() != 200)
                throw new Exception("Http Error : " + connection.getResponseCode());

            var json = readData(connection);

            Thread.sleep(300);

            return specification.apply(json);

        } catch (MalformedURLException e) {
            Log.e("ApiRequest", "You've entered an invalid URL here: " + url, e);
            throw new RuntimeException();
        } catch (IOException e) {
            Log.e("ApiRequest", "Error processing request.", e);
            throw new RuntimeException(e);
        } catch (JSONException e) {
            Log.e("ApiRequest", e.getMessage(), e);
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

    public static class Builder {

        private Function<String, String> specification = Function.identity();
        private Method method = Method.GET;
        private String url;

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withSpecification(Function<String, String> specification) {
            this.specification = specification;
            return this;
        }

        public Builder withMethod(Method method) {
            this.method = method;
            return this;
        }

        public ApiRequest build() {
            return new ApiRequest(
                    url,
                    specification,
                    method
            );
        }

    }

}
