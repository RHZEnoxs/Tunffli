package backup;
import javax.swing.JFrame;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
public class mqtt_sample {
	JFrame act_jf;
	int f_id;
	public mqtt_sample(int id,JFrame it){
		f_id = id;
		act_jf = it;
	}
	MqttClient sampleClient;
	String topic = "enoxs";
	int qos = 2;
	String broker = "tcp://192.168.1.7:1883";
	String clientId = "SubscribeSample";
	public void connect(){
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			sampleClient = new MqttClient(broker, clientId,
					persistence);
			sampleClient.setCallback(new MqttCallback() {

				@Override
				public void messageArrived(String topic, MqttMessage msg)
						throws Exception {
					((mqtt_view) act_jf).reback(f_id,topic,new String(msg.getPayload()));
					
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

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}
	public void publish_message(){
		String content      = "Message from MqttPublishSample";
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
	
}
