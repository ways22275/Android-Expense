package com.tom.expense;

import android.net.Uri;

/**
 * Created by hank on 2018/3/3.
 */

public class ExpenseContract {
    public static final String AUTHORITY = "com.tom.expense";
    public static final String TABLE_EXPENSE = "exp";
    public static final String COL_ID = "_id";
    public static final String COL_DATE = "cdate";
    public static final String COL_INFO = "info";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_READ = "read";
    public static final String CREATE_SQL = "create table " + TABLE_EXPENSE +
            " ( " + COL_ID + " INTEGER PRIMARY KEY, " +
            COL_DATE + " TEXT, " +
            COL_INFO + " TEXT, " +
            COL_AMOUNT + " INTEGER, " +
            COL_READ + " INTEGER)";

    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme("content")
            .authority(AUTHORITY)
            .path(TABLE_EXPENSE)
            .build();

}
