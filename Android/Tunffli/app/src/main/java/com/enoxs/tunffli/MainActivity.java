package com.enoxs.tunffli;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.enoxs.tunffli.Mqtt.MQTTConnectionConstants;
import com.enoxs.tunffli.Mqtt.MQTTConstants;
import com.enoxs.tunffli.Mqtt.MQTTMessage;
import com.enoxs.tunffli.Mqtt.MQTTService;
import com.enoxs.tunffli.fragment.jay_about;
import com.enoxs.tunffli.fragment.tunffli_music;
import com.enoxs.tunffli.fragment.tunffli_sw_rly;
import com.enoxs.tunffli.fragment.tunffli_webcam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Controller,MQTTConnectionConstants,
        MQTTConstants {
    // --- MQTT Protocol ---
    protected static final String TAG = "MQTTExample";
    public static MQTTService mqtt;
    boolean isBound;
    private Controller mController; // for MainActivity
    // --- Necessary ---
    public static Context mContext;
    public static Prefer mConfig;
    public static Android_Element mAe;
    //  --- Fragment ---

    public static tunffli_sw_rly t_sw_rly;
    public static tunffli_music t_music;
    public static tunffli_webcam t_webcam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mConfig = new Prefer(mContext);
//        mConfig.set_link_flag(false);
        mAe = new Android_Element();
        if (savedInstanceState == null) {
            tunffli_sw_rly fragment = new tunffli_sw_rly();//first fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, fragment)
                    .commit();
        }
        initNav();
    }
    DrawerLayout drawerLayout;Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    public boolean onBack_flag = false;
    private void initNav(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//      toolbar.setOnMenuItemClickListener(onMenuItemClick);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onBack_flag){
                    Back_Page();
                    onBackPressed();
                }else{
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Fragment fragment = null;
                switch (id) {
                    case R.id.nav_item1:
                        fragment = new tunffli_sw_rly();
                        break;
                    case R.id.nav_item2:
                        fragment = new tunffli_music();
                        break;
                    case R.id.nav_item3:
                        fragment = new tunffli_webcam();
                        break;
                    case R.id.nav_item4:
                        dialog_host();
                        break;
                    case R.id.nav_item5:
                        jump_Fragment(5);
                        break;
                }
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content, fragment);
//                ft.commitAllowingStateLoss();
                if(id == R.id.nav_item1 || id == R.id.nav_item2 || id == R.id.nav_item3){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, fragment)
//                        .addToBackStack(null)
                            .commit();
                }
                //menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
    @Override
    public void jump_Fragment(int ctrl){// 1.teach 2. About
        Fragment fragment = null;
        switch(ctrl){ // 0 - jay_layout
            case 0:
//                fragment  = new jay_layout();
                break;
            case 4:
//                fragment = new jay_course();
                break;
            case 5:
                fragment = new jay_about();
                break;
        }
        onBack_flag = true;
        getSupportActionBar().setDisplayHomeAsUpEnabled(onBack_flag);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.content, fragment);
