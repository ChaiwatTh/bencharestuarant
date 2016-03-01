package com.example.chaiwat_th.bencharestuarant;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private MyManage myManage;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();

        //Request Database
        myManage = new MyManage(this);

        //Test add Value
        //testAddValue();

        //Delete All SQLite
        deleteAllSQLite();
        
        //Synchonize JSON to SQLite
        synJSONSqLite();

    }

    public void clickLogin(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (userString.equals("") || passwordString.equals("") ) {

            MyAlertDialog myAlertDialog = new MyAlertDialog();
            myAlertDialog.myDialog(MainActivity.this, "มีช่องว่าง", "กรุณากรอกทุกช่อง !!");

        } else {
            checkUser(userString, passwordString);
        }
    }

    private void checkUser(String userString, String passwordString) {
        try {
            String[] myResultStrings = myManage.searchUser(userString);
            Log.d("Bencha", "Name : " + myResultStrings[3]);

            if (passwordString.equals(myResultStrings[2])) {
                welcom(myResultStrings[3]);
            } else {
                MyAlertDialog myAlertDialog = new MyAlertDialog();
                myAlertDialog.myDialog(MainActivity.this, "Fail !", "Password fail !!");
            }
        } catch (Exception e) {
            MyAlertDialog myAlertDialog = new MyAlertDialog();
            myAlertDialog.myDialog(MainActivity.this, "Fail !", "Again !!");
        }
    }

    private void welcom(String myResultString){
        Toast.makeText(MainActivity.this, "ยินดีต้อนรับ " + myResultString, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, ShowMenuFood.class);
        intent.putExtra("Officer", myResultString);
        startActivity(intent);
        finish();
    }

    private void bindWidget() {

        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
    }

    private void synJSONSqLite() {

        //Connected http
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        int intTimes = 0;
        while (intTimes <= 1) {

            //1. Create InputStream
            InputStream inputStream = null;
            String[] urlJSON = new String[2];
            urlJSON[0] = "http://swiftcodingthai.com/29feb/php_get_data_bird.php";
            urlJSON[1] = "http://swiftcodingthai.com/29feb/php_get_food.php";
            HttpPost httpPost = null;

            try {

                HttpClient httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(urlJSON[intTimes]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

            }catch (Exception e) {
                Log.d("Bencha", "InputStream ==> " + e.toString());
            }

            //2. Create JSON String
            String strJSON = null;

            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String strLine = null;

                while ((strLine = bufferedReader.readLine()) != null) {

                    stringBuilder.append(strLine);

                }
                inputStream.close();
                strJSON = stringBuilder.toString();

            } catch (Exception e) {
                Log.d("Bencha", "JSON String ==> " + e.toString());
            }

            //3. Update to SQLite
            try {

                JSONArray jsonArray = new JSONArray(strJSON);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    switch (intTimes) {

                        case 0:
                            String strUser = jsonObject.getString(MyManage.colum_user);
                            String strPass= jsonObject.getString(MyManage.colum_pass);
                            String strName = jsonObject.getString(MyManage.colum_name);

                            myManage.addUser(strUser, strPass, strName);

                            break;
                        case 1:
                            String strFood = jsonObject.getString(MyManage.colum_food);
                            String strPrice = jsonObject.getString(MyManage.colum_price);
                            String strSource = jsonObject.getString(MyManage.colum_source);

                            myManage.addFood(strFood, strPrice, strSource);

                            break;
                    }

                }

            } catch (Exception e) {
                Log.d("Bencha", "Update ==> " + e.toString());
            }

            intTimes += 1;

        }

    }

    private void deleteAllSQLite() {

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name, MODE_PRIVATE, null);
        sqLiteDatabase.delete(MyManage.food_table, null, null);
        sqLiteDatabase.delete(MyManage.user_table, null, null);

    }

    private void testAddValue() {

        myManage.addUser("testUser", "12345", "User123");
        myManage.addFood("ไข่เจียว", "100", "urlFood");

    }
}
