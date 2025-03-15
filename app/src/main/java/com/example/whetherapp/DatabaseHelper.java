package com.example.whetherapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context) {
            super(context, "weatherData", null, 1);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE data_weather (city TEXT, date TEXT, time TEXT, longtitude REAL, latitude REAL, temprature TEXT, condition TEXT, humidity INTEGER, wind REAL, sunrise TEXT, sunset TEXT, pressure INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE data_weather");
            onCreate(db);
        }

        public boolean insert(String city, String date, String time, double longtitude, double latitude, String temprature, String condition, long humidity, double wind, String sunrise, String sunset, long pressure){

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("city",city);
            cv.put("date",date);
            cv.put("time",time);
            cv.put("longtitude",longtitude);
            cv.put("latitude",latitude);
            cv.put("temprature",temprature);
            cv.put("condition",condition);
            cv.put("humidity",humidity);
            cv.put("wind",wind);
            cv.put("sunrise",sunrise);
            cv.put("sunset",sunset);
            cv.put("pressure",pressure);
            long result = db.insert("data_weather",null,cv);
            if (result == -1) {
                Log.d("INSERT_CHECK", "Data insertion failed");
                return false;
            }else {
                Log.e("INSERT_CHECK", "Data insertion Succesfully");
                Log.d("INSERT_CHECK", "City: " + city);
                Log.d("INSERT_CHECK", "Date: " + date);
                Log.d("INSERT_CHECK", "Time: " + time);
                Log.d("INSERT_CHECK", "Longitude: " + longtitude);
                Log.d("INSERT_CHECK", "Latitude: " + latitude);
                Log.d("INSERT_CHECK", "Temperature: " + temprature);
                Log.d("INSERT_CHECK", "Condition: " + condition);
                Log.d("INSERT_CHECK", "Humidity: " + humidity);
                Log.d("INSERT_CHECK", "Wind: " + wind);
                Log.d("INSERT_CHECK", "Sunrise: " + sunrise);
                Log.d("INSERT_CHECK", "Sunset: " + sunset);
                Log.d("INSERT_CHECK", "Pressure: " + pressure);
                return true;
            }
        }
    }

