package com.example.navin.theweatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;

import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.BadWeather;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView description;
    private boolean weatherCondition;

    private String myAppId = "dcb6553bfccc040683d9917eedd6cfbe";

    Weather weather = new Weather();
    BadWeather badWeather = new BadWeather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        description = (TextView) findViewById(R.id.cloudText); // ?

        renderWeatherData("Spokane,US");
        //Colombo,LK
        //Spokane,US
    }

    public void renderWeatherData(String city){

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city+"&appid="+myAppId}); //FIX if needed
        //weatherTask.execute(new String[]{city+"&appid=dcb6553bfccc040683d9917eedd6cfbe"});

    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            //data hold the whole StringBuffer that we returned from WeatherHttpClient class
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));
            weather = JSONWeatherParser.getWeather(data);

            //Log.v("Data : ",weather.place.getCity());
            //Log.v("Data : ",weather.currentCondition.getDescription());
            String weatherSample = weather.currentCondition.getDescription();

            if ((badWeather.isCloudy(weatherSample)) || (badWeather.isRaining(weatherSample))){
                weatherCondition = true;
            }
            else{
                weatherCondition = false;
            }

            Log.v("Good or Bad : ", String.valueOf(weatherCondition));

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {

            super.onPostExecute(weather);

            cityName.setText(weather.place.getCity()+","+weather.place.getCountry());
            description.setText("Condition: "+weather.currentCondition.getCondition()
                    +"("+weather.currentCondition.getDescription()+")");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.change_cityId){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
