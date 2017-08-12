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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.zip.Inflater;

import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    // >> ADD ICON IF NEEDED <<
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;

    private String myAppId = "dcb6553bfccc040683d9917eedd6cfbe";

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        //iconView = (ImageView) findViewById(R.id.thumbnailIcon); >> ADD ICON IF NEEDED <<
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText); // ?
        humidity = (TextView) findViewById(R.id.humidText);
        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);

        renderWeatherData("Colombo,LK");
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
            Log.v("Data : ",weather.currentCondition.getDescription());

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {

            super.onPostExecute(weather);

            //Formatting data the way I want to display
            DateFormat df = DateFormat.getTimeInstance();

            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String updateDate = df.format(new Date(weather.place.getLastupdate()));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());

            cityName.setText(weather.place.getCity()+","+weather.place.getCountry());
            temp.setText(""+tempFormat+"°C");
            humidity.setText("Humidity: "+weather.currentCondition.getHumidity()+"%");
            pressure.setText("Pressure: "+weather.currentCondition.getPressure()+"hPa");
            wind.setText("Wind: "+weather.wind.getSpeed()+"mps");
            sunrise.setText("Sunrise: "+sunriseDate);
            sunset.setText("Sunset: "+sunsetDate);
            updated.setText("Last Updated: "+updateDate);
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
