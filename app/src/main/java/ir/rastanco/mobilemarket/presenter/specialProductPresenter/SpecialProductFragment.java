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
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;

/**
 * Created by ShaisteS on 1394/12/09.
 * This Fragment show specialProduct(specialProduct is a product that discount or showAtHomeProduct=1)
 */
public class SpecialProductFragment extends Fragment {

    private ServerConnectionHandler sch;
    private ArrayList<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sch=new ServerConnectionHandler(getContext());
        products=new ArrayList<>();
        products=sch.getSpecialProduct();
        View mainView = inflater.inflate(R.layout.fragment_home,container,false);
        final ListView productListView = (ListView) mainView.findViewById(R.id.listView_picProduct);
        PictureSpecialProductItemAdapter adapter = new PictureSpecialProductItemAdapter(getActivity(),products);
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
                if (productListView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = productListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = productListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
                if (enable)
                    Configuration.getConfig().telephoneFloatingActionButton.setVisibility(View.VISIBLE);
                else
                    Configuration.getConfig().telephoneFloatingActionButton.setVisibility(View.GONE);



            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sch.refreshCategories(Link.getInstance().generateURLForGetAllCategories());
                        sch.getNewProducts();
                        sch.getEditProducts();
                        products = sch.getSpecialProduct();
                        PictureSpecialProductItemAdapter newAdapter = new PictureSpecialProductItemAdapter(getActivity(),products);
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
