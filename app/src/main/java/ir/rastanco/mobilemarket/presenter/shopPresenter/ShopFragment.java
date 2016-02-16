package ir.rastanco.mobilemarket.presenter.shopPresenter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.Article;
import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Filter.FilterCategory;

/**
 * Created by shaisteS on 02/16/2016.
 */
public class ShopFragment extends Fragment {

    private ServerConnectionHandler sch;
    private ArrayList<Product> products;
    private ArrayList<Article> articles;
    private ArrayList<String> mainCategoryTitle;
    private String second_page;
    private String third_page;
    private String fourth_page;
    private Button btnCategory;
    private Button btnSubGroup;
    private Dialog dialogGroup;
    private Dialog dialogSubGroup;
    private ImageButton btnResetAlertDialog;
    private ImageButton btnCancelAlertDialog;
    private ImageButton btnResetSubGroup;
    private ImageButton btnCancleSubGroup;
    private TextView subGroupTextView;
    private TextView groupTextView;

    private FragmentActivity myContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View shopView = inflater.inflate(R.layout.fragment_shop, container, false);

        final String pageName;
        pageName="مبلمان منزل";
        /*if(position==1)
            pageName=second_page;
        else if (position==2)
            pageName=third_page;
        else
            pageName=fourth_page;*/