//        ft.addToBackStack(null);
//        ft.commitAllowingStateLoss();
    }
    @Override
    public void Back_Page(){
        if(onBack_flag){
            onBack_flag = false;
            getSupportActionBar().setDisplayHomeAsUpEnabled(onBack_flag);
            toggle.syncState();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }
    // --- Activity Life Cycle ---
    /*connect("192.168.24.113","1883");
        String [] item_Text = MainActivity.mAe.getDataURL("/sdcard/Jetec/JetTag/Topic-JSON/");
        for(int i=0;i<item_Text.length;i++){
            item_Text[i].replace('-','/');
            subscribe(item_Text[i]+"/config");
            subscribe(item_Text[i]+"/data");
        }*/
    @Override
    protected void onResume() {
        super.onResume();
        //this.connect("192.168.24.113","1883");
    }
    @Override
    protected void onStart() {
        super.onStart();
//            connect(mConfig.get_host_ip(),mConfig.get_host_port());
        Intent service = new Intent(MainActivity.this, MQTTService.class);
        bindService(service, connection, Context.BIND_AUTO_CREATE);

    }
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }
    //unbindService(connection);
    protected void onDestroy(){
        super.onDestroy();
    }
    // --- MQTT Protocol ---
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mqtt = ((MQTTService.MQTTBinder) binder).getService();
            isBound = true;

            mqtt.setHandler(mHandler);

            if(mConfig.get_link_flag()){
                // // Default host is test.mosquitto.org (you should change this!)
                mqtt.setHost(mConfig.get_host_ip());
                //
                // // Default mqtt port is 1883
                mqtt.setPort(Integer.parseInt(mConfig.get_host_port()));
                //
                // // Set a unique id for this client-broker combination
                mqtt.setId(Build.SERIAL);
                Log.i("QQ123",Build.SERIAL+"");
                //
                // // Open the connection to the MQTT server
                mqtt.connect();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    String [] item_Text;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_CHANGE:
                    switch (msg.arg1) {
                        case STATE_NONE:
                            Toast.makeText(MainActivity.this, "Disconnected",
                                    Toast.LENGTH_SHORT).show();
                            if(alert_flag){
                                btn_link.setImageResource(R.drawable.ctrl_link_up);
//                                mConfig.set_link_flag(false);
                            }
                            break;
                        case STATE_CONNECTING:
                            Toast.makeText(MainActivity.this, "Waiting...",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case STATE_CONNECTED:
                            Toast.makeText(MainActivity.this, "Connected!",
                                    Toast.LENGTH_SHORT).show();
                            mConfig.set_link_flag(true);
                            if(alert_flag){
                                btn_link.setImageResource(R.drawable.ctrl_unlink_up);
                                alert.cancel();
                            }
                            subscribe("enoxs");
                            break;
                        case STATE_CONNECTION_FAILED:
                            Toast.makeText(MainActivity.this, "Failed",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;

                case PUBLISH:
                    MQTTMessage message = (MQTTMessage) msg.obj;
                    String topic = (String) message.variableHeader
                            .remove("topic_name");
                    byte[] payload = message.payload;
                    String text = new String(payload);

                    Log.i("QQ123", topic + " - " + text);
                    // --- json ---
                    process_json(topic,text);
                    break;
                case MQTT_RAW_READ:
                    // byte[] buf = (byte[]) msg.obj;
                    // String s = new String(buf);
                    // Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT);
                    break;
            }
        }

    };
    @Override
    public void connect(String host, String port) {
// Default host is test.mosquitto.org (you should change this!)
        mqtt.setHost(host);
// Default mqtt port is 1883
//        mqtt.setPort(1883);
        int set_port =1883;
        try{
            set_port =  Integer.parseInt(port);
        }catch(Exception e){
            set_port = 1883;
        }
        mqtt.setPort(set_port);

// Set a unique id for this client-broker combination
        mqtt.setId("enoxs");

// Open the connection to the MQTT server
        mqtt.connect();
    }

    public void unlink(){
        mqtt.disconnect();
        mConfig.set_link_flag(false);
    }
    String publish_topic;
    public void publish(String topic, String message) {
        publish_topic = topic;
        mqtt.publish(publish_topic, message);
    }

    public void subscribe(String topic) {
        mqtt.subscribe(topic, AT_MOST_ONCE);
    }
    boolean val_err_flag = false;
    private void process_json(String topic,String text){
        try {
            JSONObject obj = new JSONObject(text);
            if(obj.getBoolean("reply")){
                switch(obj.getString("id")){
                    case "Madplay":
                        if(t_music != null){
                            if(obj.getString("action").equals("media_flag")){
                                t_music.reback(obj.getString("action"),obj.getBoolean("value"));
                            }
                            if(obj.getString("action").equals("fold_file")){
                                t_music.reback(obj);
                            }
                        }
                        break;
                    case "Mjpg":
                        if(t_webcam != null)
                            if(obj.getBoolean("value")){
                                t_webcam.webcam_flag = true;
                                t_webcam.btn_webcam.setText("STOP");
                            }else{
                                t_webcam.webcam_flag = false;
                                t_webcam.btn_webcam.setText("START");
                            }
                        break;
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public AlertDialog alert;
    public boolean alert_flag = false;
    public ImageButton btn_link;
    public void dialog_host() {//自訂義布局
        LayoutInflater adbInflater = LayoutInflater.from(this);
        alert = new AlertDialog.Builder(this).create();
        View alertView = adbInflater.inflate(R.layout.item_host, null);
        final EditText edt_host = (EditText) alertView.findViewById(R.id.edt_host);
        final EditText edt_port = (EditText) alertView.findViewById(R.id.edt_port);
        final EditText edt_username = (EditText) alertView.findViewById(R.id.edt_username);
        final EditText edt_password = (EditText) alertView.findViewById(R.id.edt_password);
        edt_host.setText(MainActivity.mConfig.get_host_ip());
        edt_port.setText(MainActivity.mConfig.get_host_port());
        edt_username.setText(MainActivity.mConfig.get_username());
        edt_password.setText(MainActivity.mConfig.get_password());
        alert_flag = true;
        btn_link = (ImageButton) alertView.findViewById(R.id.btn_link);
        if(MainActivity.mConfig.get_link_flag()){
            btn_link.setImageResource(R.drawable.ctrl_unlink_up);
        }else{
            btn_link.setImageResource(R.drawable.ctrl_link_up);
        }
        btn_link.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(MainActivity.mConfig.get_link_flag()){
                        btn_link.setImageResource(R.drawable.ctrl_unlink_down);
                    }else{
                        btn_link.setImageResource(R.drawable.ctrl_link_down);
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    if(MainActivity.mConfig.get_link_flag()){
                        btn_link.setImageResource(R.drawable.ctrl_unlink_up);
                    }else{
                        btn_link.setImageResource(R.drawable.ctrl_link_up);
                    }
                }
                return false;
            }
        });
        btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect(edt_host.getText().toString(), edt_port.getText().toString());
                if(mConfig.get_link_flag()){
                    unlink();
                }else{
                    mConfig.set_host_ip(edt_host.getText()+"");
                    mConfig.set_host_port(edt_port.getText()+"");
                    mConfig.set_username(edt_username.getText()+"");
                    mConfig.set_password(edt_password.getText()+"");
                }
            }
        });
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                alert_flag =false;
            }
        });
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alert.setView(alertView);
        alert.setCancelable(true);
        alert.show();
    }//主機資訊

    // --- 頻道控制 ---
    public static ArrayList[] topic_info;
    public void init_topic_info(){
        item_Text = MainActivity.mAe.getDataURL("/sdcard/Jetec/JetTag/Topic-JSON/");
        if(item_Text.length > 0){
            topic_info = new ArrayList[item_Text.length];
            String [] jsonString = new String [item_Text.length];
            for(int i=0;i<item_Text.length;i++){
                topic_info[i] = new ArrayList();
                jsonString[i] = MainActivity.mAe.readJsonFile("/sdcard/Jetec/JetTag/Topic-JSON/"+item_Text[i]);
                try {
                    JSONObject jsonResponse = new JSONObject(jsonString[i]);
                    JSONArray dev_id = jsonResponse.getJSONArray("dev_id");
                    JSONArray dev_type = jsonResponse.getJSONArray("dev_type");
                    for (int j = 0; j < dev_id.length(); j++) {
                        JSONObject json_obj = new JSONObject();
                        if(dev_type.getString(j).equals("temp/rh")){
                            try {
                                json_obj.put("type","temp/rh");
                                json_obj.put("temp",-1);
                                json_obj.put("rh",-1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                json_obj.put("type","data");
                                json_obj.put("data",-1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        topic_info[i].add(json_obj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // -1 = No Position; Set Position;
            for(int i=mConfig.get_Display_total()-1;i>-1;i--){
                for(int j=0;j<item_Text.length;j++){
                    if(mConfig.get_Device_Topic(i).equals(item_Text[j])){
                        try {
                            JSONObject jsonResponse = new JSONObject(jsonString[j]);
                            JSONArray dev_id = jsonResponse.getJSONArray("dev_id");
                            JSONArray dev_type = jsonResponse.getJSONArray("dev_type");
                            for(int k=0;k<dev_id.length();k++){
                                if(mConfig.get_Device_Id(i).equals(dev_id.getString(k))){
                                    JSONObject json_obj = new JSONObject(topic_info[j].get(k)+"");
                                    Log.i("QQ-JSON:","dev_type:"+dev_type.getString(j));
                                    if(dev_type.getString(k).equals("temp/rh")){
                                        if(mConfig.get_Device_Type(i).equals("temp")){
                                            json_obj.put("temp",i);
                                        }
                                        if(mConfig.get_Device_Type(i).equals("rh")){
                                            json_obj.put("rh",i);
                                        }
                                    }else{
                                        json_obj.put("data",i);
                                    }
                                    topic_info[j].set(k,json_obj);
                                    k = dev_id.length();
                                    j = item_Text.length;

                                    Log.i("QQ-JSON:","k:"+k+",j:"+j+",i:"+i);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // --- Demo Log ---
            for(int i=0;i<topic_info[0].size();i++){
                Log.i("QQ-JSON:",topic_info[0].get(i)+"");
            }
        }
    }
}
