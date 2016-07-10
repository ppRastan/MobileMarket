package ir.rastanco.mobilemarket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import ir.rastanco.mobilemarket.dataModel.Product;
import ir.rastanco.mobilemarket.dataModel.ProductOption;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Services.DownloadProductOption;
import ir.rastanco.mobilemarket.presenter.Services.DownloadResultReceiver;
import ir.rastanco.mobilemarket.utility.Configuration;
import ir.rastanco.mobilemarket.utility.Link;

public class InfoActivity extends AppCompatActivity  implements DownloadResultReceiver.Receiver{

    FloatingActionMenu materialDesignFAM;
    private boolean onBackBtnPressed = false;
    private TextView informationCartView;
    private ProgressBar progressBarProductOption;
    private Context context;
    private ArrayList<ProductOption> options;
    private ServerConnectionHandler sch;
    private int productId;
    private int groupId;
    private DownloadResultReceiver mReceiver;

    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
//        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(Link.getInstance().telephoneNumber()));
                startActivity(callIntent);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO handle telegram
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/karafarini10"));
                startActivity(browserIntent);
            }
        });
//        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                //TODO something when floating action menu third item clicked
//
//            }
//        });

        Configuration.getConfig().productOptionActivityContext = this;
        context=Configuration.getConfig().productOptionActivityContext;
        sch = new ServerConnectionHandler(context);
        informationCartView = (TextView) findViewById(R.id.information_cartView);
        progressBarProductOption=(ProgressBar)findViewById(R.id.progressBar_loading_information);

        Intent intent = this.getIntent();
        productId = intent.getIntExtra("productId", 0);
        groupId=intent.getIntExtra("groupId", 0);

        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        if (!sch.existOptionsForAProduct(productId)&& Configuration.getConfig().connectionStatus) {
            informationCartView.setText("در حال دانلود ویژگی های محصول . . .");
            progressBarProductOption.setVisibility(View.VISIBLE);
            Intent productOptionService = new Intent(Intent.ACTION_SYNC, null,context, DownloadProductOption.class);
            productOptionService.putExtra("productId", productId);
            productOptionService.putExtra("groupId", groupId);
            productOptionService.putExtra("receiver", mReceiver);
            productOptionService.putExtra("requestId", 103);
            startService(productOptionService);
        }
        else if(!sch.existOptionsForAProduct(productId)&& !Configuration.getConfig().connectionStatus){
            progressBarProductOption.setVisibility(View.GONE);
            informationCartView.setText(context.getResources().getString(R.string.features) + "\n\n" +
                    "این محصول فعلا فاقد ویژگی می باشد");

        }
        else if (sch.existOptionsForAProduct(productId)){
            progressBarProductOption.setVisibility(View.GONE);
            options = sch.getProductOptionFromDataBase(productId);
            informationCartView.setText(convertArrayListToString(options));

        }

        Product aProduct = sch.getAProduct(productId);
        TextView nameOfCurrentProduct = (TextView) findViewById(R.id.eachProductName);
        nameOfCurrentProduct.setText(aProduct.getTitle());
        ImageButton btnBack = (ImageButton) findViewById(R.id.back_full_screen);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackBtnPressed = true;
                checkBackButtonState();
            }
        });



    }

    public String convertArrayListToString(ArrayList<ProductOption> informationCartView) {
        int i;
        String informationCartViewContent = Configuration.getConfig().productInfoActivityContext.getResources().getString(R.string.features) + "\n";
        for (i = 0; i < informationCartView.size(); i++) {
            informationCartViewContent += informationCartView.get(i).getTitle() + " : " + informationCartView.get(i).getValue() + "\n";
        }
        return informationCartViewContent;
    }

    private void checkBackButtonState() {
        if (onBackBtnPressed) {
            super.onBackPressed();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {

            case DownloadProductOption.STATUS_FINISHED:
                progressBarProductOption.setVisibility(View.GONE);
                options= (ArrayList<ProductOption>) resultData.getSerializable("options");
                if (options.size()>0)
                    informationCartView.setText(convertArrayListToString(options));
                else
                    informationCartView.setText(context.getResources().getString(R.string.features)+ "\n\n"+
                            "این محصول فاقد ویژگی می باشد");
                ListView listOfAllListViews = (ListView) findViewById(R.id.listOfAllListViews);
                ArrayList<String> commentsAProduct = sch.getContentCommentsAllProduct(productId);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        Configuration.getConfig().productOptionActivityContext,
                        android.R.layout.simple_list_item_1,
                        commentsAProduct);

                listOfAllListViews.setAdapter(arrayAdapter);

                break;

        }

    }
}
