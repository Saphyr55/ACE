package org.utours.ace.infra.profile.sqlite;

import static org.utours.ace.infra.common.sqlite.SQLiteDetails.TABLE_CARD_IMAGE_NAME;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.port.ImageCardInstaller;

import java.util.concurrent.CompletableFuture;

public final class SQLiteImageInstaller implements ImageCardInstaller {

    private final SQLiteDatabase database;

    public SQLiteImageInstaller(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public CompletableFuture<ImageCard> install(ImageCard imageCard) {
        return CompletableFuture.supplyAsync(() -> {

            var contentValues = new ContentValues();
            contentValues.put("uid", imageCard.uid());
            contentValues.put("image", imageCard.content());
            if (database.insert(TABLE_CARD_IMAGE_NAME, null, contentValues) == -1)
                throw new RuntimeException();

            return imageCard;
        });
    }

}
