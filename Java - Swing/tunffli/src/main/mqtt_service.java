package main;
import javax.swing.JFrame;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import frame.setting;

public class mqtt_service {
	JFrame main,sw_rly,music,webcam,setting;
	public mqtt_service(JFrame inx,JFrame sw,JFrame muc,JFrame cam,JFrame set){
		main = inx;
		sw_rly = sw;
		music = muc;
		webcam = cam;
		setting = set;
	}
	MqttClient sampleClient;
	String topic = "enoxs";
	int qos = 2;
	String clientId = "Enoxs_PC";
	public boolean link_flag = false;
	public boolean connect(String host,String port,String username,String password){
		String broker = "tcp://"+host+":"+port;
		//String broker = "tcp://192.168.1.7:1883";
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			sampleClient = new MqttClient(broker, clientId,
					persistence);
			sampleClient.setCallback(new MqttCallback() {

				@Override
				public void messageArrived(String topic, MqttMessage msg)
						throws Exception {
					try{
						JSONObject obj = new JSONObject(new String(msg.getPayload()));
						if(obj.getBoolean("reply")){
							switch(obj.getString("id")){
							case "Madplay":
								if(obj.getString("action").equals("media_flag")){
									((frame.music_madplay) music).reback(obj.getString("action"),obj.getBoolean("value"));
								}
								if(obj.getString("action").equals("fold_file")){
									((frame.music_madplay) music).reback(obj);
								}
								break;
							case "Mjpg":
								((frame.webcam_streamer) webcam).reback(obj.getBoolean("value"));
								break;
							}
						}
					}catch(Exception e){
						System.out.println("Not JSON Message");
					}
					
					System.out.println("topic:" + topic);
					System.out.println("msg:" + new String(msg.getPayload()));
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					System.out.println("delivary complete");
				}

				@Override
				public void connectionLost(Throwable cause) {
					cause.printStackTrace();
				}
			});
			MqttConnectOptions options = new MqttConnectOptions();
			options.setUserName("admin");
			options.setPassword(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'});
			sampleClient.connect(options);
			sampleClient.subscribe(topic, qos);
			link_flag = true;
			((setting) setting).reback(link_flag);
		} catch (MqttException me) {
			link_flag = false;
			index.mPrefer.setLink_flag(link_flag);
			((setting) setting).reback(link_flag);
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
		return link_flag;
	}
	public void publish_message(String content){
		MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        try {
			sampleClient.publish(topic, message);
		} catch (MqttPersistenceException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
	}
	public void disconnect(){
		try {
			sampleClient.disconnect();
			link_flag = false;
			((setting) setting).reback(link_flag);
			index.mPrefer.setLink_flag(link_flag);
		} catch (MqttException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
	}
}
