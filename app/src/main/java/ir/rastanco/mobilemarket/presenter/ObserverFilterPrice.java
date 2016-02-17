package ir.rastanco.mobilemarket.presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaisteS on 02/17/2016.
 */
public class ObserverFilterPrice {

    private static Boolean addFilterPrice;
    private static List<ObserverFilterPriceListener> ChangeSetFilterPriceListener=new ArrayList<ObserverFilterPriceListener>();


    public static Boolean getAddFilterPrice() {
        return addFilterPrice;
    }
    public static void setAddFilterPrice(Boolean addFilterPricer) {
        ObserverFilterPrice.addFilterPrice = addFilterPrice;
        for (ObserverFilterPriceListener setFilter : ChangeSetFilterPriceListener) {
            setFilter.changeFilterPrice();
        }
    }

    public static void changeFilterPriceListener(ObserverFilterPriceListener change){
        ChangeSetFilterPriceListener.add(change);
    }
}
