package ir.rastanco.mobilemarket.presenter.specialProductPresenter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;

/**
 * Created by ShaisteS on 1394/12/09.
 */
public class SpecialProductFragment extends Fragment {

    private ServerConnectionHandler sch;
    private ArrayList<Product> products;
    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch=new ServerConnectionHandler(getContext());
        products=new ArrayList<Product>();
        products=sch.getSpecialProduct();
        mainView = inflater.inflate(R.layout.fragment_home, null);
        final ListView productListView = (ListView) mainView.findViewById(R.id.lstv_picProduct);
        PictureSpecialProductItemAdapter adapter = new PictureSpecialProductItemAdapter(getActivity(), R.layout.picture_product_item_home,products);
        productListView.setAdapter(adapter);
        final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                mainView.findViewById(R.id.swipe_refresh_layout);
        //refresh grid view
        mSwipeRefreshLayout.setEnabled(false);
        productListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean enable = false;
                if (productListView != null && productListView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = productListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = productListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);

            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sch.refreshProduct();
                        products = sch.getSpecialProduct();
                        PictureSpecialProductItemAdapter newAdapter = new PictureSpecialProductItemAdapter(getActivity(), R.layout.picture_product_item_home,products);
                        productListView.setAdapter(newAdapter);
                        newAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });

        return mainView;


    }
}
