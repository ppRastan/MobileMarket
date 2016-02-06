package ir.rastanco.mobilemarket.presenter.shoppingBagPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaisteS on 02/05/2016.
 */
public class Observer {
    //Shopping cancel
    private static boolean shoppingCancel;
    private static List<ShoppingCancelListener> shoppingCancelListener = new ArrayList<ShoppingCancelListener>();
    public static boolean getShoppingCancel() {
        return shoppingCancel;
    }
    public static void setShoppingCancel(boolean shoppingCancel) {
        Observer.shoppingCancel = shoppingCancel;
        for (ShoppingCancelListener ok : shoppingCancelListener) {
            ok.ShoppingChanged();
        }
    }
    public static void addShoppingCancelListener(ShoppingCancelListener cancel){
        shoppingCancelListener.add(cancel);

    }

}
