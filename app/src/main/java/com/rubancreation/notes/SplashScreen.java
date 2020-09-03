package com.rubancreation.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_SCREEN = 7000;

    Animation top_animation, bottom_animation;
    ImageView imageView;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        top_animation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom_animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        imageView = findViewById(R.id.image_view);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        imageView.setAnimation(top_animation);
        logo.setAnimation(bottom_animation);
        slogan.setAnimation(bottom_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}