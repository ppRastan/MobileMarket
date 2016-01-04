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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverHandler.DownloadImage;
import ir.rastanco.mobilemarket.presenter.homePresenter.PictureProductHomeItemAdapter;
import ir.rastanco.mobilemarket.presenter.photoPresenter.PictureProductPhotoItemAdapter;
import ir.rastanco.mobilemarket.presenter.shopPresenter.PictureProductShopItemAdapter;
import ir.rastanco.mobilemarket.utility.Configuration;

public class SuperAwesomeCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    private Bitmap imageProduct;



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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View mainView=null;

        try {
            imageProduct= new DownloadImage().execute("http://decoriss.com/roots/wm.php?code=uploads/data/15/1513-2.jpg&size="+
                    Configuration.widthDisplay+"x"+Configuration.widthDisplay+"&q=30").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<Product> allProduct = new ArrayList<>();
        Product aProduct1 = new Product();
        //aProduct1.setName("product1");
        aProduct1.setAllImage(imageProduct);
        allProduct.add(aProduct1);

        try {
            imageProduct= new DownloadImage().execute("http://decoriss.com/roots/wm.php?code=uploads/data/15/1513-2.jpg&size="+
                    Configuration.widthDisplay+"x"+Configuration.widthDisplay+"&q=30").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Product aProduct2 = new Product();
        //aProduct2.setName("product2");
        aProduct2.setAllImage(imageProduct);
        allProduct.add(aProduct2);

        try {
            imageProduct= new DownloadImage().execute("http://decoriss.com/roots/wm.php?code=uploads/data/15/1513-2.jpg&size="+
                    Configuration.widthDisplay+"x"+Configuration.widthDisplay+"&q=30").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Product aProduct3 = new Product();
        //aProduct3.setName("product3");
        aProduct3.setAllImage(imageProduct);
        allProduct.add(aProduct3);

        switch (position) {
            case 0: {

                mainView = inflater.inflate(R.layout.fragment_home, null);
                ListView productListView = (ListView) mainView.findViewById(R.id.lstv_picProduct);
                PictureProductHomeItemAdapter adapter = new PictureProductHomeItemAdapter(getActivity(), R.layout.picture_product_item_home, allProduct);
                productListView.setAdapter(adapter);
                break;
            }
            case 1: {
                mainView = inflater.inflate(R.layout.fragment_photo, null);
                GridView gridview = (GridView) mainView.findViewById(R.id.gv_photoProduct);
                gridview.setAdapter(new PictureProductPhotoItemAdapter(getActivity()));
                break;
            }
            case 2:{

                 String[] info = {
                        "Product1","Product2","Product3","Product4"
                };

                Integer[] pric  = {
                        1200,1300,5000,2000
                };

                Integer[] mThumbIds = {
                        R.drawable.product_1, R.drawable.product_2,
                        R.drawable.product_3, R.drawable.product_1
                };

                mainView=inflater.inflate(R.layout.fragment_shop,null);
                GridView gridview = (GridView) mainView.findViewById(R.id.gv_infoProduct);
                gridview.setAdapter(new PictureProductShopItemAdapter(getActivity(),info,pric,mThumbIds));
                break;
            }
        }
        return mainView;
    }
}