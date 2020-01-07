package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 2/28/2017.
 */

public class databasehelper extends SQLiteOpenHelper {

    public BluetoothSocket socket=BluetoothActivity.socket;

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "patient_table";

    private static final String PD_ID = "ID";
    private static final String PD_NAME = "name";
    private static final String PD_PDID = "patientID";
    private static final String PD_AGE = "age";
    private static final String PD_CONTACT = "contact";
    private static final String PD_AILMENT = "ailment";
    private static final String PD_GENDER = "gender";
    private static final String ANALYSIS_X = "x";
    private static final String ANALYSIS_Y = "y";
    private static final String ANALYSIS_Z = "z";
    private static final String lastCOL = "timestamp";

    public databasehelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL2 +" TEXT, " + COL3 + " TEXT)";
//        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
    }


    public void createPatientTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PD_NAME +" TEXT, " + PD_PDID + " TEXT, " + PD_AGE + " TEXT, " + PD_CONTACT + " TEXT, "
                + PD_AILMENT + " TEXT, " + PD_GENDER + " TEXT, " + lastCOL + " NVARCHAR(30))";
        db.execSQL(query);
    }

    public boolean addPatientData(String name, String patientID, String age, String contact, String ailment, String gender, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PD_NAME, name);
        contentValues.put(PD_PDID, patientID);
        contentValues.put(PD_AGE, age);
        contentValues.put(PD_CONTACT, contact);
        contentValues.put(PD_AILMENT, ailment);
        contentValues.put(PD_GENDER, gender);
        contentValues.put(lastCOL, timestamp);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public void createAnalysisTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ANALYSIS_X + " TEXT, " + ANALYSIS_Y + " TEXT, " + ANALYSIS_Z + " TEXT )";
        db.execSQL(query);
    }


    public boolean addAnalysis(String x,String y,String z,String Table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANALYSIS_X,x);
        contentValues.put(ANALYSIS_Y,y);
        contentValues.put(ANALYSIS_Z,z);

        long result = db.insert(Table, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }

    }



    public Cursor analysisData(String Table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from"+ Table,null);
        return res;
    }



    public void deleteTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(query);
    }


    public Cursor getTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getData(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableName;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getItemID(String tableName, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + PD_ID + " FROM " + tableName +
                " WHERE " + PD_NAME + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + PD_NAME +
                " = '" + newName + "' WHERE " + PD_ID+ " = '" + id + "'" +
                " AND " + PD_NAME + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    public void deletePatient(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + PD_ID + " = '" + id + "'" +
                " AND " + PD_NAME + " = '" + name + "'";
//        Log.d(TAG, "deleteName: query: " + query);
//        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
//        db.delete(TABLE_NAME, COL2 + " = " + name,null);
    }

}