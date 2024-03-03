package org.utours.ace.infra.profile.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.utours.ace.infra.sqlite.SQLiteDetails;
import org.utours.ace.profile.model.ImageProfileCard;
import org.utours.ace.profile.port.ImageProfileCardProvider;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SQLiteImageProfileProvider implements ImageProfileCardProvider {

    private final SQLiteDatabase database;
    private final HashMap<String, ImageProfileCard> cache;

    public SQLiteImageProfileProvider(SQLiteDatabase database) {
        this.database = database;
        cache = new HashMap<>();
    }

    @Override
    public CompletableFuture<Optional<ImageProfileCard>> provide(String uid) {

        if (cache.containsKey(uid)) {
            return CompletableFuture.completedFuture(
                    Optional.ofNullable(cache.get(uid)));
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Cursor cursor = database.rawQuery(
                        "SELECT * FROM " + SQLiteDetails.TABLE_CARD_IMAGE_NAME +
                                " WHERE uid = ?", new String[]{ uid });
                cursor.moveToFirst();
                int cIndex = cursor.getColumnIndex("image");
                byte[] bitmap = cursor.getBlob(cIndex);
                cursor.close();
                var image = new ImageProfileCard(uid, bitmap);
                cache.put(uid, image);
                return Optional.of(image);
            } catch (Exception e) {
                return Optional.empty();
            }
        });
    }

}
