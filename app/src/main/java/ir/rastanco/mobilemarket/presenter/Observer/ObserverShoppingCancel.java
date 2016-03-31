package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1394/11/16.
 */
public class ObserverShoppingCancel {
    //Shopping cancel
    private static boolean shoppingCancel;
    private static List<ObserverShoppingCancelListener> shoppingCancelListener = new ArrayList<>();
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
