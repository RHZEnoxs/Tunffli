package backup;

import org.json.JSONObject;

public class dt_json {

	public static void main(String[] args) {
		
		JSONObject obj = new JSONObject();
        obj.put("id", "Relay");
        obj.put("reply",false);
        obj.put("dev_id","0x01");
        obj.put("value",true);
		
        System.out.println("Encoding Examples : \n" + obj);
		
        System.out.println("id : " + obj.getString("id"));
		
		/*JSONObject j;
		try {
		      String tmp = "{\"Data\":{\"Name\":\"MichaelChan\",\"Email\":\"XXXX@XXX.com\",\"Phone\":[1234567,0911123456]}}";

		      j = new JSONObject(tmp);

		      Object jsonOb = j.getJSONObject("Data").getJSONArray("Phone").get(1);

		      System.out.println(jsonOb);

		    }catch(Exception e){
		      System.err.println("Error: " + e.getMessage());
		  }*/
		
		
		
		
		}
}
