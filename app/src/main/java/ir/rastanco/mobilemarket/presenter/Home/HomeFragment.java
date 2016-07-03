package ir.rastanco.mobilemarket.presenter.Home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.dataModel.serverConnectionModel.ServerConnectionHandler;
import ir.rastanco.mobilemarket.presenter.Services.DownloadResultReceiver;
import ir.rastanco.mobilemarket.presenter.Services.DownloadService;
import ir.rastanco.mobilemarket.utility.Configuration;

/**
 * Created by parisan on 02/07/2016.
 */
public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener {

    private SliderLayout topPageSlider;
    private SliderLayout downPageSlider;
    private RecyclerView horizontal_recycler_view;
    private ArrayList<String> horizontalList;
    private HorizontalAdapter horizontalAdapter;
    private ServerConnectionHandler serverConnectionHandler;
    private DownloadResultReceiver mReceiver;
    private FragmentActivity myContext;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View homeView = inflater.inflate(R.layout.fragment_main_home, container, false);
        myContext = (FragmentActivity) Configuration.getConfig().shopFragmentContext;
        topPageSlider = (SliderLayout)homeView.findViewById(R.id.slider);
        downPageSlider = (SliderLayout)homeView.findViewById(R.id.second_slider);
//        context=this;
        serverConnectionHandler = ServerConnectionHandler.getInstance(context);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        //code haye ertebat ba server vase namayeshe ax ha
        serverConnectionHandler = ServerConnectionHandler.getInstance(context);
        if (ni != null && ni.isConnectedOrConnecting()) {
            Configuration.getConfig().connectionStatus = true;
        }
        if (serverConnectionHandler.emptyUserInfo())
            Configuration.getConfig().userLoginStatus = false; //please login
        else Configuration.getConfig().userLoginStatus = true;//

        if (serverConnectionHandler.emptyDBCategory()) {
            Configuration.getConfig().emptyCategoryTable=true;
            mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver((DownloadResultReceiver.Receiver) getContext());
            Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), DownloadService.class);
            /* Send optional extras to Download IntentService */
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", 101);
            getActivity().startService(intent);

        }
        else
            Configuration.getConfig().emptyCategoryTable=false;
        if (serverConnectionHandler.emptyDBProduct()) {
            Configuration.getConfig().emptyProductTable = true;
        }
        else {
            Configuration.getConfig().emptyProductTable = false;
        }
        //in code ha ax haye slider avval ra modiriat mikonnd
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
         url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.mipmap.ic_launcher);
        file_maps.put("Big Bang Theory",R.mipmap.ic_launcher);
        file_maps.put("House of Cards",R.mipmap.ic_launcher);
        file_maps.put("Game of Thrones", R.mipmap.ic_launcher);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            topPageSlider.addSlider(textSliderView);
        }
        topPageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        topPageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        topPageSlider.setCustomAnimation(new DescriptionAnimation());
        topPageSlider.setDuration(4000);
        topPageSlider.addOnPageChangeListener((ViewPagerEx.OnPageChangeListener) getContext());

        //in code ha ax haye slider dovom ra modiriat mikonnd

        HashMap<String,String> secod_slider_url_maps = new HashMap<String, String>();
        secod_slider_url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        secod_slider_url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        secod_slider_url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        secod_slider_url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> second_slider_file_maps = new HashMap<String, Integer>();
        second_slider_file_maps.put("Hannibal",R.mipmap.ic_launcher);
        second_slider_file_maps.put("Big Bang Theory",R.mipmap.ic_launcher);
        second_slider_file_maps.put("House of Cards",R.mipmap.ic_launcher);
        second_slider_file_maps.put("Game of Thrones", R.mipmap.ic_launcher);

        for(String name : second_slider_file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            downPageSlider.addSlider(textSliderView);
        }
        //// TODO: 01/07/2016 we can hide animation here
//        downPageSlider.clearAnimation();
        downPageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        downPageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        downPageSlider.setCustomAnimation(new DescriptionAnimation());
        downPageSlider.setDuration(4000);
        downPageSlider.addOnPageChangeListener((ViewPagerEx.OnPageChangeListener) getContext());


        //inja recyclerivew haye safhe ro modiriyat mikonim
        //vizheha
        //tamin konnande ha
        //por forosh tarin ha
        //maghalat

        horizontal_recycler_view = (RecyclerView) homeView.findViewById(R.id.horizontal_recycler_view_special_product);
        horizontalList = new ArrayList<>();
        horizontalList.add("horizontal 1");
        horizontalList.add("horizontal 2");
        horizontalList.add("horizontal 3");
        horizontalList.add("horizontal 4");
        horizontalList.add("horizontal 5");
        horizontalList.add("horizontal 6");
        horizontalList.add("horizontal 7");
        horizontalList.add("horizontal 8");
        horizontalList.add("horizontal 9");
        horizontalList.add("horizontal 10");
        horizontalAdapter = new HorizontalAdapter(horizontalList,getContext());

        LinearLayoutManager horizontalLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        return homeView;

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
