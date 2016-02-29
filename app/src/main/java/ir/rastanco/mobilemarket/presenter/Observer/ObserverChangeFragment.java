package ir.rastanco.mobilemarket.presenter.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaisteS on 1394/12/10.
 */
public class ObserverChangeFragment {

    private static Boolean changeFragmentParameter;
    private static List<ObserverChangeFragmentListener> ChangeSetFilterAllListener=new ArrayList<ObserverChangeFragmentListener>();

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
