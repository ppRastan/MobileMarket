package ir.rastanco.mobilemarket.presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 02/16/2016.
 */
public class ObserverFilterCategory {

    private static Boolean addFilter;
    private static List<ObserverFilterCategoryListener> ChangeSetFilterListener=new ArrayList<ObserverFilterCategoryListener>();


    public static Boolean getAddFilter() {
        return addFilter;
    }
    public static void setAddFilter(Boolean addFilter) {
        ObserverFilterCategory.addFilter = addFilter;
        for (ObserverFilterCategoryListener setFilter : ChangeSetFilterListener) {
            setFilter.changeFilterCategory();
        }
    }

    public static void changeFilterCategoryListener(ObserverFilterCategoryListener change){
        ChangeSetFilterListener.add(change);
    }
}
