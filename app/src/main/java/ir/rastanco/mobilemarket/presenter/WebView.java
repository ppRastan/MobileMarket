package ir.rastanco.mobilemarket.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOK;
import ir.rastanco.mobilemarket.presenter.Observer.ObserverConnectionInternetOKListener;
import ir.rastanco.mobilemarket.utility.Utilities;

/**
 * Created by ShaisteS on 5/29/2016.
 */
public class WebView extends Activity{

    private Context context;
    private android.webkit.WebView webView;
    private ImageButton backButtonPressed;
    private TextView txtCheckNetworkConnectionMessage;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        context=this;
        url= getIntent().getStringExtra("url");
        webView = (android.webkit.WebView) findViewById(R.id.web_view);
        backButtonPressed = (ImageButton)findViewById(R.id.actionbar_back_icon);
        txtCheckNetworkConnectionMessage=(TextView)findViewById(R.id.txt_checkNetwork_message);


        Boolean checkConnectionStatus= Utilities.getInstance().checkNetworkConnection(context);
        if(checkConnectionStatus)
            showWebView();
        else
            hideWebView();

        ObserverConnectionInternetOK.ObserverConnectionInternetOKListener(new ObserverConnectionInternetOKListener() {
            @Override
            public void connectionOK() {
                showWebView();
            }
        });

        backButtonPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoBack()) {
                    webView.goBack();
                } else {
                    onBackPressed();
                }
            }
        });
    }

    private void showWebView(){
        txtCheckNetworkConnectionMessage.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    private void hideWebView(){
        webView.setVisibility(View.GONE);
        txtCheckNetworkConnectionMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed(){
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}