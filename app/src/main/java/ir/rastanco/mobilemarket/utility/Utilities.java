package ir.rastanco.mobilemarket.utility;

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

    public int convertPriceTitleToInt(String priceTitle){
        int price=0;
        if(priceTitle.equals("تا سقف 1 میلیون تومان"))
            price=1000000;
        else if (priceTitle.equals("تا سقف 5 میلیون تومان"))
            price=5000000;
        else if (priceTitle.equals("تا سقف 10 میلیون تومان"))
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
        return quality;}

}
