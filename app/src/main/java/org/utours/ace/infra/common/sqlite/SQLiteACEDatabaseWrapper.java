package org.utours.ace.infra.common.sqlite;

import static org.utours.ace.infra.common.sqlite.SQLiteDetails.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteACEDatabaseWrapper extends SQLiteOpenHelper {

    public SQLiteACEDatabaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CARD_IMAGE_NAME + "(source VARCHAR(255), image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD_IMAGE_NAME);
        onCreate(db);
    }

}
