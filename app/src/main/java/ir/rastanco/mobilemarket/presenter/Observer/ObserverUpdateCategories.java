package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1395/1/30.
 */
public class ObserverUpdateCategories {

    private static boolean updateCategoriesStatus;
    private final static List<ObserverUpdateCategoriesListener> updateCategoriesListener = new ArrayList<>();
    public static boolean getUpdateCategoriesStatus() {
        return updateCategoriesStatus;
    }
    public static void setUpdateCategoriesStatus(boolean updateCategoriesStatus) {
        ObserverUpdateCategories.updateCategoriesStatus = updateCategoriesStatus;
        for (ObserverUpdateCategoriesListener ok : updateCategoriesListener) {
            ok.updateCategories();
        }
    }
    public static void updateCategoriesListener(ObserverUpdateCategoriesListener set){
        updateCategoriesListener.add(set);

    }
}

