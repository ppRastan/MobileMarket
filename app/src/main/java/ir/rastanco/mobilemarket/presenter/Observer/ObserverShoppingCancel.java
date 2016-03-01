package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaisteS on 02/05/2016.
 */
public class ObserverShoppingCancel {
    //Shopping cancel
    private static boolean shoppingCancel;
    private static List<ObserverShoppingCancelListener> shoppingCancelListener = new ArrayList<ObserverShoppingCancelListener>();
    public static boolean getShoppingCancel() {
        return shoppingCancel;
    }
    public static void setShoppingCancel(boolean shoppingCancel) {
        ObserverShoppingCancel.shoppingCancel = shoppingCancel;
        for (ObserverShoppingCancelListener ok : shoppingCancelListener) {
            ok.ShoppingChanged();
        }
    }
    public static void addShoppingCancelListener(ObserverShoppingCancelListener cancel){
        shoppingCancelListener.add(cancel);

    }

}
