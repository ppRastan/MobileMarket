package ir.rastanco.mobilemarket.presenter.UserProfilePresenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.UserInfo;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by Emertat on 02/11/2016.
 */
public class UserProfile extends Fragment {

    private ServerConnectionHandler sch;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View userProfileView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        Configuration.UserProfileContext =getContext();
        sch=new ServerConnectionHandler(Configuration.UserProfileContext);
        UserInfo aUser=sch.getUserInfo();
        ArrayList<Product> allProductLike=new ArrayList<Product>();
        allProductLike=sch.getAllProductFavourite();

        TextView txtEmail=(TextView) userProfileView.findViewById(R.id.txt_user_email);
        txtEmail.setText(aUser.getUserEmail());

        ListView lsvFavourite=(ListView) userProfileView.findViewById(R.id.lsv_favouriteProduct);
        UserProfileAdapter adapter= new UserProfileAdapter(Configuration.UserProfileContext,R.layout.user_profile_like_product_item,allProductLike);
        lsvFavourite.setAdapter(adapter);

        ImageButton logOut=(ImageButton)userProfileView.findViewById(R.id.img_logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sch.deleteUserInfo();

                UserLogin login=new UserLogin();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.replaceFragment,login );
                transaction.commit();
            }
        });

        return userProfileView;

    }
}
