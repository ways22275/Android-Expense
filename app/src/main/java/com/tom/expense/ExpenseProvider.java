package com.tom.expense;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by hank on 2018/3/3.
 */

public class ExpenseProvider extends ContentProvider {
    public static final String TAG = ExpenseProvider.class.getSimpleName();
    DBHelper helper = null;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int EXPENSES = 200;
    private static final int EXPENSE_WITH_ID = 100;

    static {
        sUriMatcher.addURI(ExpenseContract.AUTHORITY,
                ExpenseContract.TABLE_EXPENSE,
                EXPENSES);
        sUriMatcher.addURI(ExpenseContract.AUTHORITY,
                ExpenseContract.TABLE_EXPENSE + "/#",
                EXPENSE_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: ");
        helper = DBHelper.getInstance(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String orderBy) {
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)){
            case EXPENSES:
                cursor = helper.getReadableDatabase()
                        .query(ExpenseContract.TABLE_EXPENSE,
                                projection, selection, selectionArgs,
                                null, null, orderBy);
                break;
            case EXPENSE_WITH_ID:
                long id = ContentUris.parseId(uri);
                String where = ExpenseContract.COL_ID + " = ? ";
//                selection = (selection == null) ? selection :
//                        selection + " AND ";
                //hack
                selection = (selection == null) ? "" : selection;
                selection = selection + where;
                selectionArgs = new String[]{String.valueOf(id)};
                cursor = helper.getReadableDatabase()
                        .query(ExpenseContract.TABLE_EXPENSE,
                                projection,
                                selection, selectionArgs,
                                null, null, orderBy);
                break;
        }

        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable
            ContentValues contentValues) {
        long id =
            helper.getWritableDatabase()
                .insert(ExpenseContract.TABLE_EXPENSE,
                        null, contentValues);
        if (id < 0)
            return null;
        else{
            return ContentUris.withAppendedId(uri, id);
        }
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowId = helper.getWritableDatabase()
                .delete(ExpenseContract.TABLE_EXPENSE,
                        selection, selectionArgs);
        return rowId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowCount = helper.getWritableDatabase()
                        .update(ExpenseContract.TABLE_EXPENSE,
                            contentValues,
                            selection, selectionArgs);
        return rowCount;
    }
}
