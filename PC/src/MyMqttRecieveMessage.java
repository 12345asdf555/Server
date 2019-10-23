import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyMqttRecieveMessage {
	private static int QoS = 1;
	private static String Host = "tcp://device1-api.10646.cn:1883";
	private static MemoryPersistence memoryPersistence = null;
	private static MqttConnectOptions mqttConnectOptions = null;
	private static MqttClient mqttClient  = null;
	
	public static void init(String clientId) {
		mqttConnectOptions = new MqttConnectOptions();
		memoryPersistence = new MemoryPersistence();
		if(null != memoryPersistence && null != clientId && null != Host) {
			try {
				 mqttClient = new MqttClient(Host, clientId, memoryPersistence);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//ilogger.logerr("memoryPersistence clientId Host 有空值");
		}
		
		if(null != mqttConnectOptions) {
			mqttConnectOptions.setUserName("815137651@qq.com");
			mqttConnectOptions.setPassword("shgwth4916.".toCharArray());
			mqttConnectOptions.setCleanSession(true);
			mqttConnectOptions.setConnectionTimeout(30);
			mqttConnectOptions.setKeepAliveInterval(45);
			if(null != mqttClient && !mqttClient.isConnected()) {
				mqttClient.setCallback(new MqttReceriveCallback());
				try {
					mqttClient.connect();
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}else {
				//ilogger.logerr("mqttClient is error");
			}
		}else {
			//ilogger.logerr("mqttConnectOptions is null");
		}
	}
	
	
    public static void recieve(String topic) {
        int[] Qos = {QoS};
        String[] topics = {topic};
        //ilogger ilogger = new ilogger("MyMqttRecieveMessage", "subTopic");
        if(null != mqttClient && mqttClient.isConnected()) {
        	if(null!=topics && null!=Qos && topics.length>0 && Qos.length>0) {
        		try {
        			mqttClient.subscribe(topics, Qos);
        		} catch (MqttException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        	}else {
        		//ilogger.logerr("there is error");
        	}
        }else {
        	init("123444");
        	recieve(topic);
        }
    }
}
