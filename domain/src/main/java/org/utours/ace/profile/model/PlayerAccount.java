package org.utours.ace.profile.model;

import java.util.List;

public record PlayerAccount(
        String uid,
        String username,
        String email,
        Integer level,
        String description,
        List<Card> cards,
        ProfileDeck deck
) {

}
