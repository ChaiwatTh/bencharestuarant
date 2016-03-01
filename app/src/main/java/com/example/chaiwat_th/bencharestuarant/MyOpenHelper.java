package com.example.chaiwat_th.bencharestuarant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chaiwat_th on 29/2/2559.
 */
public class MyOpenHelper extends SQLiteOpenHelper{

    public static final String database_name = "banja.db";
    private static final int database_version = 1;

    private static final String creTE_table_user = "create table userTABLE (" +
            "_id integer primary key, " +
            "User text, " +
            "Password text, " +
            "Name text);";

    private static final String create_table_food = "create table foodTABLE (" +
            "_id integer primary key, " +
            "Food text, " +
            "Price text, " +
            "Source text);";

    public MyOpenHelper(Context context) {

        super(context, database_name, null, database_version);

    } //Constructor

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(creTE_table_user);
        sqLiteDatabase.execSQL(create_table_food);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
} //Main Class
