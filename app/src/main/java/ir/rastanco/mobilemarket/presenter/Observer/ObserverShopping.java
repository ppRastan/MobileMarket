package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1394/11/12.
 */
public class ObserverShopping {

    //Click on Button Shopping Bag
    private static boolean shoppingStatus;
    private final static List<ObserverShoppingBagClickListener> listeners = new ArrayList<>();
    public static boolean getMyBoolean() { return shoppingStatus; }
    public static void setMyBoolean(boolean value) {
        shoppingStatus = value;
        for (ObserverShoppingBagClickListener l : listeners) {
            l.OnMyBooleanChanged();
        }
    }
    public static  void addMyBooleanListener(ObserverShoppingBagClickListener l) {
        listeners.add(l);
    }


    //Shopping Ok
    private static boolean shoppingOk;
    private final static List<ObserverShoppingOkListener> shoppingOkListener = new ArrayList<>();
    public static boolean getShoppingOk() {
        return shoppingOk;
    }
    public static void setShoppingOk(boolean shoppingOk) {
        ObserverShopping.shoppingOk = shoppingOk;
        for (ObserverShoppingOkListener ok : shoppingOkListener) {
            ok.ShoppingOKChanged();
        }
    }
    public static void addShoppingOkListener(ObserverShoppingOkListener ok){
        shoppingOkListener.add(ok);

    }





}
