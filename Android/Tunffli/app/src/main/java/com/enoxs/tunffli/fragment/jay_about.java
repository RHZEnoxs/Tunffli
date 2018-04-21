package com.enoxs.tunffli.fragment;


import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enoxs.tunffli.Controller;
import com.enoxs.tunffli.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class jay_about extends Fragment {

    private Controller mControl;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mControl = (Controller) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DeviceController callback interface.");
        }
    }
    public jay_about() {
        // Required empty public constructor
    }
    View rootView;TextView tv;LinearLayout  head,medium;
    String lang = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about, container, false);
        head = (LinearLayout) rootView.findViewById(R.id.jay5_head);
        medium = (LinearLayout) rootView.findViewById(R.id.jay5_medium);
        tv = (TextView) rootView.findViewById(R.id.txt_inf);
//        switch(mControl.mParameter().get_language()){
//            case 0:
//                lang = "";
//                break;
//            case 1:
//                lang = "_cn";
//                break;
//            case 2:
//                lang = "_en";
//                break;
//        }
        String str = getAsset("about"+lang+".txt");
        tv.setAutoLinkMask(Linkify.ALL);
        tv.setText(str);
//
//        //---add_hideDemo--
//        head.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog_timerpick();
//            }
//        });


        return rootView;
    }
    private String getAsset(String fileName) {
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName, AssetManager.ACCESS_BUFFER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scanner(is).useDelimiter("\\Z").next();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mControl.Back_Page();
    }
}
