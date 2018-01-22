package com.ksajja.reminders;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ksajja on 1/21/18.
 */

public class CalendarManager {

    private Context mContext;

    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;

    CalendarManager(Context context) {
        mContext = context;
    }

    void queryReminders(){
        Calendar startTime = Calendar.getInstance();
        startTime.set(2018, 00, 20, 00, 00);

        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 00, 23, 00, 00);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startTime.getTimeInMillis());
        ContentUris.appendId(builder, endTime.getTimeInMillis());

        Cursor cursor = null;
        try {
            cursor = this.mContext.getContentResolver().query(builder.build(),
                    INSTANCE_PROJECTION,
                    null,
                    null,
                    null);

            if (cursor == null) return;
            if (cursor.moveToFirst()) {
                do {
                    String Title = cursor.getString(PROJECTION_TITLE_INDEX);
                    long eventID = cursor.getLong(PROJECTION_ID_INDEX);
                    long beginVal = cursor.getLong(PROJECTION_BEGIN_INDEX);
                    String abc = Title;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(CalendarManager.class.getSimpleName(), e.getLocalizedMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    void queryEvents() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(2018, 00, 20, 00, 00);

        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 00, 23, 00, 00);

        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))";

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor cursor = null;
        try {
            //cursor = this.mContext.getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, selection, null, null);
            cursor = this.mContext.getContentResolver().query(CalendarContract.CONTENT_URI,
                    new String[]{ "_id", "title", "description", "dtstart", "dtend" },
                    selection,null, null);
            if (cursor == null) return;
            if (cursor.moveToFirst()) {
                do {
                    String Title = cursor.getString(1);
                    Date startTimeStr = new Date(cursor.getLong(3));
                    String abc = Title;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(CalendarManager.class.getSimpleName(), e.getLocalizedMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
