import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
@Sharable 
public class NettyServerHandlerTest extends ChannelHandlerAdapter{

	public String ip;
	public String ip1;
	public String connet;
	public Thread workThread;
	public java.sql.Statement stmt =null;
	public java.sql.Connection conn = null;
	public SocketChannel socketchannel = null;
	public ArrayList<String> dbdata = new ArrayList<String>();
	public ArrayList<String> listarray1 = new ArrayList<String>();
	public ArrayList<String> listarray2 = new ArrayList<String>();
	public ArrayList<String> listarray3 = new ArrayList<String>();
	public ArrayList<String> listarray4 = new ArrayList<String>();
	public ArrayList<String> listarrayplc = new ArrayList<String>();  //焊工、工位对应
	public HashMap<String, SocketChannel> socketlist = new HashMap<>();
	public HashMap<String, SocketChannel> websocketlist = new HashMap<>();
	public Mysql mysql = new Mysql();
	public Android android = new Android();
	public Websocket websocket = new Websocket();
	public MyMqttClient mqtt;
	public byte[] b;
	public int a=0;

	@Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		String str = "";
		try{
			str = (String) msg;
			Workspace ws = new Workspace(str);
			workThread = new Thread(ws);  
			workThread.setDaemon(true);
			workThread.start();

			ReferenceCountUtil.release(msg);
			ReferenceCountUtil.release(str);
		}catch(Exception e){
			e.printStackTrace();
		}

		ctx.flush();
	}

	public class Workspace implements Runnable{

		private String str="";
		public byte[] req;
		private String socketfail;
		private String websocketfail;
		private String data;

		public Workspace(String str) {
			// TODO Auto-generated constructor stub

			this.str=str;

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			//基本版
			if(str.substring(0,2).equals("7E") && (str.substring(10,12).equals("22")) && str.length()==290){

				websocket.Websocketbase(str,listarray2,listarray3,websocketlist);

			//处理焊机下发和上传
			}else{    

				//去掉离线数据
				if(!(str.substring(0,2).equals("7E") && (str.substring(10,12).equals("23")) && str.length()==290)){
					//MQTT
					mqtt.publishMessage("webdataup", str, 0);
				}
				
				/*
				 * HashMap<String, SocketChannel> socketlist_cl; synchronized (websocketlist){
				 * socketlist_cl = (HashMap<String, SocketChannel>) websocketlist.clone(); }
				 * ArrayList<String> listarraybuf = new ArrayList<String>(); boolean ifdo =
				 * false;
				 * 
				 * Iterator<Entry<String, SocketChannel>> webiter =
				 * socketlist_cl.entrySet().iterator(); while(webiter.hasNext()) { try{
				 * Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>)
				 * webiter.next(); websocketfail = entry.getKey(); SocketChannel websocketcon =
				 * entry.getValue(); if(websocketcon.isActive() && websocketcon.isOpen()){
				 * websocketcon.writeAndFlush(new TextWebSocketFrame(str)).sync(); }else{
				 * listarraybuf.add(websocketfail); }
				 * 
				 * }catch (Exception e) { listarraybuf.add(websocketfail); ifdo = true; } }
				 * 
				 * if(ifdo){ synchronized (websocketlist){ //socketlist_cl = (HashMap<String,
				 * SocketChannel>) socketlist.clone(); for(int i=0;i<listarraybuf.size();i++){
				 * websocketlist.remove(listarraybuf.get(i)); } }
				 * 
				 * }
				 */

				/*
				 * synchronized (websocketlist) { ArrayList<String> listarraybuf = new
				 * ArrayList<String>(); boolean ifdo = false;
				 * 
				 * Iterator<Entry<String, SocketChannel>> webiter =
				 * websocketlist.entrySet().iterator(); while(webiter.hasNext()){ try{
				 * Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>)
				 * webiter.next(); websocketfail = entry.getKey(); SocketChannel websocketcon =
				 * entry.getValue(); websocketcon.writeAndFlush(new
				 * TextWebSocketFrame(str)).sync(); }catch (Exception e) {
				 * 
				 * listarraybuf.add(websocketfail); ifdo = true; } }
				 * 
				 * if(ifdo){ for(int i=0;i<listarraybuf.size();i++){
				 * websocketlist.remove(listarraybuf.get(i)); } } }
				 */
			}
		}
	}

	@Override  
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception { 
		ctx.flush();  
	} 
	@Override  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
		//ctx.close().sync();  
	}

}
