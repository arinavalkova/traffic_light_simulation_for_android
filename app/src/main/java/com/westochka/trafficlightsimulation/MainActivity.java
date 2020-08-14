package com.westochka.trafficlightsimulation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private Button startStopButton;
    private ImageButton dayNightButton;

    ArrayList<FrameLayout> layoutsWithLights = new ArrayList<>();
    MusicController musicController;

    private boolean isDayModeInsteadOfNightMode = true;
    private boolean trafficLightIsWorking = false;

    private boolean isMusicAllowed = true;
    private float speed;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        overridePendingTransition(R.anim.activities_animation,R.anim.alpha);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        connectObjectsWithId();
        setSettingsFromStorage();
        setMusic();
        setCurrentStateOfChangeModeButton();
    }

    private void connectObjectsWithId()
    {
        fillArrayListOfLightsWithIdOfTheirLayouts();
        startStopButton = findViewById(R.id.startOrStopButton);
        dayNightButton = findViewById(R.id.dayOrNightModeButton);
    }

    private void fillArrayListOfLightsWithIdOfTheirLayouts()
    {
        layoutsWithLights.add((FrameLayout) findViewById(R.id.firstLight));
        layoutsWithLights.add((FrameLayout) findViewById(R.id.secondLight));
        layoutsWithLights.add((FrameLayout) findViewById(R.id.thirdLight));
    }

    private void setSettingsFromStorage()
    {
        isMusicAllowed = SettingsActivity.getIsMusicAllowed(this);
        speed = SettingsActivity.getSpeed(this);
        isDayModeInsteadOfNightMode = MainActivity.getIsDayModeInsteadOfNightMode(this);
    }

    public static boolean getIsDayModeInsteadOfNightMode(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        return sharedPreferences.getBoolean(Consts.IS_DAY_MODE_INSTEAD_OF_NIGHT_MODE, true);
    }

    private void setMusic()
    {
        musicController = new MusicController(this, isMusicAllowed);
        setCurrentModeMusic();
        musicController.startMusic();
    }

    private void setCurrentModeMusic()
    {
        if(isDayModeInsteadOfNightMode)
            musicController.setMusic(R.raw.cyber_pank_sound);
        else
            musicController.setMusic(R.raw.night_sound);
    }

    private void setCurrentStateOfChangeModeButton()
    {
        if(isDayModeInsteadOfNightMode)
            changeDayModeImage();
        else
            changeNightModeImage();
    }

    private void changeNightModeImage()
    {
        runOnUiThread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run()
            {
                //dayNightButton.setForeground(getResources().getDrawable(R.drawable.whitemoon));
                dayNightButton.setImageDrawable(getResources().getDrawable(R.drawable.whitemoon));
            }
        });
    }

    private void changeDayModeImage()
    {
        runOnUiThread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run()
            {
                //dayNightButton.setForeground(getResources().getDrawable(R.drawable.blackmoon));
                dayNightButton.setImageDrawable(getResources().getDrawable(R.drawable.blackmoon));
            }
        });
    }

    public void onClickStartOrStopButton(View view)
    {
        if (isTrafficLightAllowedToWorkRightNow())
        {
            disAllowTrafficLightToWork();
        } else
        {
            allowTrafficLightToWork();

            if(isDayModeInsteadOfNightMode)
            {
                startWorkingDayTrafficLightInNewThread();
            }
            else
            {
                startWorkingNightTrafficLightInNewThread();
            }
        }
    }

    private boolean isTrafficLightAllowedToWorkRightNow()
    {
        return trafficLightIsWorking;
    }

    private void disAllowTrafficLightToWork()
    {
        trafficLightIsWorking = false;
        changeButtonText();
    }

    private void allowTrafficLightToWork()
    {
        trafficLightIsWorking = true;
        changeButtonText();
    }

    private void changeButtonText()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (isTrafficLightAllowedToWorkRightNow())
                {
                    startStopButton.setText(Consts.STOP_TEXT);
                }
                else
                {
                    startStopButton.setText(Consts.START_TEXT);
                }
            }
        });
    }

    private void startWorkingDayTrafficLightInNewThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (isTrafficLightAllowedToWorkRightNow())
                {
                    dayTrafficLightWorking();
                }
            }
        }).start();
    }

    private void startWorkingNightTrafficLightInNewThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (isTrafficLightAllowedToWorkRightNow())
                {
                    nightTrafficLightWorking();
                }
            }
        }).start();
    }

    private void dayTrafficLightWorking()
    {
        try
        {
            dayFirstLightWorking();
            daySecondLightWorking();
            dayThirdLightWorking();
            daySecondLightWorking();
        } catch (StopTrafficLightWorkingException stopTrafficLightWorking)
        {
            changeColorOfLightOnUiThreadToGray();
        }
    }

    private void dayFirstLightWorking() throws StopTrafficLightWorkingException
    {
        changeColorOfLightAndSleep(R.drawable.red_round, layoutsWithLights.get(Consts.FIRST_LIGHT_ID), Consts.SIX_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.FIRST_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.red_round, layoutsWithLights.get(Consts.FIRST_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.FIRST_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.red_round, layoutsWithLights.get(Consts.FIRST_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.FIRST_LIGHT_ID), Consts.ZERO_SECOND);
    }

    private void daySecondLightWorking() throws StopTrafficLightWorkingException
    {
        changeColorOfLightAndSleep(R.drawable.yellow_round, layoutsWithLights.get(Consts.SECOND_LIGHT_ID), Consts.THREE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.SECOND_LIGHT_ID), Consts.ZERO_SECOND);
    }

    private void dayThirdLightWorking() throws StopTrafficLightWorkingException
    {
        changeColorOfLightAndSleep(R.drawable.green_round, layoutsWithLights.get(Consts.THIRD_LIGHT_ID), Consts.SIX_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.THIRD_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.green_round, layoutsWithLights.get(Consts.THIRD_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.THIRD_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.green_round, layoutsWithLights.get(Consts.THIRD_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.THIRD_LIGHT_ID), Consts.ZERO_SECOND);
    }

    private void changeColorOfLightOnUiThreadToGray()
    {
        changeColorOfLightOnUiThread(layoutsWithLights.get(Consts.FIRST_LIGHT_ID), R.drawable.grey_round);
        changeColorOfLightOnUiThread(layoutsWithLights.get(Consts.SECOND_LIGHT_ID), R.drawable.grey_round);
        changeColorOfLightOnUiThread(layoutsWithLights.get(Consts.THIRD_LIGHT_ID), R.drawable.grey_round);
    }

    private void nightTrafficLightWorking()
    {
        try
        {
            nightSecondLightWorking();
        } catch (StopTrafficLightWorkingException stopTrafficLightWorking)
        {
            changeColorOfLightOnUiThreadToGray();
        }
    }

    private void nightSecondLightWorking() throws StopTrafficLightWorkingException
    {
        changeColorOfLightAndSleep(R.drawable.yellow_round, layoutsWithLights.get(Consts.SECOND_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.SECOND_LIGHT_ID), Consts.ONE_SECOND);
    }

    private void changeColorOfLightAndSleep(int colorOfLight, FrameLayout light, Integer timeOfSleep) throws StopTrafficLightWorkingException
    {
        changeColorOfLightOnUiThread(light, colorOfLight);
        sleepingForTime((int) (timeOfSleep / speed));
    }

    private void changeColorOfLightOnUiThread(final FrameLayout currentLight, final int idOfColor)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                currentLight.setForeground(getResources().getDrawable(idOfColor));
            }
        });
    }

    private void sleepingForTime(int millis) throws StopTrafficLightWorkingException
    {
        try
        {
            for (int i = 0; i < millis; i += Consts.TEN_MILLIS)
            {
                if (!isTrafficLightAllowedToWorkRightNow())
                    throw new StopTrafficLightWorkingException();
                Thread.sleep(Consts.TEN_MILLIS);
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void onClickDayOrNightMode(View view)
    {
        changeMode();
        saveIsDayModeInsteadOfNightMode(isDayModeInsteadOfNightMode);
        setCurrentStateOfChangeModeButton();
        setCurrentModeMusic();
        musicController.startMusic();
        disAllowTrafficLightToWork();
    }

    private void changeMode()
    {
        isDayModeInsteadOfNightMode = !isDayModeInsteadOfNightMode;
    }

    private void saveIsDayModeInsteadOfNightMode(boolean isDayMode)
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Consts.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Consts.IS_DAY_MODE_INSTEAD_OF_NIGHT_MODE, isDayMode);
        editor.apply();
    }

    public void onClickSettings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        musicController.releaseMusic();
        disAllowTrafficLightToWork();
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        musicController.pauseMusic();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        musicController.startMusic();
        setCurrentStateOfChangeModeButton();
        super.onResume();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        finish();
    }
}