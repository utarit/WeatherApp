package com.example.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String api_key = "0155462dd44c0f06e9dcf65145996d57";

    TextView weatherTextView;
    EditText inputText;

    public class DownloadTsk extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";


            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch(Exception e){

                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String weatherText = "";

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                for(int i = 0; i <arr.length(); i++){
                    JSONObject part = arr.getJSONObject(i);

                    weatherText += String.format("%s: %s\n", part.getString("main"), part.getString("description"));
                }

                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                weatherTextView.setText(weatherText);

            } catch (Exception e){
                Log.i("Search", "NOT FOUND A CITY");
                Toast.makeText(getApplicationContext(), "CITY IS NOT FOUND!",  Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void searchClick(View view){
        DownloadTsk task = new DownloadTsk();

        task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + inputText.getText().toString() +"&appid=" + api_key);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.dataText);
        inputText = findViewById(R.id.editText);
    }
}
