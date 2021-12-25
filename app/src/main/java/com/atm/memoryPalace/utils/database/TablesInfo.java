package com.atm.memoryPalace.utils.database;

import android.provider.BaseColumns;

import com.google.android.gms.maps.model.LatLng;

public class TablesInfo {

    public static final class MemoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "memories";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE = "imageBitmap";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LAT = "location_latitude";
        public static final String COLUMN_LNG = "location_longitude";
        public static final String COLUMN_CREATE_DATE = "create_date";
    }

}
