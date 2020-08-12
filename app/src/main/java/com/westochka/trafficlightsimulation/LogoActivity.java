package com.westochka.trafficlightsimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LogoActivity extends Activity
{
    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_activity);

        connectObjectsWithId();
        setLogoAnimation();
        sleepingLogoActivity();
    }

    private void connectObjectsWithId()
    {
        logoImageView = findViewById(R.id.logoImageView);
    }

    private void setLogoAnimation()
    {
        Animation logoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim);
        logoImageView.startAnimation(logoAnimation);
    }

    private void sleepingLogoActivity()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                sleep(Consts.TWO_SECONDS);
                loadMainActivity();
            }
        }).start();
    }

    private void sleep(int time)
    {
        try
        {
            Thread.sleep(time);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void loadMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
    }
}
