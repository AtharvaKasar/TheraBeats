package com.themagicofmusic.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.media.*;

public class MediaPlayerService extends Service  implements MediaPlayer.OnPreparedListener{
    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static final String ACTION_PLAY = "com.example.action.PLAY";
    MediaPlayer mMediaPlayer = null;

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(ACTION_PLAY)) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        }
        //Atharva - temp done
        return Service.START_STICKY;
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        player.start();
    }
}
