package ir.rastanco.mobilemarket.presenter;

/**
 * Created by Samaneh on 12/20/2015.
 */
/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.homePresenter.PictureProductHomeItemAdapter;
import ir.rastanco.mobilemarket.presenter.photoPresenter.PictureProductPhotoItemAdapter;
import ir.rastanco.mobilemarket.presenter.shopPresenter.PictureProductShopItemAdapter;

public class SuperAwesomeCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;

    private ServerConnectionHandler sch;
    private ArrayList<Product> products;




    public static SuperAwesomeCardFragment newInstance(int position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        sch=new ServerConnectionHandler();
        products=new ArrayList<Product>();
        products=sch.getAllProductInfoACategory("http://decoriss.com/json/get,com=product&catid=44&cache=true");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView=null;

        switch (position) {
            case 0: {

                mainView = inflater.inflate(R.layout.fragment_home, null);
                ListView productListView = (ListView) mainView.findViewById(R.id.lstv_picProduct);
                PictureProductHomeItemAdapter adapter = new PictureProductHomeItemAdapter(getActivity(), R.layout.picture_product_item_home,products);
                productListView.setAdapter(adapter);
                break;
            }
            case 1: {
                mainView = inflater.inflate(R.layout.fragment_photo, null);
                GridView gridview = (GridView) mainView.findViewById(R.id.gv_photoProduct);
                PictureProductPhotoItemAdapter adapter= new PictureProductPhotoItemAdapter(getActivity());
                adapter.setData(products);
                gridview.setAdapter(adapter);
                break;
            }
            case 2:{

                mainView=inflater.inflate(R.layout.fragment_shop,null);
                GridView gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);
                gridview.setAdapter(new PictureProductShopItemAdapter(getActivity(),products));
                break;
            }
        }
        return mainView;
    }
}