package backup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

public class dt_array {

	public static void main(String[] args) {
		dt_array dt = new dt_array();
		// TODO 自動產生的方法 Stub
		String[] playlist  = {"Default","Girls' Generation"};
		String[] musiclist = {"Oh !!!","Mr.taxi"};
		String[] filefold  = {"Desktop", "Enoxs", "RHZ"};
		ArrayList filefile = new ArrayList();
		/*for(int i=0;i<filefold.length;i++){
			filefile.add(new ArrayList());
		}*/
		JSONObject obj = new JSONObject(dt.LoadData());
		
		//System.out.println(obj.get("fold_list"));
		System.out.println(obj.getJSONArray("file_list").getJSONArray(0).get(0));
		
		dt.SaveData("Hello Again");
		
	}
	public String LoadData(){
        FileReader fr;
        StringBuilder StrShow = new StringBuilder();;
        try {
            fr = new FileReader("D:/Desktop_Workspace/Java_WorkSpace/tunffli/src/asset/array_json.json");
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
		String path="config/";
	      File file1=new File(path);
	      if(!file1.exists())
	      {
	        file1.mkdirs();
	      }
		 try{
		        FileWriter fw = new FileWriter(path + title, false);
		        BufferedWriter bw = new BufferedWriter(fw); 
		        bw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
		        bw.write(content);
		        bw.newLine();
		        bw.close();
		    }catch(IOException e){
		       e.printStackTrace();
		    }
	}

}
