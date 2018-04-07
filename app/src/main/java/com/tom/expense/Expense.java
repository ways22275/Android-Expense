package com.tom.expense;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hank on 2018/3/3.
 */

public class Expense implements Parcelable {
    int id;
    String date;
    String info;
    int amount;
    int read;
    public Expense(){

    }

    public Expense(int id, String date, String info, int amount) {
        this.id = id;
        this.date = date;
        this.info = info;
        this.amount = amount;
    }

    public Expense(int id, String date, String info, int amount, int read) {
        this(id, date, info, amount);
        this.read = read;
    }

    public Expense(Cursor cursor) {
        id = cursor.getInt(
                cursor.getColumnIndex(ExpenseContract.COL_ID));
        date = cursor.getString(
                cursor.getColumnIndex(ExpenseContract.COL_DATE));
        info = cursor.getString(
                cursor.getColumnIndex(ExpenseContract.COL_INFO));
        amount = cursor.getInt(
                cursor.getColumnIndex(ExpenseContract.COL_AMOUNT));
        read = cursor.getInt(cursor.getColumnIndex(ExpenseContract.COL_READ));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ExpenseContract.COL_ID, id);
        values.put(ExpenseContract.COL_DATE, date);
        values.put(ExpenseContract.COL_INFO, info);
        values.put(ExpenseContract.COL_AMOUNT, amount);
        values.put(ExpenseContract.COL_READ, read);
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.date);
        dest.writeString(this.info);
        dest.writeInt(this.amount);
        dest.writeInt(this.read);
    }

    protected Expense(Parcel in) {
        this.id = in.readInt();
        this.date = in.readString();
        this.info = in.readString();
        this.amount = in.readInt();
        this.read = in.readInt();
    }

    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel source) {
            return new Expense(source);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
}
