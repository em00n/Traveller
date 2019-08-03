package com.emon.traveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.emon.traveller.model.WeatherModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    TextView textView1, textView2, textView3, textView4;
    ImageView imageView1, imageView2;
    OpenWeatherMap openWeatherMap;
    private FusedLocationProviderClient client;
    private LocationCallback callback;
    private LocationRequest request;
    int t;
    double lat, lon;
    String currentTime, currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        textView1 = findViewById(R.id.t1);
        textView3 = findViewById(R.id.t3);
        textView4 = findViewById(R.id.t4);
        imageView2 = findViewById(R.id.i2);
        imageView2.setImageResource(R.drawable.celsius);


        openWeatherMap = WeatherApiClient.getClient().create(OpenWeatherMap.class);

        client = LocationServices.getFusedLocationProviderClient(this);
        request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000)
                .setFastestInterval(1000);

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();

                }

            }
        };


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return;
            }
        }

        client.requestLocationUpdates(request, callback, null);

        getCurrentWeather();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                currentTime = sdf.format(new Date());
                SimpleDateFormat sdf1 = new SimpleDateFormat("EE", Locale.ENGLISH);
                currentDay = sdf1.format(new Date());

                textView3.setText(currentDay + "  " + currentTime);

                getCurrentWeather();
                handler.postDelayed(this, 15000);
            }
        }, 15000);

    }


    @Override
    protected void onRestart() {
        getCurrentWeather();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        getCurrentWeather();
        super.onResume();
    }

    private void getCurrentWeather() {
        Call<WeatherModel> call = openWeatherMap.getCurrentWeather(String.valueOf(lat), String.valueOf(lon), WeatherApiClient.appliedId);
        //Call<WeatherModel> call = openWeatherMap.getCurrentWeather("data/2.5/weather?q=dhaka&appid=73bb1dd44b0cc93ec1b695164df7e092");

        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (!response.isSuccessful()) {
                    //  Toast.makeText(WeatherActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }
                WeatherModel weatherModel = response.body();
                String temp = String.valueOf(weatherModel.main.temp);
                double conver = Double.parseDouble(temp);
                double value = conver - 273.15;
                int tempr = (int) value;
                t = tempr;

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                currentTime = sdf.format(new Date());
                SimpleDateFormat sdf1 = new SimpleDateFormat("EE", Locale.ENGLISH);
                currentDay = sdf1.format(new Date());

                textView1.setText(weatherModel.name);
                textView3.setText(currentDay + "  " + currentTime);
                textView4.setText(String.valueOf(tempr));


            }

            double conver(String v) {
                double conver = Double.parseDouble(v);
                double value = conver - 273.15;
                return value;
            }


            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                //  Toast.makeText(WeatherActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                }

                client.requestLocationUpdates(request, callback, null);
            }
        }
    }
}
