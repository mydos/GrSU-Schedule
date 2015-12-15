package by.kirich1409.grsuschedule.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import junit.framework.Assert;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by kirillrozov on 12/5/15.
 */
public class ScheduleContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CODE_FAVOURITES = 1;
    private static final int CODE_FAVOURITE_ITEM = 2;

    static {
        sUriMatcher.addURI(ScheduleContract.AUTHORITY,
                ScheduleContract.Favourite.CONTENT_TABLE, CODE_FAVOURITES);
        sUriMatcher.addURI(ScheduleContract.AUTHORITY,
                ScheduleContract.Favourite.CONTENT_TABLE + "/#", CODE_FAVOURITE_ITEM);
    }

    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        Assert.assertNotNull(context);
        mHelper = new ScheduleDBHelper(context);
        return true;
    }

    public SQLiteDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = mHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        int code = sUriMatcher.match(uri);
        if (code == CODE_FAVOURITES) {
            if (TextUtils.isEmpty(sortOrder)) {
                sortOrder = ScheduleContract.Favourite.TIMESTAMP + " ASC";
            }

            int countIndexOf = ArrayUtils.indexOf(projection, ScheduleContract.Favourite._COUNT);
            if (countIndexOf >= 0) {
                projection[countIndexOf] = "count(*) as " + ScheduleContract.Favourite._COUNT;
            }

            cursor = getDatabase().query(ScheduleContract.Favourite.CONTENT_TABLE,
                    projection, selection, selectionArgs, null, null, sortOrder);

        } else if (code == CODE_FAVOURITE_ITEM) {
            String idSelection = ScheduleContract.Favourite._ID + " = " + uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                selection = idSelection;
            } else {
                selection += " " + idSelection;
            }

            int countIndexOf = ArrayUtils.indexOf(projection, ScheduleContract.Favourite._COUNT);
            if (countIndexOf >= 0) {
                projection[countIndexOf] = "count(*) as " + ScheduleContract.Favourite._COUNT;
            }

            cursor = getDatabase().query(ScheduleContract.Favourite.CONTENT_TABLE,
                    projection, selection, selectionArgs, null, null, sortOrder);

        } else {
            cursor = null;
        }

        if (cursor != null) {
            Context context = getContext();
            Assert.assertNotNull(context);
            cursor.setNotificationUri(
                    context.getContentResolver(), ScheduleContract.Favourite.CONTENT_URI);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVOURITE_ITEM:
                return ScheduleContract.Favourite.CONTENT_ITEM_TYPE;

            case CODE_FAVOURITES:
                return ScheduleContract.Favourite.CONTENT_TYPE;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVOURITES:
                if (!values.containsKey(ScheduleContract.Favourite.TIMESTAMP)) {
                    values.put(ScheduleContract.Favourite.TIMESTAMP, System.currentTimeMillis());
                }

                long newRowId = getDatabase().insert(
                        ScheduleContract.Favourite.CONTENT_TABLE, null, values);
                if (newRowId != -1) {
                    notifyChange(ScheduleContract.Favourite.CONTENT_URI, 1);
                    return ContentUris.withAppendedId(
                            ScheduleContract.Favourite.CONTENT_URI, newRowId);
                } else {
                    return null;
                }

            case UriMatcher.NO_MATCH:
            default:
                return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int code = sUriMatcher.match(uri);
        if (code == CODE_FAVOURITES) {
            int deleteCount = getDatabase()
                    .delete(ScheduleContract.Favourite.CONTENT_TABLE, selection, selectionArgs);
            return notifyChange(ScheduleContract.Favourite.CONTENT_URI, deleteCount);

        } else if (code == CODE_FAVOURITE_ITEM) {
            if (TextUtils.isEmpty(selection)) {
                selection = ScheduleContract.Favourite._ID + " = " + uri.getLastPathSegment();
            }

            int deleteCount = getDatabase()
                    .delete(ScheduleContract.Favourite.CONTENT_TABLE, selection, selectionArgs);
            return notifyChange(ScheduleContract.Favourite.CONTENT_URI, deleteCount);

        } else {
            return 0;
        }
    }

    private int notifyChange(@NonNull Uri uri, int effectedRows) {
        if (effectedRows > 0) {
            Context context = getContext();
            Assert.assertNotNull(context);
            context.getContentResolver().notifyChange(uri, null);
        }
        return effectedRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int code = sUriMatcher.match(uri);
        if (code == CODE_FAVOURITES) {
            return getDatabase().update(
                    ScheduleContract.Favourite.CONTENT_TABLE, values, selection, selectionArgs);

        } else if (code == CODE_FAVOURITE_ITEM) {
            if (TextUtils.isEmpty(selection)) {
                selection = ScheduleContract.Favourite._ID + " = " + uri.getLastPathSegment();
            }
            return getDatabase().update(
                    ScheduleContract.Favourite.CONTENT_TABLE, values, selection, selectionArgs);

        } else {
            return 0;
        }
    }
}
