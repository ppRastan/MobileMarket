package ir.rastanco.mobilemarket.utility;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ShaisteS on 1395/1/8.
 * This class include utility Method
 */
public class Utilities {

    private static Utilities utility = new Utilities();

    public static Utilities getInstance() {
        if (utility != null) {
            return utility;
        }
        else return new Utilities();
    }

    public ArrayList<String> getPriceFilterTitle(){
        ArrayList<String> priceFilter = new ArrayList<String>();
        priceFilter.add(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto1million));
        priceFilter.add(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto5million));
        priceFilter.add(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto10million));
        priceFilter.add(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.over10million));
        return priceFilter;
    }
    public int convertPriceTitleToInt(String priceTitle){
        int price=0;
        if(priceTitle.equals(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto1million)))
            price=1000000;
        else if (priceTitle.equals(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto5million)))
            price=5000000;
        else if (priceTitle.equals(Configuration.getConfig().MainActivityContext.getResources().getString(R.string.upto10million)))
            price=getAtLeastHighestPrice();
        else
            price=10000001; //1 is sign for price is above
        return price;
    }
    public int getAtLeastHighestPrice(){
        return 10000000;
    }
    public int getStartArticleNumber(){return 0;}
    public int getAtLeastArticleInFirstTime(){return 25;}
    public int getArticleNumberWhenRefresh(){return 100;}
    public String getImageQuality(){
        String quality="30";
        return quality;
    }
    public int calculatePriceOffProduct(int price,int priceOff){
        int off=(price*priceOff)/100;
        int priceForYou=price-off;
        return priceForYou;
    }

}
