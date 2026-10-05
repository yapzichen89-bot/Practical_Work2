package com.example;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class EventContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.practical_work2.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/registrations");

    private static final int REGISTRATIONS = 1;
    private static final int REGISTRATION_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "registrations", REGISTRATIONS);
        uriMatcher.addURI(AUTHORITY, "registrations/#", REGISTRATION_ID);
    }

    private EventDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new EventDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case REGISTRATIONS:
                cursor = db.query(EventDbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case REGISTRATION_ID:
                selection = EventDbHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(EventDbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(EventDbHelper.TABLE_NAME, null, values);
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case REGISTRATIONS:
                count = db.update(EventDbHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REGISTRATION_ID:
                selection = EventDbHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                count = db.update(EventDbHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case REGISTRATIONS:
                count = db.delete(EventDbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case REGISTRATION_ID:
                selection = EventDbHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                count = db.delete(EventDbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.com.example.practical_work2.registrations";
    }
}