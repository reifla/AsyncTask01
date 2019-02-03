package org.example.det.asynctask_01;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: starting Asynctask.");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/" +
                "topfreeapplications/limit=10/xml");
//        downloadData.execute("http://www.posh24.se/kandisar");
        Log.d(TAG, "onCreate: done.");
    }

    private static class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXml(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading.");
            }
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is: " + s);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);
        }

        private String downloadXml(String urlPath) {
            //Neue Methode der Class "DownloadData"
            //..wird von "doInBackground" aufgerufen
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXml: The responsecode was " + response);
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));

                int charsRead;                          //Anz der gelesenen Chars
                char[] inputBuffer = new char[500];     //Buffer für die gelesenen Zeichen
                while(true) {
                    charsRead = reader.read(inputBuffer);   //Reader liest in inputBuffer
                    if(charsRead < 0) {
                        break;                          //Wenn kleiner 0, dann sind wir am Ende
                    }
                    if(charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXml: invalid URL " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXml: IO-Exception reading data. " + e.getMessage());
//                e.printStackTrace();
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXml: Security Exception. Needs permission?" +
                        e.getMessage());
//                e.printStackTrace();          //das gibt noch mehr Infos raus
            }
            return null;
        }
    }
}
