package org.utours.ace.profile.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.Card;

import java.util.concurrent.CompletableFuture;

@Port
public interface ProfileCardRepository {

    CompletableFuture<Card> getByUID(String uid);

}
