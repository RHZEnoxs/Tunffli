package com.enoxs.tunffli;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by enoxs on 2016/8/29.
 */
public class Prefer {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public Prefer(Context context){
        this.context=context;
        preferences =context.getSharedPreferences("jet_tag", Context.MODE_PRIVATE);
        editor =preferences.edit();
    }
    //  --- Set ---
    public void set_init_flag(boolean init){
        editor.putBoolean("init_flag", init).commit();
    }
    public void set_host_ip(String ip){
        editor.putString("host_ip", ip).commit();
    }
    public void set_host_port(String port){
        editor.putString("host_port", port).commit();
    }
    public void set_username(String user){
        editor.putString("username", user).commit();
    }
    public void set_password(String pwd){
        editor.putString("password", pwd).commit();
    }
    public void set_link_flag(boolean link){
        editor.putBoolean("link_flag", link).commit();
    }
    public void set_language(int lang){
        editor.putInt("jt_lang", lang).commit();
    }
    public void set_Display_total(int total){
        editor.putInt("display_total",total).commit();
    }
    public void set_Display_col(int col){
        editor.putInt("display_col", col).commit();
    }
    /*private String [][] Dev_info ;
    public void init_Device_Information(){
        Dev_info = new String[15][3];
        for(int i=0;i<15;i++){
            Dev_info[i][0]="Dev_Na_"+i;
            Dev_info[i][1]="Dev_Ty_"+i;
            Dev_info[i][2]="Dev_No_"+i;
        }
    }*/
    public void set_Device_Information(int position,String topic,String name,String type,String id) {
        editor.putString("Dev_Tc_" + position, topic).commit();
        editor.putString("Dev_Na_" + position, name).commit();
        editor.putString("Dev_Ty_" + position, type).commit();
        editor.putString("Dev_No_" + position, id).commit();
    }
    //  --- Get ---
    public boolean get_init_flag(){
        return preferences.getBoolean("init_flag",true);
    }
    public String get_host_ip(){
        return preferences.getString("host_ip", "127.0.0.1");
    }
    public String get_host_port(){
        return preferences.getString("host_port", "1883");
    }
    public String get_username(){
        return preferences.getString("username", "");
    }
    public String get_password(){
        return preferences.getString("password", "");
    }
    public boolean get_link_flag(){
        return preferences.getBoolean("link_flag",false);
    }
    public int get_language(){
        return preferences.getInt("jt_lang", 0);
    }
    public int get_Display_total(){
        return preferences.getInt("display_total", 9);
    }
    public int get_Display_col(){
        return preferences.getInt("display_col", 3);
    }
    public String get_Device_Topic(int position) {
        return preferences.getString("Dev_Tc_"+position, "topic");
    }
    public String get_Device_Name(int position) {
        return preferences.getString("Dev_Na_"+position, "name");
    }
    public String get_Device_Type(int position) {
        return preferences.getString("Dev_Ty_"+position, "type");
    }
    public String get_Device_Id(int position) {
        return preferences.getString("Dev_No_"+position, "no.");
    }



}
