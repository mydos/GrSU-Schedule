package by.kirich1409.grsuschedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by kirillrozov on 12/5/15.
 */
class ScheduleDBHelper(context: Context) : SQLiteOpenHelper(context, "schedules", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + ScheduleContract.Favourite.CONTENT_TABLE + "(" +
                ScheduleContract.Favourite._ID + " INTEGER AUTO_INCREMENT," +
                ScheduleContract.Favourite.NAME + " VARCHAR(255) NOT NULL," +
                ScheduleContract.Favourite.TIMESTAMP + " INTEGER NOT NULL," +
                ScheduleContract.Favourite.TYPE + " VARCHAR(20) NOT NULL," +
                ScheduleContract.Favourite.CONTENT_ID + " INTEGER NOT NULL," +
                "PRIMARY KEY(" + ScheduleContract.Favourite._ID + ")" +
                ")")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE " + ScheduleContract.Favourite.CONTENT_TABLE)
        onCreate(db)
    }
}