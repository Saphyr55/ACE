package org.utours.ace.infra.sqlite;

import static org.utours.ace.infra.sqlite.SQLiteDetails.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteACEDatabase extends SQLiteOpenHelper {


    public SQLiteACEDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CARD_IMAGE_NAME + "(uid VARCHAR(255), image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD_IMAGE_NAME);
    }

    public boolean insertCardImage(String uid, byte[] img) {
        var db = getWritableDatabase();
        var contentValues = new ContentValues();
        contentValues.put("uid", uid);
        contentValues.put("image", img);
        var result = db.insert(TABLE_CARD_IMAGE_NAME, null, contentValues);
        return result != -1;
    }

}
