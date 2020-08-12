package com.westochka.trafficlightsimulation;

import android.app.Activity;
import android.os.Bundle;

public class LogoActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_activity);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
    }
}
