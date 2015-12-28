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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Product;

public class SuperAwesomeCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";


    TextView textView;

    private int position;

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

        switch (position) {
            case 0: {

                mainView = inflater.inflate(R.layout.fragment_home, null);
                ArrayList<Product> allProduct = new ArrayList<>();
                Product aProduct1 = new Product();
                aProduct1.setName("product1");
                aProduct1.setPic(R.drawable.product_1);
                allProduct.add(aProduct1);

                Product aProduct2 = new Product();
                aProduct2.setName("product2");
                aProduct2.setPic(R.drawable.product_2);
                allProduct.add(aProduct2);

                Product aProduct3 = new Product();
                aProduct3.setName("product3");
                aProduct3.setPic(R.drawable.product_3);
                allProduct.add(aProduct3);

                ListView productListView = (ListView) mainView.findViewById(R.id.lstv_picProduct);
                PictureProductItemAdapter adapter = new PictureProductItemAdapter(getActivity(), R.layout.picture_product_item, allProduct);
                productListView.setAdapter(adapter);

                break;
            }
            case 1: {
                mainView = inflater.inflate(R.layout.fragment_photo, null);
                break;
            }
        }
        return mainView;
    }
}