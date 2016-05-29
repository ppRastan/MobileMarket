package ir.rastanco.mobilemarket.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ShaisteS on 5/29/2016.
 */
public class WebView extends Activity{

    private android.webkit.WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        String url= getIntent().getStringExtra("url");
        browser = (android.webkit.WebView) findViewById(R.id.web_view);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl(url);
    }

    @Override
    public void onBackPressed(){
        if(browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
        }
    }
}