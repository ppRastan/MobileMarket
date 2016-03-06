package ir.rastanco.mobilemarket.presenter.specialProductPresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Category;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverChangeFragment;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by shaisteS on 1394/12/16.
 */
public class SpecialLoadingFragment extends Fragment {

    private ServerConnectionHandler sch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View loadingView = inflater.inflate(R.layout.fragment_loading, null);
        Configuration.LoadingContext=getContext();
        sch=new ServerConnectionHandler(getContext());
        if (Configuration.productTableEmptyStatus && Configuration.connectionStatus) {
            final String[] jsonString = {""};
            Thread getProductInfoFromServerThread = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            // Wait given period of time or exit on touch
                            checkDbState();
                            jsonString[0] =getProductInfoFromServer("http://decoriss.com/json/get,com=product&newfromts=1352689345&cache=false");
                            wait(10);
                        }
                    } catch (InterruptedException ex) {
                    }
                    String timeStamp=addProductToTable(jsonString[0]);
                    sch.setSetting(timeStamp,
                            Configuration.LoadingContext.getResources().getString(R.string.firstArticleNumber),
                            Configuration.LoadingContext.getResources().getString(R.string.version),
                            timeStamp);
                    Configuration.productTableEmptyStatus=false;
                    ObserverChangeFragment.setChangeFragmentParameter(true);
                }
            };
            getProductInfoFromServerThread.start();
        }

        return loadingView;
    }

    public String getProductInfoFromServer(String urlProduct) {

        HttpURLConnection connection = null;
        InputStream is = null;
        String jsonString = "";
        try {
            URL url = new URL(urlProduct);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        Log.v("connect", "Connecte to Internet");
        int response = 0;
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
        Log.v("RequestGet", "Request Method Get");
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

    public String addProductToTable(String jsonString) {

        ArrayList<Product> allProduct;
        JSONArray dataJsonArr;
        ArrayList<String> imagePath;

        dataJsonArr = null;
        allProduct = new ArrayList<Product>();

        try {

            JSONObject json = new JSONObject(jsonString);
            dataJsonArr = json.getJSONArray("product");
            for (int i = 0; i < dataJsonArr.length(); i++) {
                JSONObject c = dataJsonArr.getJSONObject(i);
                imagePath = new ArrayList<String>();
                Product aProduct = new Product();
                aProduct.setTitle(c.getString("t"));
                aProduct.setId(Integer.parseInt(c.getString("id")));
                aProduct.setGroupId(Integer.parseInt(c.getString("gid")));
                aProduct.setPrice(Integer.parseInt(c.getString("p")));
                aProduct.setPriceOff(Integer.parseInt(c.getString("po")));
                //pp
                aProduct.setVisits(Integer.parseInt(c.getString("v")));
                aProduct.setMinCounts(Integer.parseInt(c.getString("mc")));
                aProduct.setBrandName(c.getString("brndname"));
                aProduct.setStock(Integer.parseInt(c.getString("stock")));
                aProduct.setQualityRank(c.getString("qr"));
                aProduct.setCommentsCount(Integer.parseInt(c.getString("cc")));
                aProduct.setCodeProduct(c.getString("n"));
                aProduct.setDescription(c.getString("d"));
                aProduct.setSellsCount(Integer.parseInt(c.getString("s")));
                aProduct.setTimeStamp(c.getString("ts"));
                aProduct.setUpdateTimeStamp(c.getString("update_ts"));

                aProduct.setShowAtHomeScreen(Integer.parseInt(c.getString("h")));
                aProduct.setWatermarkPath(c.getString("wm"));
                aProduct.setImagesMainPath(c.getString("ipath"));
                aProduct.setLinkInSite(c.getString("l"));
                for (int j = 0; j < 10; j++) {
                    String counter = "i" + j;
                    if (c.has(counter))
                        imagePath.add(c.getString(counter));
                }
                aProduct.setImagesPath(imagePath);
                allProduct.add(aProduct);
            }
            sch.addAllProductToTable(allProduct);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allProduct.get(0).getTimeStamp();
    }

    private void checkDbState() {

        ArrayList<Category> categories = new ArrayList<Category>();
        ArrayList<Article> articles = new ArrayList<Article>();

        //for add brandName to DataBase then update brandName filed
        //last version in server 1.3.9
        //version app that install in mobile is 1.0.0

        if (sch.getLastVersionInDB().equals("1.0.0")) {
            sch.reloadProduct("1352689345");
            sch.updateVersionApp("1.0.0.1");
        }
        if (sch.getLastVersionInDB().equals("1.3.9")) {
            sch.reloadProduct("1352689345");
            sch.updateVersionApp("1.3.9.1");
            sch.setLastUpdateTimeStamp();
        }

        if (sch.emptyDBCategory()) {
            categories = sch.getAllCategoryInfoURL("http://decoriss.com/json/get,com=allcats&cache=false");
            sch.addAllCategoryToTable(categories);
        } else
            sch.refreshCategories("http://decoriss.com/json/get,com=allcats&cache=false");

    }
}
