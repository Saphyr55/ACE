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
        db.execSQL(
                "CREATE TABLE " + CardTable.TABLE_NAME +
                "(" +   CardTable.ATTR_SOURCE + "UNIQUE TEXT," +
                        CardTable.ATTR_IMAGE + " BLOB)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CardTable.TABLE_NAME);
        onCreate(db);
    }

}
