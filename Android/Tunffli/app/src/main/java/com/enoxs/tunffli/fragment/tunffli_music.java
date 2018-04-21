package com.enoxs.tunffli.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enoxs.tunffli.Controller;
import com.enoxs.tunffli.MainActivity;
import com.enoxs.tunffli.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class tunffli_music extends Fragment {


    public tunffli_music() {
        // Required empty public constructor
    }
    public Controller mControl;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mControl = (Controller) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DeviceController callback interface.");
        }
    }
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_tunffli_music, container, false);
        init();
        return rootView;
    }
    JSONObject obj;
    ArrayList music_file;MyAdapter myAdapter;
    String[] item_Text1 = {"Desktop","Enoxs","RHZ","Hello","Again","QQ123"};
    ImageButton fab_menu, fab_music_play, fab_music_stop, fab_music_list;
    private boolean fab_flag =false;
    public boolean media_flag = false;
    public void init(){
        music_file = new ArrayList<String>();
//        for (int i = 0; i < item_Text1.length; i++) {
//            music_file.add(item_Text1[i]);
//        }
        myAdapter = new MyAdapter(music_file,getContext());
        myAdapter.music_position = -1;
        RecyclerView mList = (RecyclerView) rootView.findViewById(R.id.list_record);
        mList.getItemAnimator().setMoveDuration(200);
        final GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 1);
        gridManager.setOrientation(GridLayoutManager.VERTICAL);
        mList.setLayoutManager(gridManager);
        mList.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                int position = Integer.parseInt(data);
                if( position <  myAdapter.music_position){
                    if(fold_flag == -1){
                        filepath_position(position);
                    }else{
                        filepath_position(-1);
                    }
                }else{
                    String file = "";
                    String selectedItem = music_file.get(position) + "";
                    if(fold_flag == -1){
                        file = selectedItem;
                    }else{
                        try{
                            file = obj.getJSONArray("fold_list").getString(fold_flag)+"/"+selectedItem;
                        }catch(Exception e){}
                    }
                    ctrl_media("play",file);
                    //JOptionPane.showMessageDialog(null,file);
                    Toast.makeText(getActivity(), music_file.get(position) + "",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
        fab_menu = (ImageButton) rootView.findViewById(R.id.fab_menu);
        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_status();
            }
        });
        fab_menu.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    fab_menu.setBackgroundResource(R.drawable.fab_down);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    fab_menu.setBackgroundResource(R.drawable.fab_up);
                }
                return false;
            }
        });
        fab_music_play = (ImageButton) rootView.findViewById(R.id.fab_ble);
        fab_music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(media_flag){
                    ctrl_media("pause","");
                }else{
                    ctrl_media("play","");
                }
            }
        });
        fab_music_play.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    fab_music_play.setBackgroundResource(R.drawable.fab_down);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    fab_music_play.setBackgroundResource(R.drawable.fab_up);
                }
                return false;
            }
        });
        fab_music_stop = (ImageButton) rootView.findViewById(R.id.fab_alert);
        fab_music_stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ctrl_media("stop","");
            }
        });
        fab_music_stop.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    fab_music_stop.setBackgroundResource(R.drawable.fab_down);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    fab_music_stop.setBackgroundResource(R.drawable.fab_up);
                }
                return false;
            }
        });
        fab_music_list = (ImageButton) rootView.findViewById(R.id.fab_storage);
        fab_music_list.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ctrl_media("fold_file","refresh");
            }
        });
        fab_music_list.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    fab_music_list.setBackgroundResource(R.drawable.fab_down);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    fab_music_list.setBackgroundResource(R.drawable.fab_up);
                }
                return false;
            }
        });
        try{
            obj = new JSONObject(LoadData());
            filepath_position(-1);
        }catch(Exception e){
        }
        ctrl_media("media_flag","status");
    }
    private void fab_status(){
        if(fab_flag){
            fab_flag = false;
            Animation anim_menu = new RotateAnimation(0, 360 ,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim_menu.setDuration(500);
            anim_menu.setStartOffset(50);
            Animation anim_1 = new AlphaAnimation(1.0f, 0.0f);
            anim_1.setDuration(500);
            anim_1.setStartOffset(450);
            Animation anim_2 = new AlphaAnimation(1.0f, 0.0f);
            anim_2.setDuration(500);
            anim_2.setStartOffset(300);
            Animation anim_3 = new AlphaAnimation(1.0f, 0.0f);
            anim_3.setDuration(500);
            anim_3.setStartOffset(150);
            fab_menu.startAnimation(anim_menu);
            fab_music_play.startAnimation(anim_1);
            fab_music_play.setVisibility(View.GONE);
            fab_music_stop.startAnimation(anim_2);
            fab_music_stop.setVisibility(View.GONE);
            fab_music_list.startAnimation(anim_3);
            fab_music_list.setVisibility(View.GONE);
        }
        else{
            Animation anim_menu = new RotateAnimation(360, 0 ,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim_menu.setDuration(500);
            anim_menu.setStartOffset(50);
            Animation anim_1 = new AlphaAnimation(0.0f, 1.0f);
            anim_1.setDuration(500);
            anim_1.setStartOffset(150);
            Animation anim_2 = new AlphaAnimation(0.0f, 1.0f);
            anim_2.setDuration(500);
            anim_2.setStartOffset(300);
            Animation anim_3 = new AlphaAnimation(0.0f, 1.0f);
            anim_3.setDuration(500);
            anim_3.setStartOffset(450);
//            anim_2.setRepeatCount(Animation.INFINITE);
            fab_menu.startAnimation(anim_menu);
            fab_flag = true;
            fab_music_play.setVisibility(View.VISIBLE);
            fab_music_play.startAnimation(anim_1);
            fab_music_stop.setVisibility(View.VISIBLE);
            fab_music_stop.startAnimation(anim_2);
            fab_music_list.setVisibility(View.VISIBLE);
            fab_music_list.startAnimation(anim_3);
        }
    }
    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements
            View.OnClickListener {
        private List<String> mfile;
        public int music_position = -1;
        Context mConetxt;
        public MyAdapter(List<String>file ,Context mt) {
            mConetxt = mt;
            mfile = file;
        }

        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        //define interface
        public interface OnRecyclerViewItemClickListener {
            void onItemClick(View view, String data);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recy_list, parent, false);
            ViewHolder vh = new ViewHolder(v);
            v.setOnClickListener(this);
            return vh;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意這裡使用getTag方法獲取數據
                mOnItemClickListener.onItemClick(v, (String) v.getTag());
            }
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public int getItemCount() {
            return mfile.size();
        }//獲取數據數量

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public ImageView mImageView;
            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.record_item_text);
                mImageView = (ImageView) v.findViewById(R.id.record_item_image);
            }
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(position < music_position){
                holder.mImageView.setImageResource(R.drawable.tunffli_music_fold);
            }else{
                holder.mImageView.setImageResource(R.drawable.tunffli_music_file);
            }
            holder.mTextView.setText(mfile.get(position));
            holder.itemView.setTag(position + "");
        }
    }
    int fold_flag = -1;
    public void filepath_position(int position){
        try{
            music_file.clear();
            if(position == -1){
                fold_flag = -1;
                myAdapter.music_position  = obj.getJSONArray("fold_list").length();
                for(int i=0;i<myAdapter.music_position;i++){
                    music_file.add(obj.getJSONArray("fold_list").getString(i));
                }
                for(int i=myAdapter.music_position;i<obj.getJSONArray("file_list").length();i++){
                    music_file.add(obj.getJSONArray("file_list").getString(i));
                }
            }else{
                fold_flag = position;
                music_file.add("..");
                myAdapter.music_position = 1;
                for(int i=0;i<obj.getJSONArray("file_list").getJSONArray(position).length();i++){
                    music_file.add(obj.getJSONArray("file_list").getJSONArray(position).getString(i));
                }
            }
            myAdapter.notifyDataSetChanged();
        }catch(Exception e){
        }
    }
    public String LoadData(){
        FileReader fr;
        StringBuilder StrShow = new StringBuilder();;
        try {
            fr = new FileReader("/sdcard/Tunffli/config/music_path.json");
            //將BufferedReader與FileReader做連結
            BufferedReader br = new BufferedReader(fr);
            String temp = br.readLine(); //readLine()讀取一整行
            ArrayList list = new ArrayList();
            while (temp!=null ){
                if(temp.length()>3){
                    list.add(temp+"\n");
                }
                temp=br.readLine();
            }
            for(int i=0;i<list.size();i++){
                StrShow.append(list.get(i)+"");
            }
            br.close();
        } catch (IOException e) {
            // TODO 自動產生的 catch 區塊
            e.printStackTrace();
        }
        return StrShow.toString();
    }
    public void SaveData(String content){//1.csv 2.txt
        String title="music_path.json";
        String path="/sdcard/Tunffli/config/";
        File file1=new File(path);
        if(!file1.exists())
        {
            file1.mkdirs();
        }
        try{
            FileWriter fw = new FileWriter(path + title, false);
            BufferedWriter bw = new BufferedWriter(fw);
            //bw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
            bw.write(content);
            bw.newLine();
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void ctrl_media(String ctrl,String file){
        try{
            JSONObject obj = new JSONObject();
            obj.put("id", "Madplay");
            obj.put("reply",false);
            obj.put("action",ctrl);
            obj.put("value",file);
            mControl.publish("enoxs",obj.toString());
        }catch (Exception e){

        }
    }
    public void reback(String action ,boolean flag){
        media_flag = flag;
        if(media_flag){
            fab_music_play.setImageResource(R.drawable.tunffli_music_pause);
        }else{
            fab_music_play.setImageResource(R.drawable.tunffli_music_play);
        }
    }
    public void reback(JSONObject msg_obj){
        SaveData(msg_obj.toString());
        try{
            obj = new JSONObject(LoadData());
            filepath_position(-1);
        }catch (Exception e){

        }
    }
    public void onStop() {
        super.onStop();
        MainActivity.t_music = null;
    }
    public void onResume(){
        super.onResume();
        MainActivity.t_music = this;
    }
}

