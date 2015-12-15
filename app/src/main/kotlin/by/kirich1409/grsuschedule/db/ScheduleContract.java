package by.kirich1409.grsuschedule.db;

import android.net.Uri;
import android.provider.BaseColumns;

import by.kirich1409.grsuschedule.BuildConfig;

/**
 * Created by kirillrozov on 12/5/15.
 */
public final class ScheduleContract {

    public static final String AUTHORITY = BuildConfig.SCHEDULE_PROVIDER_AUTHORITY;
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Favourite implements BaseColumns {

        public static final String CONTENT_TABLE = "favourites";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_TABLE);

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/schedule_favourites";

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/schedule_favourite";

        public static final String TYPE_TEACHER = "teacher";

        public static final String TYPE_GROUP = "group";

        public static final String TYPE = "type";

        public static final String NAME = "name";

        public static final String TIMESTAMP = "timestamp";

        public static final String CONTENT_ID = "content_id";

        private Favourite() {
        }
    }

    private ScheduleContract() {
    }
}
