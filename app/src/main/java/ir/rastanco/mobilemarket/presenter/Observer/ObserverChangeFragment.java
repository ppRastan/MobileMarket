package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaisteS on 1394/12/10.
 * This class for listen "changeFragmentParameter".
 * after change this parameter than changed fragments in Tabs
 * This  class is utilized here : Special Loading Fragment,First Tab Fragment Manager
 *
 */
public class ObserverChangeFragment {

    private static Boolean changeFragmentParameter;
    private static List<ObserverChangeFragmentListener> ChangeSetFilterAllListener=new ArrayList<>();

    public static Boolean getChangeFragmentParameter() {
        return changeFragmentParameter;
    }
    public static void setChangeFragmentParameter( Boolean changeFragmentParamet) {
        ObserverChangeFragment.changeFragmentParameter = changeFragmentParamet;
        for (ObserverChangeFragmentListener setFilter : ChangeSetFilterAllListener) {
            setFilter.changeFragment();
        }
    }

    public static void ObserverChangeFragmentListener(ObserverChangeFragmentListener change){
        ChangeSetFilterAllListener.add(change);
    }
}
