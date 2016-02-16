package ir.rastanco.mobilemarket.presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emertat on 02/11/2016.
 */
public class ObserverSimilarProduct {

    private static int similarProduct;
    private static List<ObserverSimilarProductListener> similarProductListener = new ArrayList<ObserverSimilarProductListener>();
    public static int getSimilarProduct() {
        return similarProduct;
    }
    public static void setSimilarProduct(int shoppingCancel) {
        ObserverSimilarProduct.similarProduct = shoppingCancel;
        for (ObserverSimilarProductListener ok : similarProductListener) {
            ok.SimilarProductSet();
        }
    }
    public static void SimilarProductListener(ObserverSimilarProductListener set){
        similarProductListener.add(set);

    }
}