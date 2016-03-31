package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1394/11/28.
 */
public class ObserverFilterPrice {

    private static Boolean addFilterPrice;
    private static List<ObserverFilterPriceListener> ChangeSetFilterPriceListener=new ArrayList<>();


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
