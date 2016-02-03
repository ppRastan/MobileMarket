package ir.rastanco.mobilemarket.presenter;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by shaisteS on 1394/11/12.
 */
public class Connect {

    //Click on Button Shopping Bag
    private static boolean shoppingStatus;
    private static List<ConnectionBooleanChangeListener> listeners = new ArrayList<ConnectionBooleanChangeListener>();
    public static boolean getMyBoolean() { return shoppingStatus; }
    public static void setMyBoolean(boolean value) {
        shoppingStatus = value;
        for (ConnectionBooleanChangeListener l : listeners) {
            l.OnMyBooleanChanged();
        }
    }
    public static  void addMyBooleanListener(ConnectionBooleanChangeListener l) {
        listeners.add(l);
    }


    //Shopping Ok
    private static boolean shoppingOk;
    private static List<ShoppingOkListener> shoppingOkListener = new ArrayList<ShoppingOkListener>();
    public static boolean getShoppingOk() {
        return shoppingOk;
    }
    public static void setShoppingOk(boolean shoppingOk) {
        Connect.shoppingOk = shoppingOk;
        for (ShoppingOkListener ok : shoppingOkListener) {
            ok.ShoppingOKChanged();
        }
    }
    public static void addShoppingOkListener(ShoppingOkListener ok){
        shoppingOkListener.add(ok);

    }





}
