package org.utours.ace.profile.model;

import org.utours.ace.common.ImageContent;

public record ProfilePicture(
        String source,
        byte[] content,
        Type type
) implements ImageContent {
    public enum Type {
        Card,
        External
    }
}
