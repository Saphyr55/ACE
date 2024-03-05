package org.utours.ace.infra.common.sqlite;

public final class SQLiteDetails {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ace.db";
    public static final String TABLE_CARD_IMAGE_NAME = "card_image_t";

    public static class CardTable {

        public static final String TABLE_NAME = "card_image_t";
        public static final String ATTR_SOURCE = "source";
        public static final String ATTR_IMAGE = "image";

    }

}
