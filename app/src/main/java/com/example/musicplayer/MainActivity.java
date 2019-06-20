package com.example.musicplayer;

import android.Manifest;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Runnable {
    public static List<HashMap<String, String>> list;
    ListView lv;
    Button play, next, last;
    SeekBar seekBar;
    TextView text, songname;
    MusicAdapter musicAdapter;
    Handler handler;
    int playing=-1;
    private MusicState musicState = new MusicState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        readDataExternal();
        Thread thread = new Thread(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    seekBar.setProgress(msg.arg1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void readDataExternal(){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    666);
            System.out.println("requested");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 666:
                if (grantResults.length > 0) {
                    System.out.println("Permission OK");
                    Cursor cursor = MainActivity.this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            null, null, null, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("music",
                                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                            hashMap.put("musicpath",
                                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                            hashMap.put("singer",
                                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                            hashMap.put("duration",
                                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                            hashMap.put("isplaying","0");
                            list.add(hashMap);
                        }
                    }
                } else {
                    System.out.println("Permission GG");
                }
                break;
        }
        System.out.println(list);
        musicAdapter = new MusicAdapter(list, MainActivity.this, R.layout.item);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(musicAdapter);
        onClickSetting();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Message msg = handler.obtainMessage();
            if(!musicState.isNull() && musicState.isPlaying() ){
                msg.arg1 = musicState.getCurrentposition();
                handler.sendMessage(msg);
                try{
                    Thread.sleep(1000);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("thread sleep gg");
                }
            }
        }
    }
    public void onClickSetting(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                musicState.listClickplay(list,arg2);
                play.setText("Pause");
                seekBar.setMax(musicState.getMax());
                if(arg2 != playing){

                    list.get(arg2).put("isplaying","1");
                    if (playing!=-1){
                        list.get(playing).put("isplaying","0");
                    }

                    musicAdapter.notifyDataSetChanged();
                    playing=arg2;
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int i =musicState.playClick(list);
                if(i==1){
                    play.setText("Pause");
                    seekBar.setMax(musicState.getMax());
                }else if(i==0){
                    play.setText("Play");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                playing = musicState.nextClick(list);
                play.setText("Pause");
                seekBar.setMax(musicState.getMax());
                list.get(playing-1).put("isplaying","0");
                list.get(playing).put("isplaying","1");
                musicAdapter.notifyDataSetChanged();
            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            // TODO Auto-generated method stub
                playing = musicState.lastClick(list);
                play.setText("Pause");
                seekBar.setMax(musicState.getMax());
                list.get(playing+1).put("isplaying","0");
                list.get(playing).put("isplaying","1");
                musicAdapter.notifyDataSetChanged();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                musicState.seekTo(seekBar.getProgress());
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                musicState.seekTo(progress);
            }
        });
    }
    void initview() {
        list = new ArrayList<HashMap<String, String>>();
        play =  findViewById(R.id.btn_play);
        last =  findViewById(R.id.btn_last);
        next =  findViewById(R.id.btn_next);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        text = (TextView) findViewById(R.id.text);
        songname = (TextView) findViewById(R.id.songname);
    }
}

