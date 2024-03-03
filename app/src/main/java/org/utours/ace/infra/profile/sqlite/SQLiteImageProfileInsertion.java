package org.utours.ace.infra.profile.sqlite;

import static org.utours.ace.infra.sqlite.SQLiteDetails.TABLE_CARD_IMAGE_NAME;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import org.utours.ace.profile.model.ImageProfileCard;
import org.utours.ace.profile.port.ImageProfileCardInsertion;

import java.util.concurrent.CompletableFuture;

public final class SQLiteImageProfileInsertion implements ImageProfileCardInsertion {

    private final SQLiteDatabase database;

    public SQLiteImageProfileInsertion(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public CompletableFuture<ImageProfileCard> insert(ImageProfileCard imageProfileCard) {
        return CompletableFuture.supplyAsync(() -> {

            var contentValues = new ContentValues();
            contentValues.put("uid", imageProfileCard.uid());
            contentValues.put("image", imageProfileCard.content());
            if (database.insert(TABLE_CARD_IMAGE_NAME, null, contentValues) == -1)
                throw new RuntimeException();

            return imageProfileCard;
        });
    }

}
