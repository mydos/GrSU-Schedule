package by.kirich1409.grsuschedule.utils;

import com.octo.android.robospice.persistence.DurationInMillis;

import java.util.Locale;

/**
 * Created by kirillrozov on 9/27/15.
 */
public final class Constants {
    public static final Locale LOCALE_RU = new Locale("ru", "RU");
    public static final int MINUTES_IN_HOUR = 60;
    public static final long SCHEDULE_TIME_CACHE = DurationInMillis.ONE_DAY;
    public static final long FACULTIES_TIME_CACHE = DurationInMillis.ONE_DAY;
    public static final long DEPARTMENTS_TIME_CACHE = DurationInMillis.ONE_DAY;
    public static final long GROUPS_TIME_CACHE = DurationInMillis.ONE_DAY;
    public static final long TEACHERS_TIME_CACHE = DurationInMillis.ONE_DAY;
    public static final String MIMETYPE_TEXT_PLAIN = "text/plain";

    private Constants() {
    }
}
