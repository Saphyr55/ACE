package org.utours.ace.profile.model;

public record ProfileDeck(
        ProfileCard first,
        ProfileCard second,
        ProfileCard third,
        ProfileCard fourth,
        ProfileCard fifth
) {
}
