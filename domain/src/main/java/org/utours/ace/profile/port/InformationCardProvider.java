package org.utours.ace.profile.port;

import org.utours.ace.profile.model.Card;

import java.util.concurrent.CompletableFuture;

public interface InformationCardProvider {


    CompletableFuture<String> information(Card card);


}
