package org.utours.ace.profile.model;

import org.utours.ace.common.ImageContent;

public record ImageCard(
        String source,
        byte[] content
) implements ImageContent {

}
