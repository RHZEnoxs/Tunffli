package com.enoxs.tunffli.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.enoxs.tunffli.Controller;
import com.enoxs.tunffli.MainActivity;
import com.enoxs.tunffli.R;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class tunffli_sw_rly extends Fragment {


    public tunffli_sw_rly() {
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
        rootView =  inflater.inflate(R.layout.fragment_tunffli_sw_rly, container, false);
        init();
        return rootView;
    }
    Button btn_x1,btn_x2,btn_x3,btn_x4,btn_x5;
    public void init(){
        btn_x1 = (Button) rootView.findViewById(R.id.btn_x1);
        btn_x2 = (Button) rootView.findViewById(R.id.btn_x2);
        btn_x3 = (Button) rootView.findViewById(R.id.btn_x3);
        btn_x4 = (Button) rootView.findViewById(R.id.btn_x4);
        btn_x5 = (Button) rootView.findViewById(R.id.btn_x5);
        btn_x1.setOnClickListener(btn_lister);
        btn_x2.setOnClickListener(btn_lister);
        btn_x3.setOnClickListener(btn_lister);
        btn_x4.setOnClickListener(btn_lister);
        btn_x5.setOnClickListener(btn_lister);

    }
    private Button.OnClickListener btn_lister=new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_x1:
                    ctrl_command(1);
                    break;
                case R.id.btn_x2:
                    ctrl_command(2);
                    break;
                case R.id.btn_x3:
                    ctrl_command(3);
                    break;
                case R.id.btn_x4:
                    ctrl_command(4);
                    break;
                case R.id.btn_x5:
                    ctrl_command(5);
                    break;
            }
        }
    };
    public void  ctrl_command(int which){
        try{
            JSONObject obj = new JSONObject();
            obj.put("id", "Relay");
            obj.put("reply",false);
            obj.put("dev_id","0x0"+which);
            obj.put("value",true);
            mControl.publish("enoxs",obj.toString());
        }catch(Exception e){}
    }
    public void onStop() {
        super.onStop();
        MainActivity.t_sw_rly = null;
    }
    public void onResume(){
        super.onResume();
        MainActivity.t_sw_rly = this;
    }
}
