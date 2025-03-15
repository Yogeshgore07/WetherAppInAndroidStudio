package com.example.whetherapp;

import static java.lang.Math.floor;
import static java.lang.Math.round;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whetherapp.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    DatabaseHelper mydb;

    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchWeatherData("Latur");
        mydb = new DatabaseHelper(this);
        handler = new Handler(Looper.getMainLooper());
        updateTimeEverySecond();

        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    fetchWeatherData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchWeatherData(String cityName) {
        binding.progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build();

        apiInterface apiInterface = retrofit.create(apiInterface.class);
        Call<WeatherApp> response = apiInterface.getWeatherData(cityName, "OpenMapApiKey", "metrics");

        response.enqueue(new Callback<WeatherApp>() {
            @Override
            public void onResponse(Call<WeatherApp> call, Response<WeatherApp> response) {

                binding.progressBar.setVisibility(View.GONE);
                WeatherApp responseBody = response.body();
                if (response.isSuccessful() && responseBody != null) {
                    String temp = String.valueOf(round(responseBody.getMain().getTemp() - 274) + "°C");

                    binding.temp.setText(temp);
                    String humidity = String.valueOf(responseBody.getMain().getHumidity());
                    binding.humidity.setText(humidity + "%");
                    binding.wind.setText(String.valueOf(responseBody.getWind().getSpeed()) + "m/s");
                    if (responseBody.getWeather() != null && !responseBody.getWeather().isEmpty()) {
                        String condition = responseBody.getWeather().get(0).getMain();
                        binding.condition.setText(condition);
                        binding.weather.setText(condition);
                        changeAsCondition(condition);
                    }

                    binding.sunrise.setText(String.valueOf(time(responseBody.getSys().getSunrise())) + "AM");
                    binding.sunset.setText(String.valueOf(time(responseBody.getSys().getSunset())) + "PM");
                    binding.Longtitude.setText(String.valueOf("Longtitude : " + responseBody.getCoord().getLon()) + "°");
                    binding.Latitude.setText(String.valueOf("Latitude : " + responseBody.getCoord().getLat()) + "° ");
                    binding.pressure.setText(String.valueOf(responseBody.getMain().getPressure()) + "hPa");
                    binding.cityName.setText(cityName);
                    binding.day.setText(getDayName());
                    binding.date.setText(getDate());

                    boolean x = mydb.insert(cityName, getDate(), getCurrentTime(), responseBody.getCoord().getLon(), responseBody.getCoord().getLat(), temp, responseBody.getWeather().get(0).getMain(), responseBody.getMain().getHumidity(), responseBody.getWind().getSpeed(), time(responseBody.getSys().getSunrise()), time(responseBody.getSys().getSunset()), responseBody.getMain().getPressure());


                } else {
                    binding.imageView2.setImageResource(R.drawable.error);
                    binding.cityName.setText("Enter Proper Name");
                    binding.sunrise.setText("00:00AM");
                    binding.sunset.setText("00:00PM");
                    binding.Longtitude.setText("00°");
                    binding.Latitude.setText("00°");
                    binding.pressure.setText("00hPa");
                    binding.condition.setText("Error");
                    binding.weather.setText("Error");
                    binding.temp.setText("00°C");
                    binding.wind.setText("00m/s");
                    binding.humidity.setText("00%");

                }

            }

            @Override
            public void onFailure(Call<WeatherApp> call, Throwable t) {

            }
        });


    }

    private void changeAsCondition(String condition) {
        switch (condition) {
            case "Clear":
                binding.imageView2.setImageResource(R.drawable.clear);
                binding.condtionImg.setImageResource(R.drawable.clear);
                break;
            case "Sunny":
                binding.imageView2.setImageResource(R.drawable.clear);
                binding.condtionImg.setImageResource(R.drawable.clear);
                break;
            case "Clear Sky":
                binding.imageView2.setImageResource(R.drawable.clear);
                binding.condtionImg.setImageResource(R.drawable.clear);
                break;
            case "Clouds":
                binding.imageView2.setImageResource(R.drawable.cloud);
                binding.condtionImg.setImageResource(R.drawable.cloud);
                break;
            case "Partly Clouds":
                binding.imageView2.setImageResource(R.drawable.cloud);
                binding.condtionImg.setImageResource(R.drawable.cloud);
                break;
            case "Overcast":
                binding.imageView2.setImageResource(R.drawable.cloud);
                binding.condtionImg.setImageResource(R.drawable.cloud);
                break;
            case "Mist":
                binding.imageView2.setImageResource(R.drawable.mist);
                binding.condtionImg.setImageResource(R.drawable.mist);
                break;
            case "Foggy":
                binding.imageView2.setImageResource(R.drawable.cloud);
                binding.condtionImg.setImageResource(R.drawable.cloud);
                break;
            case "Light Rain":
                binding.imageView2.setImageResource(R.drawable.rain);
                binding.condtionImg.setImageResource(R.drawable.rain);
                break;
            case "Rain":
                binding.imageView2.setImageResource(R.drawable.rain);
                binding.condtionImg.setImageResource(R.drawable.rain);
                break;
            case "Drizzle":
                binding.imageView2.setImageResource(R.drawable.rain);
                binding.condtionImg.setImageResource(R.drawable.rain);
                break;
            case "Moderate Rain":
                binding.imageView2.setImageResource(R.drawable.rain);
                binding.condtionImg.setImageResource(R.drawable.rain);
                break;
            case "Showers":
                binding.imageView2.setImageResource(R.drawable.rain);
                binding.condtionImg.setImageResource(R.drawable.rain);
                break;
            case "Heavy Rain":
                binding.imageView2.setImageResource(R.drawable.rain);
                binding.condtionImg.setImageResource(R.drawable.rain);
                break;
            case "Light Snow":
                binding.imageView2.setImageResource(R.drawable.snow);
                binding.condtionImg.setImageResource(R.drawable.snow);
                break;
            case "Moderate Snow":
                binding.imageView2.setImageResource(R.drawable.snow);
                binding.condtionImg.setImageResource(R.drawable.snow);
                break;
            case "Heavy Snow":
                binding.imageView2.setImageResource(R.drawable.snow);
                binding.condtionImg.setImageResource(R.drawable.snow);
                break;
            case "Blizzard":
                binding.imageView2.setImageResource(R.drawable.snow);
                binding.condtionImg.setImageResource(R.drawable.snow);
                break;
            default:
                binding.imageView2.setImageResource(R.drawable.clear);
                binding.condtionImg.setImageResource(R.drawable.clear);


        }
    }


    public String time(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000));
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getDayName() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(new Date());
    }





    private void updateTimeEverySecond() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = getCurrentTime();
                binding.time.setText(currentTime);


                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }


    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}


