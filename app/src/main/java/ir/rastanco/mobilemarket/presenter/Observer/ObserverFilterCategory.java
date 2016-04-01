package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1394/11/27.
 */
public class ObserverFilterCategory {

    private static Boolean addFilter;
    private final static List<ObserverFilterCategoryListener> ChangeSetFilterListener=new ArrayList<>();


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
