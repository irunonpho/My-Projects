package ducnguyen.hex;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by ducng on 12/21/2016.
 */

public class Soundeffects{
    private MediaPlayer mp;
    private boolean playedOnce;

    Soundeffects(Context context, int res){
        mp = MediaPlayer.create(context, res);
        if(GamePanel.sound) {
            float log1 = (float) (Math.log(GamePanel.MAXVOLUME - GamePanel.VOLUME) / Math.log(GamePanel.MAXVOLUME));
            mp.setVolume(1 - log1, 1 - log1);
        }
        else{
            mp.setVolume(0,0);
        }
        playedOnce = false;
    }
    public void play(){
        playedOnce = true;
        mp.start();
    }
    public void release(){
        mp.release();
    }
    public boolean isPlayedOnce(){
        return playedOnce;
    }
    public boolean isPlaying(){
        return mp.isPlaying();
    }
}
