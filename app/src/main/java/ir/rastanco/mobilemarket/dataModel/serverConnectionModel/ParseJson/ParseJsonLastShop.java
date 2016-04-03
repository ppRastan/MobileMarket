package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.ProductShop;

/**
 * Created by ShaisteS on 1394/11/3.
 * This Class Parse Last Shop information Json String
 */
public class ParseJsonLastShop {


    public ArrayList<ProductShop> getLastShop(String LastProductShop) {
        ArrayList<ProductShop> productsShop=new ArrayList<>();
        JSONObject json;
        try {
            json = new JSONObject(LastProductShop);
            JSONArray dataJsonArr = json.getJSONArray("orders");
            JSONObject jsonProduct=new JSONObject(String.valueOf(dataJsonArr.getJSONObject(0)));
            JSONArray jsonLastProduct=jsonProduct.getJSONArray("products");
            JSONObject last = jsonLastProduct.getJSONObject(0);
            for (int i=0;i<last.length();i++){
                JSONObject counterLastShop=new JSONObject(String.valueOf(last.getJSONObject(String.valueOf(i))));
                ProductShop aProductShop=new ProductShop();
                aProductShop.setInvoiceNumber(counterLastShop.getString("n"));
                aProductShop.setTimeStamp(counterLastShop.getString("d"));
                aProductShop.setInvoiceImageLink(counterLastShop.getString("i"));
                aProductShop.setInvoiceStatus(counterLastShop.getString("s"));
                productsShop.add(aProductShop);
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return productsShop;
    }
}
