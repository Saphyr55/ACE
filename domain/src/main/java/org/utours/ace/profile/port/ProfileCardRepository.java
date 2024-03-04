package org.utours.ace.profile.port;

import org.utours.ace.profile.model.Card;

import java.util.concurrent.CompletableFuture;

public interface ProfileCardRepository {

    CompletableFuture<Card> getByUID(String id);

}
