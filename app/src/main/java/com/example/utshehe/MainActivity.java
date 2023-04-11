package com.example.utshehe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    final String API_ID = "104ca9ffdd53f5ad89d60f8118ac6c0e";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    ImageView Back;

    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temperature,Humidity, WinsSpeed;;
    ImageView mweatherIcon;

    RelativeLayout mCityFinder;


    LocationManager mLocationManager;
    LocationListener mLocationListner;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Back = findViewById(R.id.back);
        weatherState = findViewById(R.id.weather);
        NameofCity = findViewById(R.id.city);
        Temperature = findViewById(R.id.suhu);
        Humidity = findViewById(R.id.humidity_content);
        WinsSpeed = findViewById(R.id.winspeed_content);


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("city");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else
        {
            Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
        }


    }


    private void getWeatherForNewCity(String city)
    {
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",API_ID);
        letsdoSomeNetworking(params);

    }



    private  void letsdoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this,"Data Get Success",Toast.LENGTH_SHORT).show();

                weatherData weatherD=weatherData.fromJson(response);
                updateUI(weatherD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });



    }
    private  void updateUI(weatherData weather){


        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        Humidity.setText(weather.getmHumidity());
        WinsSpeed.setText(weather.getmWinsSpeed());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());



    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}