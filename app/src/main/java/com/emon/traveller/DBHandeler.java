package com.emon.traveller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.emon.traveller.model.Model;
import com.emon.traveller.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DBHandeler extends SQLiteOpenHelper {
    //Database Version
    private static final int DATABASE_VERSION = 2;
    //Database Name
    private static final String DATABASE_NAME = "COST";
    //Table Name
    private static final String TABLE_DETAILS = "DetailsTable";
    private static final String TABLE_SHOW = "ShowTable";
    private static final String TABLE_NOTE = "NoteTable";
    //Column Name
    private static final String KEY_ID = "ID";
    private static final String KEY_REASON = "reason";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";

    private static final String SHOW_ID = "id";
    private static final String SHOW_BUDJET = "budjet";
    private static final String SHOW_EXPENSE = "expense";


    private static final String NOTE_ID = "ID";
    private static final String NOTE_SUBJECT = "subject";
    private static final String NOTE_DETAILS = "details";
    private static final String NOTE_TIME = "time";
    private static final String NOTE_DATE = "date";

    private static Model model;
    Context context;


    public DBHandeler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context = this.context;
    }

    //Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SHOW_TABLE = "CREATE TABLE " + TABLE_SHOW + "("
                + SHOW_ID + " INTEGER PRIMARY KEY," + SHOW_BUDJET + " TEXT," + SHOW_EXPENSE + " TEXT" + ")";
        db.execSQL(CREATE_SHOW_TABLE);


        String CREATE_DETAILS_TABLE = "CREATE TABLE " + TABLE_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_REASON + " TEXT," + KEY_AMOUNT + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT" + ")";
        db.execSQL(CREATE_DETAILS_TABLE);


        String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
                + NOTE_ID + " INTEGER PRIMARY KEY," + NOTE_SUBJECT + " TEXT," + NOTE_DETAILS + " TEXT," + NOTE_DATE + " TEXT," + NOTE_TIME + " TEXT" + ")";
        db.execSQL(CREATE_NOTE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(db);
    }

    //Insert Value
    public void adddata(Context context, String reason, String amount, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REASON, reason);
        values.put(KEY_AMOUNT, amount);
        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);
        db.insert(TABLE_DETAILS, null, values);
        db.close();
    }

    //Get Row Count
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DETAILS;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    //Delete Query
    public void delete(int id) {
        String deleteQuery = "DELETE FROM " + TABLE_DETAILS + " where " + KEY_ID + "= " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery);
    }

    //AllDelete Query
    public void Alldelete() {
        String deleteQuery = "DELETE FROM " + TABLE_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery);
    }


    //Update Query
    public boolean updateData(String id, String reason, String amount, String date, String time) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_AMOUNT, amount);
        contentValues.put(KEY_REASON, reason);
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME, time);
        contentValues.put(KEY_ID, id);

        db.update(TABLE_DETAILS, contentValues, KEY_ID + " = ?", new String[]{id});


        return true;
    }


    //Get data
    public List<Model> getData() {
        int position = 0;
        String selectQuery = "SELECT * FROM " + TABLE_DETAILS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Model> models = new ArrayList<Model>();
        if (cursor.moveToFirst()) {
            do {
                Model list = new Model();
                list.setId(Integer.parseInt(cursor.getString(0)));
                list.setReason(cursor.getString(1));
                list.setAmount(cursor.getString(2));
                list.setDate(cursor.getString(3));
                list.setTime(cursor.getString(4));
                models.add(position, list);
            } while (cursor.moveToNext());
        }
        return models;
    }

    //get income expense data
    Cursor getIncExpData() {
        String selectQuery = "SELECT * FROM " + TABLE_DETAILS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    //Insert show Value
    public void addShowdata(Context context, String budjet, String expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SHOW_BUDJET, budjet);
        values.put(SHOW_EXPENSE, expense);
        db.insert(TABLE_SHOW, null, values);
        db.close();
    }

    //update show Value
    public boolean updateShowDate(String id, String budjet, String expense) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHOW_ID, id);
        contentValues.put(SHOW_BUDJET, budjet);
        contentValues.put(SHOW_EXPENSE, expense);

        db.update(TABLE_SHOW, contentValues, SHOW_ID + " = ?", new String[]{id});
        //  Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show();

        return true;
    }

    //get show Value
    public Cursor getShowData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SHOW;
        Cursor data = db.rawQuery(selectQuery, null);
        return data;
    }

    //AllDelete Query
    public void showDatadelete() {
        String deleteQuery = "DELETE FROM " + TABLE_SHOW;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery);
    }


    ///NOTE

    //Insert Note Value
    public void addNoteData(Context context, String subject, String details, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_SUBJECT, subject);
        values.put(NOTE_DETAILS, details);
        values.put(NOTE_DATE, date);
        values.put(NOTE_TIME, time);
        db.insert(TABLE_NOTE, null, values);
        db.close();
    }

    //Get Row Count
    public int getNoteCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTE;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    //Delete Query
    public void deleteNote(int id) {
        String deleteQuery = "DELETE FROM " + TABLE_NOTE + " where " + NOTE_ID + "= " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery);
    }


    //Update Query
    public boolean updateNoteData(String id, String subject, String details, String date, String time) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_SUBJECT, subject);
        values.put(NOTE_DETAILS, details);
        values.put(NOTE_DATE, date);
        values.put(NOTE_TIME, time);
        values.put(NOTE_ID, id);

        db.update(TABLE_NOTE, values, NOTE_ID + " = ?", new String[]{id});


        return true;
    }


    //Get data
    public List<Note> getNoteData() {
        int position = 0;
        String selectQuery = "SELECT * FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Note> notes = new ArrayList<Note>();
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNoteid(Integer.parseInt(cursor.getString(0)));
                note.setSubject(cursor.getString(1));
                note.setDetails(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                notes.add(position, note);
            } while (cursor.moveToNext());
        }
        return notes;
    }


    //AllDelete Query
    public void allNoteDatadelete() {
        String deleteQuery = "DELETE FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery);
    }


}