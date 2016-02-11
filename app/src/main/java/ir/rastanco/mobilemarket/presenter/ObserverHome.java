package ir.rastanco.mobilemarket.presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emertat on 02/11/2016.
 */
public class ObserverHome {

    private static int similarProduct;
    private static List<SimilarProductListener> similarProductListener = new ArrayList<SimilarProductListener>();
    public static int getSimilarProduct() {
        return similarProduct;
    }
    public static void setSimilarProduct(int shoppingCancel) {
        ObserverHome.similarProduct = shoppingCancel;
        for (SimilarProductListener ok : similarProductListener) {
            ok.SimilarProductSet();
        }
    }
    public static void SimilarProductListener(SimilarProductListener set){
        similarProductListener.add(set);

    }
}