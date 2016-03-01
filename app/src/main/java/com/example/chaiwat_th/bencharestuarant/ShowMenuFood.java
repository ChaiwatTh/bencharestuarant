package com.example.chaiwat_th.bencharestuarant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ShowMenuFood extends AppCompatActivity {

    //Explicit
    private TextView showOfficerTextView;
    private Spinner deskSpinner;
    private ListView foodListView;

    private String officerString, deskString, orderFoodString, amountString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu_food);

        //Bind Widget
        bindWidget();

        //ShoView
        showView();

        //Create Desk Spinner
        createDeskSpinner();

        //Create Food ListView
        createFoodListView();
    }

    private void createDeskSpinner() {

        final String[] deskStrings = {"โต๊ะที่ 1", "โต๊ะที่ 2", "โต๊ะที่ 3", "โต๊ะที่ 4", "โต๊ะที่ 5", "โต๊ะที่ 6", "โต๊ะที่ 7", "โต๊ะที่ 8", "โต๊ะที่ 9"};
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deskStrings);
        deskSpinner.setAdapter(stringArrayAdapter);

        deskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                deskString = deskStrings[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                deskString = deskStrings[0];
            }
        });

    }

    private void showView() {

        officerString = getIntent().getStringExtra("Officer");
        showOfficerTextView.setText(officerString);

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

        FoodAdapter foodAdapter = new FoodAdapter(ShowMenuFood.this, foodStrings, priceStrings, sourceStrings);
        foodListView.setAdapter(foodAdapter);

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
