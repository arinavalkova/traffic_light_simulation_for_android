package com.westochka.trafficlightsimulation;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private Button startStopButton;
    private Button dayNightButton;

    ArrayList<FrameLayout> layoutsWithLights = new ArrayList<>();

    private boolean dayModeEnableInsteadNightMode = true;
    private boolean trafficLightIsWorking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectObjectsWithId();
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

    public void onClickStartOrStopButton(View view)
    {
        if (isTrafficLightAllowedToWorkRightNow())
        {
            disAllowTrafficLightToWork();
        } else
        {
            allowTrafficLightToWork();

            if(isDayModeEnableInsteadNightMode())
            {
                startWorkingDayTrafficLightInNewThread();
            }
            else
            {
                startWorkingNightTrafficLightInNewThread();
            }
        }
    }

    private boolean isDayModeEnableInsteadNightMode()
    {
        return dayModeEnableInsteadNightMode;
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

    private void changeColorOfLightOnUiThreadToGray()
    {
        changeColorOfLightOnUiThread(layoutsWithLights.get(Consts.FIRST_LIGHT_ID), R.drawable.grey_round);
        changeColorOfLightOnUiThread(layoutsWithLights.get(Consts.SECOND_LIGHT_ID), R.drawable.grey_round);
        changeColorOfLightOnUiThread(layoutsWithLights.get(Consts.THIRD_LIGHT_ID), R.drawable.grey_round);
    }

    private void nightSecondLightWorking() throws StopTrafficLightWorkingException
    {
        changeColorOfLightAndSleep(R.drawable.yellow_round, layoutsWithLights.get(Consts.SECOND_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, layoutsWithLights.get(Consts.SECOND_LIGHT_ID), Consts.ONE_SECOND);
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

    private void changeColorOfLightAndSleep(int colorOfLight, FrameLayout light, Integer timeOfSleep) throws StopTrafficLightWorkingException
    {
        changeColorOfLightOnUiThread(light, colorOfLight);
        sleepingForTime(timeOfSleep);
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
        disAllowTrafficLightToWork();
        if (isDayModeEnableInsteadNightMode())
        {
            startNightMode();
        } else
        {
            startDayMode();
        }
    }

    private void startNightMode()
    {
        dayModeEnableInsteadNightMode = false;
        changeNightModeImage();
    }

    private void startDayMode()
    {
        dayModeEnableInsteadNightMode = true;
        changeDayModeImage();
    }

    private void changeNightModeImage()
    {
        runOnUiThread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run()
            {
                //dayNightButton.setForeground(getResources().getDrawable(R.drawable.blue_moon));
                dayNightButton.setText(Consts.NIGHT_MODE_TEXT);
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
                //dayNightButton.setForeground(getResources().getDrawable(R.drawable.yellow_moon));
                dayNightButton.setText(Consts.DAY_MODE_TEXT);
            }
        });
    }

    public void onClickSettings()
    {

    }
}