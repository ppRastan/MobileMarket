package ir.rastanco.mobilemarket.dataModel.serverHandler;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ShaisteS 12/30/2015.
 * This Class Connect to JSON File Url and return String
 */
public class GetJsonFile extends AsyncTask<String,String,String> {

    private static InputStream is = null;
    private static String jsonString = "";
    private HttpURLConnection connection;


    @Override
    protected String doInBackground(String... params) {
        try {
            URL url= new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        Log.v("connect", "Connecte to Internet");
        int response=0;
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            response = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("RequestGet","Request Method Get");
        Log.v("Response", Integer.toString(response));
        try {
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonString = sb.toString();

        } catch (Exception e) {
            Log.e("TAG", "Error converting result " + e.toString());
        }
        return jsonString;
    }
}

