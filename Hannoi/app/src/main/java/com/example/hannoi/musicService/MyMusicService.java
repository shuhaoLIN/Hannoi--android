package com.example.hannoi.musicService;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import com.example.hannoi.R;

import java.io.IOException;

/**
 * Created by lenovo on 2019/3/1.
 * 这个不好用啊。。。。就不用了
 */
public class MyMusicService extends Service{
    private MediaPlayer mp;
    private int[] musics = {R.raw.ai_ru_cao_shui, R.raw.lao_nan_hai, R.raw.zhiwudazhanjiangshi};
    private int i=1;
    @Override
    public void onStart(Intent intent,int startId){
        mp.start();
        //处理播放完成后的事件
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try{
//                    i = ++i % 3;
//                    mp.setDataSource(String.valueOf(musics[i]));
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                try{
                    mp.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return  false;
            }
        });
        super.onStart(intent, startId);
    }
    @Override
    public void onCreate(){
        try{
            mp = new MediaPlayer();
            mp = MediaPlayer.create(this,R.raw.ai_ru_cao_shui);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onCreate();
    }
    @Override
    public void onDestroy(){
        mp.stop();
        mp.release();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
