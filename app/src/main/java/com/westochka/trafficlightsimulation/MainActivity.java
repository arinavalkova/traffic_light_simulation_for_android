package com.westochka.trafficlightsimulation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<FrameLayout> lights = new ArrayList<>();
    private Button startStopButton;

    boolean dayModeRunning = false;
    boolean nightModeRunning = false;

    void fillArrayListOfLightsWithTheirId()
    {
        lights.add((FrameLayout) findViewById(R.id.firstLight));
        lights.add((FrameLayout) findViewById(R.id.secondLight));
        lights.add((FrameLayout) findViewById(R.id.thirdLight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillArrayListOfLightsWithTheirId();
        startStopButton = findViewById(R.id.startOrStopButton);
    }

    public void onClickStartOrStopButton(View view)
    {
        if(!dayModeRunning)
        {
            dayModeRunning = true;
            workingDayTrafficLightInNewThread();
        }
        else
        {
            dayModeRunning = false;
        }

        changeButtonText();
    }

    private void changeButtonText()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(checkingIsDayModeEnabled())
                    startStopButton.setText(Consts.STOP_TEXT);
                else
                    startStopButton.setText(Consts.START_TEXT);
            }
        });
    }

    private void workingDayTrafficLightInNewThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(dayModeRunning)
                {
                    trafficLightWorking();
                }
            }
        }).start();
    }

    private void trafficLightWorking()
    {
        try
        {
            firstLightWorking();
            secondLightWorking();
            thirdLightWorking();
            secondLightWorking();
        } catch (StopTrafficLightWorking stopTrafficLightWorking)
        {
            changeColorOfLightOnUiThreadToGray();
        }
    }

    private void changeColorOfLightOnUiThreadToGray()
    {
        changeColorOfLightOnUiThread(lights.get(Consts.FIRST_LIGHT_ID), R.drawable.grey_round);
        changeColorOfLightOnUiThread(lights.get(Consts.SECOND_LIGHT_ID), R.drawable.grey_round);
        changeColorOfLightOnUiThread(lights.get(Consts.THIRD_LIGHT_ID), R.drawable.grey_round);
    }

    private void thirdLightWorking() throws StopTrafficLightWorking
    {
        changeColorOfLightAndSleep(R.drawable.green_round,lights.get(Consts.THIRD_LIGHT_ID), Consts.SIX_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, lights.get(Consts.THIRD_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.green_round,lights.get(Consts.THIRD_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, lights.get(Consts.THIRD_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.green_round,lights.get(Consts.THIRD_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, lights.get(Consts.THIRD_LIGHT_ID), Consts.ZERO_SECOND);
    }

    private void secondLightWorking() throws StopTrafficLightWorking
    {
        changeColorOfLightAndSleep(R.drawable.yellow_round,lights.get(Consts.SECOND_LIGHT_ID), Consts.THREE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round,lights.get(Consts.SECOND_LIGHT_ID), Consts.ZERO_SECOND);
    }

    private void firstLightWorking() throws StopTrafficLightWorking
    {
        changeColorOfLightAndSleep(R.drawable.red_round,lights.get(Consts.FIRST_LIGHT_ID), Consts.SIX_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, lights.get(Consts.FIRST_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.red_round,lights.get(Consts.FIRST_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, lights.get(Consts.FIRST_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.red_round,lights.get(Consts.FIRST_LIGHT_ID), Consts.ONE_SECOND);
        changeColorOfLightAndSleep(R.drawable.grey_round, lights.get(Consts.FIRST_LIGHT_ID), Consts.ZERO_SECOND);
    }

    private void changeColorOfLightAndSleep(int colorOfLight, FrameLayout light, Integer timeOfSleep) throws StopTrafficLightWorking
    {
        changeColorOfLightOnUiThread(light, colorOfLight);
        sleepingForTime(timeOfSleep);
    }

    private boolean checkingIsDayModeEnabled()
    {
        return dayModeRunning;
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

    private void sleepingForTime(int millis) throws StopTrafficLightWorking
    {
        try
        {
            for(int i = 0; i < millis; i += Consts.TEN_MILLIS)
            {
                if(!checkingIsDayModeEnabled())
                    throw new StopTrafficLightWorking();
                Thread.sleep(Consts.TEN_MILLIS);
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void onClickDayOrNightMode(View view)
    {

    }
}