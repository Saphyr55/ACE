package org.utours.ace.infra.profile.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.utours.ace.infra.sqlite.SQLiteDetails;
import org.utours.ace.profile.model.ProfileCardImage;
import org.utours.ace.profile.port.ImageProfileCardProvider;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class SQLiteImageProfileProvider implements ImageProfileCardProvider {

    private final SQLiteDatabase database;
    private HashMap<String, ProfileCardImage> cache;

    public SQLiteImageProfileProvider(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public CompletableFuture<ProfileCardImage> provide(String uid) {

        if (cache.containsKey(uid)) {
            return CompletableFuture.completedFuture(cache.get(uid));
        }

        return CompletableFuture.supplyAsync(() -> {
            Cursor cursor = database.rawQuery(
                        "SELECT * FROM " + SQLiteDetails.TABLE_CARD_IMAGE_NAME +
                            " WHERE uid = ?", new String[]{ uid });
            cursor.moveToFirst();
            int cIndex = cursor.getColumnIndex("image");
            byte[] bitmap = cursor.getBlob(cIndex);
            cursor.close();
            return new ProfileCardImage(uid, bitmap);
        });
    }

}
