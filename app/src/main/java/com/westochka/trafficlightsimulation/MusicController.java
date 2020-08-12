package com.westochka.trafficlightsimulation;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicController
{
    private MediaPlayer mediaPlayer;
    private Context context;
    private boolean isMusicAllowed;

    public MusicController(Context context, boolean isMusicAllowed)
    {
        this.context = context;
        this.isMusicAllowed = isMusicAllowed;
    }

    public void startMusic()
    {
        if (isMusicAllowed)
            mediaPlayer.start();
    }

    public void pauseMusic()
    {
        if (isMusicAllowed)
            mediaPlayer.pause();
    }

    public void stopMusic()
    {
        if (isMusicAllowed)
        {
            mediaPlayer.pause();
            mediaPlayer.seekTo(Consts.BEGINNING);
        }
    }

    public void setMusic(int numberOfMusic)
    {
        if (mediaPlayer != null)
            mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(context, numberOfMusic);

    }

    public void releaseMusic()
    {
        mediaPlayer.release();
    }
}
