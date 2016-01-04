package ir.rastanco.mobilemarket.dataModel.serverHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ShaisteS on 1394/10/14.
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    private String urlString;
    private HttpURLConnection connection;
    private InputStream is;
    private Bitmap bitmapImage;


    public DownloadImage() {
    }

    protected Bitmap doInBackground(String... urls) {
        urlString=urls[0];
        try {
            URL url= new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        is = null;
        try {
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmapImage = BitmapFactory.decodeStream(is);

        return bitmapImage;
    }

}
