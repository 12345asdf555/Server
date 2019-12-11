import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import io.netty.channel.socket.SocketChannel;

public class MyMqttClient {
	public static MqttClient mqttClient = null;
	private static MemoryPersistence memoryPersistence = null;
	private static MqttConnectOptions mqttConnectOptions = null;
	private String socketfail;
	public HashMap<String, SocketChannel> socketlist = new HashMap<>();

	/*
	 * static { init("MQTT_FX_Client"); }
	 */

	public void init(String clientId) {
		//��ʼ���������ö���
		mqttConnectOptions = new MqttConnectOptions();
		//��ʼ��MqttClient
		if(null != mqttConnectOptions) {
			/*
			 * mqttConnectOptions.setUserName("815137651@qq.com");
			 * mqttConnectOptions.setPassword("shgwth4916.".toCharArray());
			 */
			//			true���԰�ȫ��ʹ���ڴ�־�����Ϊ�ͻ��˶Ͽ�����ʱ���������״̬
			mqttConnectOptions.setCleanSession(true);
			//			�������ӳ�ʱ������
			mqttConnectOptions.setConnectionTimeout(3000);
			mqttConnectOptions.setKeepAliveInterval(3000);
			//			���ó־û���ʽ
			memoryPersistence = new MemoryPersistence();
			if(null != memoryPersistence && null != clientId) {
				try {
					mqttClient = new MqttClient("tcp://119.3.100.103:1883", clientId,memoryPersistence);
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {

			}
		}else {
			System.out.println("mqttConnectOptions����Ϊ��");
		}

		//�������Ӻͻص�
		if(null != mqttClient) {
			if(!mqttClient.isConnected()) {

				//			�����ص���������
				MqttReceriveCallback mqttReceriveCallback = new MqttReceriveCallback(this);
				//			�ͻ�����ӻص�����
				mqttClient.setCallback(mqttReceriveCallback);
				//			��������
				try {
					mqttClient.connect(mqttConnectOptions);
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}else {
			System.out.println("mqttClientΪ��");
		}
		System.out.println(mqttClient.isConnected());
	}

	//	�ر�����
	public void closeConnect() {
		//�رմ洢��ʽ
		if(null != memoryPersistence) {
			try {
				memoryPersistence.close();
			} catch (MqttPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("memoryPersistence is null");
		}

		//		�ر�����
		if(null != mqttClient) {
			if(mqttClient.isConnected()) {
				try {
					mqttClient.disconnect();
					mqttClient.close();
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				System.out.println("mqttClient is not connect");
			}
		}else {
			System.out.println("mqttClient is null");
		}
	}

	//	������Ϣ
	public void publishMessage(String pubTopic,String message,int qos) {
		if(null != mqttClient&& mqttClient.isConnected()) {
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setQos(qos);
			mqttMessage.setPayload(message.getBytes());

			MqttTopic topic = mqttClient.getTopic(pubTopic);

			if(null != topic) {
				try {
					MqttDeliveryToken publish = topic.publish(mqttMessage);
					if(!publish.isComplete()) {
						//System.out.println("��Ϣ�����ɹ�:"+message);
					}
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}else {
			reConnect();
		}

	}
	//	��������
	public void reConnect() {
		if(null != mqttClient) {
			if(!mqttClient.isConnected()) {
				if(null != mqttConnectOptions) {
					try {
						mqttClient.connect(mqttConnectOptions);
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					System.out.println("mqttConnectOptions is null");
				}
			}else {
				System.out.println("mqttClient is null or connect");
			}
		}else {
			init("123");
		}

	}
	//	��������
	public void subTopic(String topic) {
		if(null != mqttClient&& mqttClient.isConnected()) {
			try {
				mqttClient.subscribe(topic, 0);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("mqttClient is error");
		}
	}

	//	�������
	public void cleanTopic(String topic) {
		if(null != mqttClient&& !mqttClient.isConnected()) {
			try {
				mqttClient.unsubscribe(topic);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("mqttClient is error");
		}
	}
	
	//  �����ϴ��·�����
	public void webdata(String str) {
		
		ArrayList<String> listarraybuf = new ArrayList<String>();
		boolean ifdo = false;
		HashMap<String, SocketChannel> socketlist_cl;
		Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
		while(webiter.hasNext())
		{
			try{
				Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
				socketfail = entry.getKey();
				SocketChannel socketcon = entry.getValue();
				if(socketcon.isOpen() && socketcon.isActive() && socketcon.isWritable()) {
					socketcon.writeAndFlush(str);
				}else {
					listarraybuf.add(socketfail);
					ifdo = true;
				}

			}catch (Exception e) {
				listarraybuf.add(socketfail);
				ifdo = true;
				e.getStackTrace();
			}
		}
		if(ifdo){
			//socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
			for(int i=0;i<listarraybuf.size();i++){
				socketlist.remove(listarraybuf.get(i));
			}
		}
		
		/*
		 * ArrayList<String> listarraybuf = new ArrayList<String>(); boolean ifdo =
		 * false; HashMap<String, SocketChannel> socketlist_cl; synchronized
		 * (socketlist){ socketlist_cl = (HashMap<String, SocketChannel>)
		 * socketlist.clone(); } Iterator<Entry<String, SocketChannel>> webiter =
		 * socketlist_cl.entrySet().iterator(); while(webiter.hasNext()) { try{
		 * Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>)
		 * webiter.next(); socketfail = entry.getKey(); SocketChannel socketcon =
		 * entry.getValue(); socketcon.writeAndFlush(str);
		 * 
		 * }catch (Exception e) { listarraybuf.add(socketfail); ifdo = true;
		 * e.getStackTrace(); } } if(ifdo){ synchronized (socketlist){ //socketlist_cl =
		 * (HashMap<String, SocketChannel>) socketlist.clone(); for(int
		 * i=0;i<listarraybuf.size();i++){ socketlist.remove(listarraybuf.get(i)); } } }
		 */
		
	}
}