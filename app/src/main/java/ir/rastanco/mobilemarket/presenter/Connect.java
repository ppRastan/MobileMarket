package ir.rastanco.mobilemarket.presenter;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by shaisteS on 1394/11/12.
 */
public class Connect {

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
}
