package com.example.labwebserviceandasynctask;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JokeViewModel extends AndroidViewModel { // extending from AndroidViewModel because going to use AsyncTask

    // Attribute

    private MutableLiveData<String> resultJSON;
    private MutableLiveData<String> setup;
    private MutableLiveData<String> punchLine;

    // Constructor

    public JokeViewModel(@NonNull Application application) {
        super(application);
        resultJSON = null;
    }

    // Get & Set for setup and punchLine


    // Live Data

    public LiveData<String> getResultJSON()
    {
        if(resultJSON == null)
        {
            resultJSON = new MutableLiveData<String>();
        }
        return resultJSON;
    }

    public LiveData<String> getSetup()
    {
        if(setup == null)
        {
            setup = new MutableLiveData<String>();
        }
        return setup;
    }

    public LiveData<String> getPunchLine()
    {
        if(punchLine == null)
        {
            punchLine = new MutableLiveData<String>();
        }
        return punchLine;
    }

    public void setSetup(String s)
    {
        if(setup == null)
        {
            setup = new MutableLiveData<String>();
        }
        setup.setValue(s);
    }

    public void setPunchLine(String s)
    {
        if(punchLine == null)
        {
            punchLine = new MutableLiveData<String>();
        }
        punchLine.setValue(s);

    }

    // AsyncTask

    public void loadData()
    {
        // Instead of creating a whole new class you can created an instance of AsyncTask in code
        // This code creates the class then executes it
        AsyncTask<String, Void, String> asyncTask = new AsyncTask <String, Void, String>()
        {


            @Override
            protected String doInBackground(String... strings)
            {
                String result = "";


                // Call the web service

                // HttpURLConnection Obj
                HttpURLConnection urlConnection = null;

                try
                {
                    // URL for random joke
                    String urlString = "https://official-joke-api.appspot.com/random_joke";

                    // URL object initialized and url string passed as parameter to constructor
                    URL url = new URL(urlString);

                    // Open the URL connection and initialize it to HttpURLConnection obj
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // Get the JSON  from .getInputStream and add it to Input Stream obj
                    // returns a string in JSON format
                    InputStream inStream = urlConnection.getInputStream();

                    // Add code to get data from input stream here…
                    // Create BufferedReader Obj Construct with InputStreamReader
                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream));

                    // Create StringBuilder obj to help get read JSON data
                    StringBuilder total = new StringBuilder();


                    for (String line; (line = buffReader.readLine()) != null; )
                    {
                        total.append(line).append('\n');
                    }

                    // Set the result string
                    result = total.toString();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    // If the url connection was open disconnect it here
                    if (urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);



                // onPostExecute should just set the value of the view model's resultJSON
                resultJSON.setValue(result);

                try
                {
                    String joke;
                    String punchline;

                    // Create an instance and pass in the JSON string
                    JSONObject jsonObject = new JSONObject(result);
                    joke = jsonObject.getString("setup");
                    //JSONArray jokeJSONArray = jsonObject.getJSONArray("setup");
                    //JSONObject jokeObject = jokeJSONArray.getJSONObject(2);


                    // Pass the joke string to the set setup, mutable live data variable initialization
                    //setSetup(joke);

                    // Create an instance and pass in the JSON string
                    JSONObject jsonObjectPunchline = new JSONObject(result);
                    punchline = jsonObjectPunchline.getString("punchline");
                    //JSONArray punchlineJSONArray = jsonObjectPunchline.getJSONArray("punchline");
                    //JSONObject punchlineObject = punchlineJSONArray.getJSONObject(4);


                    // Pass the punchline string to the set punchline, mutable live data variable initialization
                    //setPunchLine(punchline);

                    //setup.setValue(joke);
                    //punchLine.setValue(punchline);

                    setup.setValue(joke);
                    punchLine.setValue(punchline);


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        // Now execute it
        asyncTask.execute();

    }
}
