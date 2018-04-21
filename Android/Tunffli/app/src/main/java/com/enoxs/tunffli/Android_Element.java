package com.enoxs.tunffli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Android_Element {
    public String getDateTime(){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyyMMdd_HHmmss
        Date date = new Date();
        String strDate = sdFormat.format(date);
        //System.out.println(strDate);
        return strDate;
    }
    public void SaveData(String title,String content,String filename){//1.csv 2.txt
        String path = "";
        if(filename.equals(".ini")){
            path="/sdcard/Jetec/JetTag/Configuration/";
        }
        else if(filename.equals(".json")){
            path="/sdcard/Jetec/JetTag/Topic-JSON/";
            filename ="";
        }
        else
        {
            path="/sdcard/Jetec/JetTag/Record/";
        }

        File file1=new File(path);
        if(!file1.exists())
        {
            file1.mkdirs();
        }
        try{
            FileWriter fw = new FileWriter(path + title + filename, false);
            BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
            bw.write(content);
            bw.newLine();
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public String LoadData(String address){
        FileReader fr;
        StringBuilder sb = new StringBuilder();
        try {
            fr = new FileReader(address);//"/sdcard/Jetec/JetTag/Configuration/"
            //將BufferedReader與FileReader做連結
            BufferedReader br = new BufferedReader(fr);
            String temp = br.readLine(); //readLine()讀取一整行
            ArrayList list = new ArrayList();
            while (temp!=null ){
                list.add(temp);
                temp=br.readLine();
            }
            for(int i=0;i<list.size();i++){
                sb.append(list.get(i));
            }
        } catch (IOException e) {
            // TODO 自動產生的 catch 區塊
            e.printStackTrace();
        }
        return sb.toString();
    }
    public String readJsonFile(String address){
        FileInputStream fin;
        String returnString = "";
        try {
            fin = new FileInputStream(new File(address));
            byte[] buffer = new byte[fin.available()];
            while (fin.read(buffer) != -1) ;
            returnString = new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public String[] getDataURL(String path ){
        String[] list = {};
        FilenameFilter namefilter =new FilenameFilter(){
            private String[] filter={
                    "txt","csv","ini",""
            };
            @Override
            public boolean accept(File dir, String filename){
                for(int i=0;i<filter.length;i++){
                    if(filename.indexOf(filter[i])!=-1)
                        return true;
                }
                return false;
            }
        };
        try{
            File filePath=new File(path);//"/sdcard/Jetec/MeshTag/Configuration/"
            File[] fileList=filePath.listFiles(namefilter);
            list =new String[fileList.length];
            for(int i=0;i<list.length;i++){
                list[i]=fileList[i].getName();
                // URL[i]=list[i]+"\n";
                // textFileName.append(/*textFileName.getText().toString()*/list[i]+"\n"/*+BR*/);
            }
        }catch(Exception e){}
        return list;
    }
}
