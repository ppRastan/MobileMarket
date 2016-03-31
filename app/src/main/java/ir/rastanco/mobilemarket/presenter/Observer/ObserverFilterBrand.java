package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1394/11/28 */

public class ObserverFilterBrand {

    private static Boolean addFilterBrand;
    private static List<ObserverFilterBrandListener> ChangeSetFilterBrandListener=new ArrayList<>();

    public static Boolean getAddFilterBrand() {
        return addFilterBrand;
    }
    public static void setAddFilterBrand(Boolean addFilterBrand) {
        ObserverFilterBrand.addFilterBrand = addFilterBrand;
        for (ObserverFilterBrandListener setFilter : ChangeSetFilterBrandListener) {
            setFilter.changeFilterBrand();
        }
    }

    public static void changeFilterBrandListener(ObserverFilterBrandListener change){
        ChangeSetFilterBrandListener.add(change);
    }
}
