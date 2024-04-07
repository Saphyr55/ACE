package org.utours.ace.infra.profile.jikan;

import org.utours.ace.common.ApiRequest;
import org.utours.ace.infra.common.jikan.JikanJsonParser;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.port.InformationCardProvider;

import java.util.concurrent.CompletableFuture;

public class JikanInformationCardProvider implements InformationCardProvider {

    public final static String URL = "https://api.jikan.moe/v4/characters/%s";
    private final JikanJsonParser jikanJsonParser;

    public JikanInformationCardProvider() {
        this.jikanJsonParser = new JikanJsonParser();
    }

    @Override
    public CompletableFuture<String> information(Card card) {
        return CompletableFuture.supplyAsync(() -> ApiRequest.builder()
                .withMethod(ApiRequest.Method.GET)
                .withSpecification(json -> jikanJsonParser.aboutCharacter(json, card.uid()))
                .withUrl(String.format(URL, card.uid()))
                .build()
                .getContent());
    }

}
