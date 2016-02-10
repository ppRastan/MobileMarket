package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.ProductShop;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by Emertat on 01/15/2016.
 */
public class UserAccount extends Fragment {

    private ServerConnectionHandler sch;
    private ArrayList<ProductShop> allProductsShop;
    private UserInfo user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View userAccountView=inflater.inflate(R.layout.fragment_user_account, container, false);
        Configuration.UserAccountFragment=getContext();
        sch=new ServerConnectionHandler(Configuration.UserAccountFragment);
        allProductsShop=new ArrayList<ProductShop>();
        user=new UserInfo();
        user=sch.getUserInfo();
        if (user != null){
            allProductsShop=sch.getLastProductShop("http://decoriss.com/json/get,com=orders&uid="+
                    user.getUserId()+"&cache=false");
        }
        //allProductsShop=sch.getLastProductShop("http://decoriss.com/json/get,com=orders&uid=4973&cache=false");
        ListView lvLastShopping=(ListView)userAccountView.findViewById(R.id.lv_lastShopping);
        LastShoppingItemAdapter adapter=new LastShoppingItemAdapter(getActivity(),R.layout.last_shopping_item,allProductsShop);
        lvLastShopping.setAdapter(adapter);

        return userAccountView;


    }
}
