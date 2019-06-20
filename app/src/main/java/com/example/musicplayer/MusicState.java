package com.example.musicplayer;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.text.TextPaint;

import java.util.HashMap;
import java.util.List;

public class MusicState {

    private MediaPlayer mediaPlayer;
    private int currentposition;

    public int listClickplay(List<HashMap<String, String>> list, int position){
            if (mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            currentposition = position;
            startPlayer(list,position);
            return currentposition;
    }

    public int playClick(List<HashMap<String, String>> list){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            startPlayer(list,0);
            return 1;
        }
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return 0;
        }else {
            mediaPlayer.start();
            return 1;
        }
    }

    public int nextClick(List<HashMap<String, String>> list){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            startPlayer(list,0);
        }else {
            if (currentposition+1>=list.size()){
                startPlayer(list,0);
                currentposition = 0;
            }else {
                currentposition+=1;
                startPlayer(list,currentposition);
                System.out.println(list.get(currentposition).get("music"));
            }
        }
        return currentposition;
    }

    public int lastClick(List<HashMap<String, String>> list){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            startPlayer(list,list.size());
        }else {
            if (currentposition-1<=0){
                startPlayer(list,list.size()-1);
                currentposition = list.size()-1;
            }else {
                currentposition-=1;
                startPlayer(list,currentposition);
                System.out.println(list.get(currentposition).get("music"));
            }
        }
        return currentposition;
    }

    private void startPlayer(final List<HashMap<String, String>> list, int position){
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(list.get(position).get("musicpath"));
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("mediaPlayer Play GG");
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextClick(list);
            }
        });
    }


    public void seekTo(int progess) {
        mediaPlayer.seekTo(progess);
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public boolean isNull(){
        return mediaPlayer == null;
    }

    public int getCurrentposition(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getMax(){
        return mediaPlayer.getDuration();
    }
}
