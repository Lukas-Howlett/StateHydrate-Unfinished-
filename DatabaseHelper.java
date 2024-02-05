package com.example.statehydrate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "people_table";
    private static final String COL1 = "ID"; //Technically Column 0
    private static final String COL2 = "name"; //Technically Column 1
    /*
    Generalize life-stage to 3 main categories: Adolescent/Teen, Adult, Elderly
     */
    private static final String COL3 = "lifestage"; //Technically Column 2
    private static final String COL4 = "bioSex"; //BIOLOGICAL MALE OR FEMALE (False = male; True = female) 3
    private static final String COL5 = "isAthlete"; //ATHLETE OR NOT (False = athlete; True = sedentary) 4
    private static final String COL6 = "isPregnant"; //PREGNANT OR NOT (Generally only for bio-females) 5
    private static final String COL7 = "isBreastfeeding"; //BREASTFEEDING OR NOT (False = not feeding; True = feeding) 6

    //FOUR MORE COLUMNS TO ADD
    //Calculated water recommendation
    private static final String COL8 = "calcWater";//7
    //Water consumed in current day (resets at end of day)
    private static final String COL9 = "curWater";//8
    //Water consumed per hour
    private static final String COL10 = "waterPerHour";//9
    //Hours in current day cycle / water currently consumed = water consumed per hour.


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    //CURRENT MUST FIX TABLE SYNTAX
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 +
                " TEXT, " + COL7 + " TEXT, " + COL8 + " TEXT, " + COL9 + " TEXT, " + COL10 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /*
    Eventually want to add String name, String lifestage, Boolean bio-sex, Boolean activity-level, Boolean pregnant,
    and Boolean breastfeeding
     */
    public boolean addData2(String name, String lifeStage, Boolean bioSex,
                            Boolean isAthlete, Boolean isPregnant, Boolean isBreastfeeding, Double calcWater,
                            Double curWater, Double waterperHour){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, name);
        Log.d(TAG, "addData: Adding " + name + " to " + TABLE_NAME);
        cv.put(COL3, lifeStage);
        Log.d(TAG, "addData: Adding " + lifeStage + " to " + TABLE_NAME);
        cv.put(COL4, bioSex);
        Log.d(TAG, "addData: Adding " + bioSex + " to " + TABLE_NAME);
        cv.put(COL5, isAthlete);
        Log.d(TAG, "addData: Adding " + isAthlete + " to " + TABLE_NAME);
        cv.put(COL6, isPregnant);
        Log.d(TAG, "addData: Adding " + isPregnant + " to " + TABLE_NAME);
        cv.put(COL7, isBreastfeeding);
        Log.d(TAG, "addData: Adding " + isBreastfeeding + " to " + TABLE_NAME);
        cv.put(COL8, calcWater);
        Log.d(TAG, "addData: Adding " + calcWater + " to " + TABLE_NAME);
        cv.put(COL9, curWater);
        Log.d(TAG, "addData: Adding " + curWater + " to " + TABLE_NAME);
        cv.put(COL10, waterperHour);
        Log.d(TAG, "addData: Adding " + curWater + " to " + TABLE_NAME);


        long result = db.insert(TABLE_NAME, null, cv);

        //If data inserted incorrectly, result will equal -1
        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean addData(String item){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    /*
     Returns all the data from the database
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //returns ID that matches the name that is passed in
    public  Cursor getItemID(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query" + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    public void updateLifestage(String newLifeStage, int id, String oldLifestage){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + newLifeStage + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL3 + " = '" + oldLifestage + "'";
        Log.d(TAG, "updateLifestage: query" + query);
        Log.d(TAG, "updateLifestage: Setting lifestage to " + newLifeStage);
        db.execSQL(query);
    }

    public void updateBioSex(Boolean newBioSex, int id, Boolean oldBioSex){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL4 +
                " = '" + newBioSex + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL4 + " = '" + oldBioSex + "'";
        Log.d(TAG, "updateBioSex: query" + query);
        Log.d(TAG, "updateBioSex: Setting bioSex to " + newBioSex);
        db.execSQL(query);
    }

    public void updateIsAthlete(Boolean newIsAthlete, int id, Boolean oldIsAthlete){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL5 +
                " = '" + newIsAthlete + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL5 + " = '" + oldIsAthlete + "'";
        Log.d(TAG, "updateIsAthlete: query" + query);
        Log.d(TAG, "updateIsAthlete: Setting isAthlete to " + newIsAthlete);
        db.execSQL(query);
    }

    public void updateIsPregnant(Boolean newIsPregnant, int id, Boolean oldIsPregnant){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL6 +
                " = '" + newIsPregnant + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL6 + " = '" + oldIsPregnant + "'";
        Log.d(TAG, "updateIsPregnant: query" + query);
        Log.d(TAG, "updateIsPregnant: Setting isPregnant to " + newIsPregnant);
        db.execSQL(query);
    }

    public void updateIsBreastfeeding(Boolean newIsBreastfeeding, int id, Boolean oldIsBreastfeeding){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL7 +
                " = '" + newIsBreastfeeding + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL7 + " = '" + oldIsBreastfeeding + "'";
        Log.d(TAG, "updateIsBreastfeeding: query" + query);
        Log.d(TAG, "updateIsBreastfeeding: Setting isBreastfeeding to " + newIsBreastfeeding);
        db.execSQL(query);
    }

    public void updateCurWater(Double newCurWater, int id, Double oldCurWater){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL9 +
                " = '" + newCurWater + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL9 + " = '" + oldCurWater + "'";
        Log.d(TAG, "updateCurWater: query" + query);
        Log.d(TAG, "updateCurWater: Setting curWater to " + newCurWater);
        db.execSQL(query);
    }

    public void updateWaterPerHour(Double newWaterPerHour, int id, Double oldWaterPerHour){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL10 +
                " = '" + newWaterPerHour + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL10 + " = '" + oldWaterPerHour + "'";
        Log.d(TAG, "updateWaterPerHour: query" + query);
        Log.d(TAG, "updateWaterPerHour: Setting waterPerHour to " + newWaterPerHour);
        db.execSQL(query);
    }

    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COL1 + " = '" + id + "'" + " AND " + COL2 + " = '" +
                name + "'";
        Log.d(TAG, "deleteName: query" + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

}//END CLASS
