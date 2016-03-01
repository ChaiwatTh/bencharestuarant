package com.example.chaiwat_th.bencharestuarant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ShowMenuFood extends AppCompatActivity {

    //Explicit
    private TextView showOfficerTextView;
    private Spinner deskSpinner;
    private ListView foodListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu_food);

        //Bind Widget
        bindWidget();

        //Create Food ListView
        createFoodListView();

    }

    private void createFoodListView() {

        //Read All Data From SQLite
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name, MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MyManage.food_table, null);
        cursor.moveToFirst();

        String[] foodStrings = new String[cursor.getCount()];
        String[] priceStrings = new String[cursor.getCount()];
        String[] sourceStrings = new String[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            foodStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.colum_food));
            priceStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.colum_price));
            sourceStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.colum_source));

            cursor.moveToNext();
        }//for
        cursor.close();



    }//Create Food ListView

    private void bindWidget() {

        showOfficerTextView = (TextView) findViewById(R.id.textView);
        deskSpinner = (Spinner) findViewById(R.id.spinner);
        foodListView = (ListView) findViewById(R.id.listView);

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
