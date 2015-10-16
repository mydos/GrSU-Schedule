package by.kirich1409.grsuschedule.utils;

import com.octo.android.robospice.persistence.DurationInMillis;

import java.util.Locale;

/**
 * Created by kirillrozov on 9/27/15.
 */
public final class Constants {
    public static final Locale LOCALE_RU = new Locale("ru", "RU");
    public static final Locale LOCALE_BY_BE = new Locale("be", "BY");
    public static final int MINUTES_IN_HOUR = 60;
    public static final long GROUPS_TIME_CACHE = DurationInMillis.ONE_DAY;
    public static final String MIMETYPE_TEXT_PLAIN = "text/plain";
    public static final String GOOGLE_PLAY_APP_LINK =
            "https://play.google.com/store/apps/details?" +
                    "id=by.kirich1409.grsuschedule&referrer=utm_source%3Dapp";

    private Constants() {
    }
}
