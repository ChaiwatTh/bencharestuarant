package com.example.chaiwat_th.bencharestuarant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by chaiwat_th on 29/2/2559.
 */
public class MyManage {

    //Explicit
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String user_table = "userTABLE";
    public static final String colum_id = "_id";
    public static final String colum_user = "User";
    public static final String colum_pass = "Password";
    public static final String colum_name = "Name";

    public static final String food_table = "foodTABLE";
    public static final String colum_food = "Food";
    public static final String colum_price = "Price";
    public static final String colum_source = "Source";

    public static final String order_table = "orderTABLE";
    public static final String colum_officer = "Officer";
    public static final String colum_desk = "Desk";
    public static final String colum_amount = "Amount";

    public MyManage(Context context) {

        //Create Database
        myOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = myOpenHelper.getWritableDatabase();
        readSqLiteDatabase = myOpenHelper.getReadableDatabase();

    }//Contructor

    public String[] searchUser(String strUser) {

        try {

            String[] resultStrings = null;
            Cursor cursor = readSqLiteDatabase.query(user_table,
                    new String[]{colum_id, colum_user, colum_pass, colum_name},
                    colum_user + "=?",
                    new String[]{String.valueOf(strUser)},
                    null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    resultStrings = new String[4];
                    for (int i = 0; i < 4; i++) {
                        resultStrings[i] = cursor.getString(i);
                    }
                }
            }
            cursor.close();
            return resultStrings;
        } catch (Exception e) {
            return null;
        }

        //return new String[0];
    }

    public long addFood(String strFood, String strprice, String strSource) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(colum_food, strFood);
        contentValues.put(colum_price, strprice);
        contentValues.put(colum_source, strSource);

        return writeSqLiteDatabase.insert(food_table, null, contentValues);
    }

    //Add Value
    public long addUser(String strUser,String strPassword, String strName) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(colum_user, strUser);
        contentValues.put(colum_pass, strPassword);
        contentValues.put(colum_name, strName);

        return writeSqLiteDatabase.insert(user_table, null, contentValues);
    }

}//Main Class
