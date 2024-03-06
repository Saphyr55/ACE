package org.utours.ace.profile.feature;

import junit.framework.TestCase;

import org.utours.ace.profile.model.CardRarity;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.port.ImageCardDownloader;
import org.utours.ace.profile.port.ImageCardInstaller;
import org.utours.ace.profile.port.ImageCardProvider;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class CardServiceTest extends TestCase {

    CardService cardService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        var image = new ImageCard("", new byte[]{});

        ImageCardDownloader cardDownloader = source -> CompletableFuture.completedFuture(image);
        ImageCardProvider imageCardProvider = source -> CompletableFuture.completedFuture(Optional.of(image));
        ImageCardInstaller imageCardInstaller = CompletableFuture::completedFuture;

        cardService = new CardService(imageCardInstaller, imageCardProvider, cardDownloader);
    }

    public void testSortedByRarity() {
    }

    public void testCardsDeckFirst() {
    }

    public void testCardRarityProbabilities() {

        var sum = Stream.of(CardRarity.R, CardRarity.SSR, CardRarity.SR, CardRarity.UR)
                .map(cardService::cardRarityProbabilities)
                .reduce(Double::sum);

        assertEquals(1.d, sum.get());
    }

}