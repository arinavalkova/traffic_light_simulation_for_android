package com.westochka.trafficlightsimulation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends Activity
{
    ImageButton backToMainActivityButton;
    CheckBox isMusicAllowedCheckBox;
    SeekBar speedValueSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        overridePendingTransition(R.anim.activities_animation,R.anim.alpha);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        connectObjectsWithId();
        refreshActivity();
        setSpeedSeekBarEvents();
    }

    private void connectObjectsWithId()
    {
        backToMainActivityButton = findViewById(R.id.backButton);
        isMusicAllowedCheckBox = findViewById(R.id.musicCheckBox);
        speedValueSeekBar = findViewById(R.id.speadSeekBar);
    }

    private void refreshActivity()
    {
        refreshIsMusicAllowedCheckBox();
        refreshSpeedSeekBar();
    }

    private void refreshIsMusicAllowedCheckBox()
    {
        boolean isMusicAllowed = getIsMusicAllowed(this);

        isMusicAllowedCheckBox.setChecked(isMusicAllowed);
    }

    private void refreshSpeedSeekBar()
    {
        float speedValue = getSpeed(this);

        speedValueSeekBar.setProgress((int) (speedValue * 10));
    }

    private void setSpeedSeekBarEvents()
    {
        speedValueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                float speedValue = (float) seekBar.getProgress() / 10;

                saveSpeedSeekBarValue(speedValue);
                setToastWithCurrentSpeedValue(speedValue);
            }
        });
    }

    private void saveSpeedSeekBarValue(float progress)
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat(Consts.SPEED_FIELD, progress);
        editor.apply();
    }

    private void setToastWithCurrentSpeedValue(float speedValue)
    {
        Toast toast = Toast.makeText(getApplicationContext(), Float.toString(speedValue), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onClickedBackToMainActivityButton(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickedMusicCheckBox(View view)
    {
        boolean isMusicAllowed = isMusicAllowedCheckBox.isChecked();

        saveIsMusicAllowed(isMusicAllowed);
    }

    private void saveIsMusicAllowed(boolean isMusicAllowed)
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Consts.IS_MUSIC_ALLOWED, isMusicAllowed);
        editor.apply();
    }

    public static boolean getIsMusicAllowed(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        boolean defaultIsMusicAllowed = context.getResources().getBoolean(R.bool.isMusicAllowed);

        return sharedPreferences.getBoolean(Consts.IS_MUSIC_ALLOWED, defaultIsMusicAllowed);
    }

    public static float getSpeed(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        int defaultSpeed = context.getResources().getInteger(R.integer.speed);

        return sharedPreferences.getFloat(Consts.SPEED_FIELD, defaultSpeed);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        finish();
    }
}
