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
		//初始化连接设置对象
		mqttConnectOptions = new MqttConnectOptions();
		//初始化MqttClient
		if(null != mqttConnectOptions) {
			/*
			 * mqttConnectOptions.setUserName("815137651@qq.com");
			 * mqttConnectOptions.setPassword("shgwth4916.".toCharArray());
			 */
			//			true可以安全地使用内存持久性作为客户端断开连接时清除的所有状态
			mqttConnectOptions.setCleanSession(true);
			//			设置连接超时、心跳
			mqttConnectOptions.setConnectionTimeout(3000);
			mqttConnectOptions.setKeepAliveInterval(3000);
			//			设置持久化方式
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
			System.out.println("mqttConnectOptions对象为空");
		}

		//设置连接和回调
		if(null != mqttClient) {
			if(!mqttClient.isConnected()) {

				//			创建回调函数对象
				MqttReceriveCallback mqttReceriveCallback = new MqttReceriveCallback(this);
				//			客户端添加回调函数
				mqttClient.setCallback(mqttReceriveCallback);
				//			创建连接
				try {
					mqttClient.connect(mqttConnectOptions);
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}else {
			System.out.println("mqttClient为空");
		}
		System.out.println(mqttClient.isConnected());
	}

	//	关闭连接
	public void closeConnect() {
		//关闭存储方式
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

		//		关闭连接
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

	//	发布消息
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
						//System.out.println("消息发布成功:"+message);
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
	//	重新连接
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
	//	订阅主题
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

	//	清空主题
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
	
	//  处理上传下发数据
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