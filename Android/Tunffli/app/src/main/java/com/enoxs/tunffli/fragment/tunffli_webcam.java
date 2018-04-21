package com.enoxs.tunffli.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.camera.simplemjpeg.MjpegView;
import com.camera.simplemjpeg.SettingsActivity;
import com.enoxs.tunffli.Controller;
import com.enoxs.tunffli.MainActivity;
import com.camera.simplemjpeg.MjpegActivity;
import com.enoxs.tunffli.R;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class tunffli_webcam extends Fragment {


    public tunffli_webcam() {
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
        rootView =  inflater.inflate(R.layout.fragment_tunffli_webcam, container, false);
        init();
        return rootView;
    }
    public boolean webcam_flag = false;
    public Button btn_view,btn_webcam;
    public void init(){
        btn_view = (Button) rootView.findViewById(R.id.btn_view);
        btn_webcam = (Button) rootView.findViewById(R.id.btn_webcam);
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MjpegActivity.class));
            }
        });
        btn_webcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("id", "Mjpg");
                    obj.put("reply",false);
                    obj.put("dev_id","0x01");
                    obj.put("value",!webcam_flag);
                    mControl.publish("enoxs",obj.toString());
                }catch(Exception e){

                }
            }
        });
        if(MainActivity.mConfig.get_link_flag()){
            webcam_sta();
        }
    }
    public void webcam_sta(){
        try{
            JSONObject obj_mjpg = new JSONObject();
            obj_mjpg.put("id", "Mjpg");
            obj_mjpg.put("reply",false);
            obj_mjpg.put("dev_id","all");
            mControl.publish("enoxs",obj_mjpg.toString());
        }catch(Exception e){

        }
    }
    public void onStop() {
        super.onStop();
        MainActivity.t_webcam = null;
    }
    public void onResume(){
        super.onResume();
        MainActivity.t_webcam = this;
    }
}