        products=sch.ProductOfMainCategory(pageName);
        shopView=inflater.inflate(R.layout.fragment_shop, null);
        final GridView gridview = (GridView) shopView.findViewById(R.id.gv_infoProduct);
        final PictureProductShopItemAdapter adapter=new  PictureProductShopItemAdapter(getActivity(),products);
        gridview.setAdapter(adapter);
        final SwipeRefreshLayout mSwipeRefreshLayout= (SwipeRefreshLayout)
                shopView.findViewById(R.id.swipe_refresh_layout);
        //refresh grid view
        mSwipeRefreshLayout.setEnabled(false);
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean enable = false;
                if (gridview != null && gridview.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = gridview.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = gridview.getChildAt(0).getTop() == 0;
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
                        products = sch.ProductOfMainCategory(pageName);
                        PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), products);
                        gridview.setAdapter(newAdapter);
                        newAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        groupTextView = (TextView)shopView.findViewById(R.id.group_dialog_text);
        /*ObserverSimilarProduct.SimilarProductListener(new SimilarProductListener() {
            @Override
            public void SimilarProductSet() {
                groupTextView.setText(sch.getACategoryTitle(ObserverSimilarProduct.getSimilarProduct()));
                ArrayList<Product> newProducts = sch.ProductOFASubCategory(ObserverSimilarProduct.getSimilarProduct());
                PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                gridview.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();

            }
        });

        ObserverLike.changeLikeStatusListener(new ChangeLikeListener() {
            @Override
            public void changeLikeStatus() {
                gridview.setSelection(ObserverLike.getLikeStatus());
                gridview.setAdapter(adapter);

            }
        });*/

        //Filter
        ///FilterSubCategory
        final String[]categorySelected = new String[1];
        final int[] categoryIdSelected = new int[1];
        ///default=all
        categorySelected[0]=getActivity().getString(R.string.all);
        //get category id
        categoryIdSelected[0]=sch.getMainCategoryId(pageName);
        ArrayList<String> subCategoryTitle = new ArrayList<String>();
        //get subCategoryTitle
        subCategoryTitle=sch.getTitleOfChildOfACategory(categoryIdSelected[0]);
        //SubCategory dialogBox
        btnCategory=(Button)shopView.findViewById(R.id.group_dialog);
        final ArrayList<String> finalSubCategoryTitle = subCategoryTitle;
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("name", pageName);
                FilterCategory filterCategory = new FilterCategory();
                filterCategory.setArguments(args);


                //filterCategory.setTargetFragment(, 0);
                filterCategory.show(myContext.getFragmentManager(), "Category");
                String subCategorySelected = getArguments().getString("subCategorySelected");
                Toast.makeText(getActivity(), subCategorySelected, Toast.LENGTH_LONG).show();


                        dialogGroup = new Dialog(getActivity());
                        dialogGroup.setContentView(R.layout.title_alertdialog_for_group);
                        btnCancelAlertDialog = (ImageButton) dialogGroup.findViewById(R.id.cancel);
                        btnResetAlertDialog = (ImageButton)dialogGroup.findViewById(R.id.reset_action);
                        groupTextView.setTextColor(Color.parseColor("#EB4D2A"));
                        btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogGroup.dismiss();
                            }
                        });
                        btnResetAlertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                categorySelected[0]=getActivity().getString(R.string.all);
                                groupTextView.setText(categorySelected[0]);
                                dialogGroup.dismiss();
                            }
                        });
                        TextView text = (TextView) dialogGroup.findViewById(R.id.title_alertdialog_group);
                        final ListView listCategory = (ListView) dialogGroup.findViewById(R.id.list);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, finalSubCategoryTitle);
                        listCategory.setAdapter(adapter);
                        //childOfASubCategory
                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                categorySelected[0] = (String) parent.getItemAtPosition(position);
                                final int[] subCategoryId = {0};
                                Map<Integer, String> filterSubCategory = sch.getFilterSubCategory(pageName);
                                for (Map.Entry<Integer, String> entry : filterSubCategory.entrySet()) {
                                    if (entry.getValue().equals(categorySelected[0]))
                                        subCategoryId[0] = entry.getKey();
                                }

                                ArrayList<String> childOfASubcategory = new ArrayList<String>();
                                childOfASubcategory = sch.getTitleOfChildOfACategory(subCategoryId[0]);
                                //dialogBox for subCategory
                                final Dialog dialogSGroup = new Dialog(getActivity());
                                dialogSGroup.setContentView(R.layout.title_alertdialog_for_group);
                                btnCancelAlertDialog = (ImageButton) dialogSGroup.findViewById(R.id.cancel);
                                btnResetAlertDialog = (ImageButton) dialogSGroup.findViewById(R.id.reset_action);
                                groupTextView.setTextColor(Color.parseColor("#EB4D2A"));
                                btnCancelAlertDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogSGroup.dismiss();
                                    }
                                });
                                btnResetAlertDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        categorySelected[0] = getActivity().getString(R.string.all);
                                        groupTextView.setText(categorySelected[0]);
                                        dialogSGroup.dismiss();

                                    }
                                });
                                TextView text = (TextView) dialogSGroup.findViewById(R.id.title_alertdialog_group);
                                final ListView listSubCategory = (ListView) dialogSGroup.findViewById(R.id.list);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1, android.R.id.text1, childOfASubcategory);
                                listSubCategory.setAdapter(adapter);
                                final int[] subCategoryIdSelected = new int[1];
                                listSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String subCategorySelected = (String) parent.getItemAtPosition(position);
                                        groupTextView.setText(subCategorySelected);
                                        subCategoryIdSelected[0] = sch.getCategoryIdWithTitle(subCategorySelected);
                                        dialogGroup.dismiss();
                                        dialogSGroup.dismiss();
                                        ArrayList<Product> newProducts = sch.getproductOfACategory(subCategoryIdSelected[0]);
                                        PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), newProducts);
                                        gridview.setAdapter(newAdapter);
                                        newAdapter.notifyDataSetChanged();
                                    }
                                });
                                dialogSGroup.show();
                            }
                        });
                        dialogGroup.show();
                        dialogGroup.setCancelable(true);
                    }
                });
                ///Filter in Product Features
                final String[] subCategorySelected = new String[1];
                subCategorySelected[0]=getActivity().getString(R.string.all);
                subGroupTextView = (TextView)shopView.findViewById(R.id.acordingto_dialog_text);
                btnSubGroup=(Button)shopView.findViewById(R.id.acording_to_dialog);
                btnSubGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSubGroup = new Dialog(getActivity());
                        dialogSubGroup.setContentView(R.layout.title_alertdialog_for_sub_group);
                        btnResetSubGroup = (ImageButton)dialogSubGroup.findViewById(R.id.reset_action_subgroup);
                        btnCancleSubGroup = (ImageButton)dialogSubGroup.findViewById(R.id.cancel_action_subgroup);
                        subGroupTextView.setTextColor(Color.parseColor("#EB4D2A"));
                        btnCancleSubGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogSubGroup.dismiss();
                            }
                        });
                        btnResetSubGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                subCategorySelected[0] = getActivity().getString(R.string.all);
                                dialogSubGroup.dismiss();
                                subGroupTextView.setText(categorySelected[0]);
                            }
                        });
                        TextView text = (TextView) dialogSubGroup.findViewById(R.id.title_alertdialog_group);
                        ArrayList<String> subCategoryTitle=new ArrayList<String>();
                        subCategoryTitle.add("قیمت");
                        subCategoryTitle.add("برند");

                        final ListView listCategory = (ListView) dialogSubGroup.findViewById(R.id.list);
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                        listCategory.setAdapter(adapter);

                        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Filter Price
                                if(position==0)
                                {
                                    dialogSubGroup.dismiss();
                                    final Dialog dialogPrice = new Dialog(getActivity());
                                    dialogPrice.setContentView(R.layout.title_alertdialog_for_sub_group);
                                    ImageButton btnResetPrice = (ImageButton)dialogPrice.findViewById(R.id.reset_action_subgroup);
                                    ImageButton btnCanclePrice = (ImageButton)dialogPrice.findViewById(R.id.cancel_action_subgroup);
                                    btnCanclePrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogPrice.dismiss();
                                        }
                                    });
                                    btnResetPrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            subCategorySelected[0] = getActivity().getString(R.string.all);
                                            dialogPrice.dismiss();
                                            subGroupTextView.setText(categorySelected[0]);
                                        }
                                    });
                                    TextView text = (TextView) dialogPrice.findViewById(R.id.title_alertdialog_group);
                                    ArrayList<String> subCategoryTitle=new ArrayList<String>();
                                    subCategoryTitle.add(" تا سقف 1 میلیون تومان");
                                    subCategoryTitle.add("تا سقف 5 میلیون تومان");
                                    subCategoryTitle.add("تا سقف 10 میلیون تومان");
                                    subCategoryTitle.add("بالاتر از 10 میلیون تومان");

                                    final ListView listPrice = (ListView) dialogPrice.findViewById(R.id.list);
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                            android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                                    listPrice.setAdapter(adapter);
                                    listPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            final ArrayList<Product> productPrice = new ArrayList<Product>();
                                            if (position == 0) {
                                                for (int i = 0; i < products.size(); i++) {
                                                    if (products.get(i).getPrice() <= 1000000)
                                                        productPrice.add(products.get(i));
                                                }
                                                subGroupTextView.setText(listPrice.getItemAtPosition(position).toString());

                                            }
                                            if (position == 1) {
                                                for (int i = 0; i < products.size(); i++) {
                                                    if (products.get(i).getPrice() <= 5000000 && products.get(i).getPrice()>1000000)
                                                        productPrice.add(products.get(i));
                                                }
                                                subGroupTextView.setText(listPrice.getItemAtPosition(position).toString());

                                            }

                                            if (position == 2) {
                                                for (int i = 0; i < products.size(); i++) {
                                                    if (products.get(i).getPrice() <= 10000000 && products.get(i).getPrice()>5000000)
                                                        productPrice.add(products.get(i));
                                                }
                                                subGroupTextView.setText(listPrice.getItemAtPosition(position).toString());

                                            }
                                            if (position == 3) {
                                                for (int i = 0; i < products.size(); i++) {
                                                    if (products.get(i).getPrice() > 10000000)
                                                        productPrice.add(products.get(i));
                                                }
                                                subGroupTextView.setText(listPrice.getItemAtPosition(position).toString());

                                            }
                                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(), productPrice);
                                            gridview.setAdapter(newAdapter);
                                            newAdapter.notifyDataSetChanged();
                                            dialogPrice.dismiss();
                                        }
                                    });
                                    dialogPrice.show();
                                }
                                //Filter Brand
                                if (position==1){
                                    dialogSubGroup.dismiss();
                                    final Dialog dialogBrand = new Dialog(getActivity());
                                    dialogBrand.setContentView(R.layout.title_alertdialog_for_sub_group);
                                    ImageButton btnResetPrice = (ImageButton)dialogBrand.findViewById(R.id.reset_action_subgroup);
                                    ImageButton btnCanclePrice = (ImageButton)dialogBrand.findViewById(R.id.cancel_action_subgroup);
                                    btnCanclePrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBrand.dismiss();
                                        }
                                    });
                                    btnResetPrice.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            subCategorySelected[0] = getActivity().getString(R.string.all);
                                            dialogBrand.dismiss();
                                            subGroupTextView.setText(categorySelected[0]);
                                        }
                                    });
                                    TextView text = (TextView) dialogBrand.findViewById(R.id.title_alertdialog_group);
                                    ArrayList<String> subCategoryTitle=new ArrayList<String>();
                                    subCategoryTitle=sch.getAllBrands(products);
                                    final ListView listBrand = (ListView) dialogBrand.findViewById(R.id.list);
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                            android.R.layout.simple_list_item_1, android.R.id.text1, subCategoryTitle);
                                    listBrand.setAdapter(adapter);
                                    listBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String brandTile=(String)parent.getItemAtPosition(position);
                                            subGroupTextView.setText(brandTile);
                                            PictureProductShopItemAdapter newAdapter = new PictureProductShopItemAdapter(getActivity(),
                                                    sch.getAllProductOfABrand(products,brandTile));
                                            gridview.setAdapter(newAdapter);
                                            newAdapter.notifyDataSetChanged();
                                            dialogBrand.dismiss();
                                        }
                                    });
                                    dialogBrand.show();
                                }
                            }
                        });
                        dialogSubGroup.setCancelable(true);
                        dialogSubGroup.show();
                    }
                });

        return shopView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                //get subCategory Selected from FilterSubcategory Dialog
                Bundle bundle = data.getExtras();
                String subCategorySelected = bundle.getString("subCategorySelected");
                Log.d("Tag 2", subCategorySelected);
                break;
        }
    }
}
