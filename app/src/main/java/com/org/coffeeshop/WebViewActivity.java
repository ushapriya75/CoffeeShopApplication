package com.org.coffeeshop;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.org.coffeeshop.Utils.Constant;

public class WebViewActivity extends AppCompatActivity {

    WebView myWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String webLink = intent.getStringExtra(Constant.WEB_LINK);

        myWebview=findViewById(R.id.webview);
        myWebview.loadUrl(webLink);
    }
}
