package com.rubancreation.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PrivacyPolicy extends AppCompatActivity {

    //variable
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        webView = findViewById(R.id.privacyPolicyWebview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://ruban-app-creations.blogspot.com/2020/08/notes-writing-master-app-privacy-policy.html");

        //setting title
        getSupportActionBar().setTitle("Privacy Policy");

        //setting back button
        getSupportActionBar().setHomeButtonEnabled(true);


    }
}