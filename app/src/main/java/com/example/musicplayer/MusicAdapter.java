package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class MusicAdapter extends BaseAdapter {
    List<HashMap<String, String>> list;
    Context context;
    int layout;
    LayoutInflater inflater;
    TextView songName;
    TextView singer;
    TextView order;
    ImageView img;
    public MusicAdapter(List list, Context context, int layout){
        this.list=list;
        this.context=context;
        this.layout=layout;
        inflater=inflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        view = LayoutInflater.from(context).inflate(layout,null);
        songName = view.findViewById(R.id.songname);
        singer = view.findViewById(R.id.signer);
        order = view.findViewById(R.id.order);
        img = view.findViewById(R.id.img);
        img.setVisibility(View.INVISIBLE);
        if (list.get(position).get("isplaying") == "1"){
            img.setVisibility(View.VISIBLE);
        }
        String name = list.get(position).get("music");
        System.out.println(list);
        System.out.println(name);
        songName.setText(name.substring(0,name.length()-4));
        singer.setText(list.get(position).get("singer"));
        int ordernum = position+1;
        order.setText(ordernum+"");
        return view;
    }


}
