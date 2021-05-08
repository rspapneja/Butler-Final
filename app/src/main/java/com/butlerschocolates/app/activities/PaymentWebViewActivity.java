package com.butlerschocolates.app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.butlerschocolates.app.BuildConfig;
import com.butlerschocolates.app.R;
import com.butlerschocolates.app.util.Utilities;


public class PaymentWebViewActivity extends AppCompatActivity {

    WebView paymentWebView;
    ProgressBar progress_bar;

    WebSettings ws;

    String loadUrl;

    int backPressCount=0;

    Utilities utilities;

    String paymentStatus="force_close";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        utilities=new Utilities(this);

        paymentWebView = (WebView) findViewById(R.id.PaymentwebView);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        ws = paymentWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);

        paymentWebView.setWebViewClient(new PaymenteWebViewClient());

        Intent i = getIntent();
        loadUrl = i.getStringExtra("loadUrl");

        loadWebview(loadUrl);
    }

    private void loadWebview(String Url) {
        paymentWebView.loadUrl(Url);
    }

    public class PaymenteWebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (BuildConfig.DEBUG) {
                if(Uri.parse(url).getHost().equals("butlers.dev.qkangaroo.com"))
                {
                    progress_bar.setVisibility(View.VISIBLE);
                }
                else{
                    progress_bar.setVisibility(View.GONE);
                }

            } else {
                if(Uri.parse(url).getHost().equals("butlers.qkangaroo.com"))
                {
                    progress_bar.setVisibility(View.VISIBLE);
                }
                else{
                    progress_bar.setVisibility(View.GONE);
                }
            }

            view.loadUrl(url);

            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress_bar.setVisibility(View.GONE);

            if (url.endsWith(urlRegex())) {
                Intent i =new Intent();
                i.putExtra("paymentStatus","auto_close");
                setResult(2, i);
                finish();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        }
    }

    @Override
    public void onBackPressed() {
        if(backPressCount==0)
        {
            utilities.showAlert("Payment","Please wait while payment is being processed.");
            backPressCount++;
        }
        else if(backPressCount==1)
        {
         utilities.showAlertWithActions("Payment", "Your payment is being processed.Do you really want to terminate the process?", "YES", "NO", new Utilities.ActionButtons() {
         @Override
         public void okAction() {
             Intent i =new Intent();
             i.putExtra("paymentStatus","force_close");
             setResult(2, i);
             finish();
         }
         @Override
         public void cancelAction() {
         }
        });
     }
     else
     {
        super.onBackPressed();
     }
    }

    public String urlRegex() {
        if (BuildConfig.DEBUG) {
            return "https://butlers.dev.qkangaroo.com/ipn/sagesilent.php";
        } else {
            return "https://butlers.qkangaroo.com/ipn/sagesilent.php";
        }
    }
}
