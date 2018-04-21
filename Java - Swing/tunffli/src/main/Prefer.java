package main;

import java.util.prefs.Preferences;

public class Prefer {
	private Preferences prefs;   
	public Prefer(){
		prefs = Preferences.userRoot().node(this.getClass().getName());  
	}
	public void setLink_flag(boolean flag){
		prefs.putBoolean("Link_flag", flag); 
	}
	public void setHost(String host){
		prefs.put("host_ip", host); 
	}
	public void setPort(String port){
		prefs.put("host_port", port); 
	}
	public void setUsername(String user){
		prefs.put("username", user); 
	}
	public void setPassword(String pwd){
		prefs.put("password", pwd); 
	}
	public Boolean getLink_flag(){
		return prefs.getBoolean("Link_flag",false);
	}
	public String getHost(){
		return prefs.get("host_ip","127.0.0.1" );
	}
	public String getPort(){
		return prefs.get("host_port","1883" );
	}
	public String getUsername(){
		return prefs.get("username","" );
	}
	public String getPassword(){
		return prefs.get("password","" );
	}
}
