package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ShaisteS on 04/06/2016.
 */
public class GetFileNoAsync {
    private InputStream is = null;
    private String jsonString = "";
    private HttpURLConnection connection;

    public String getFile(String urlFile){

        try {
            URL url= new URL(urlFile);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        Log.v("connect", "Connect to Internet");
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            jsonString = sb.toString();

        } catch (Exception e) {
            Log.e("TAG", "Error converting result " + e.toString());
        }
        return jsonString;
    }

}
