import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.DriverManager;
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
public class NettyServerHandler extends ChannelHandlerAdapter{
	
	public String ip;
    public String ip1;
    public String connet;
    public Thread workThread;
    public java.sql.Statement stmt =null;
    public SocketChannel socketchannel = null;
    public ArrayList<String> dbdata = new ArrayList<String>();
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public ArrayList<String> listarray4 = new ArrayList<String>();
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    public HashMap<String, SocketChannel> websocketlist = new HashMap<>();
    public Mysql mysql = new Mysql();
    public Android android = new Android();
    public Websocket websocket = new Websocket();
	public byte[] b;
    public int a=0;
    
	@Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		 
		String str = "";
		try{
			str = (String) msg;
			Workspace ws = new Workspace(str);
	        workThread = new Thread(ws);  
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

		public Workspace(String str) {
			// TODO Auto-generated constructor stub
			
			this.str=str;
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
		
			if(str.substring(0,2).equals("7E") && (str.substring(10,12).equals("22")) && str.length()==290){
				synchronized (listarray4) {
					for(int i=0;i<listarray4.size();i+=14){
						listarray4.set(i+2, "0");
	                    listarray4.set(i+3, "0");
	                    listarray4.set(i+4, "0");
	                    listarray4.set(i+5, "0");
	                    listarray4.set(i+6, "0");
	                    listarray4.set(i+7, "0");
	                    listarray4.set(i+8, "0");
	                    listarray4.set(i+9, "0");
	                    listarray4.set(i+10, "0");
	                    listarray4.set(i+11, "0");
	                    listarray4.set(i+12, "0");
	                    listarray4.set(i+13, "0");
					}
					
					long gatherid = Integer.valueOf(str.substring(16, 20));
					for(int i=0;i<listarray4.size();i+=14){
						if(gatherid == Integer.valueOf(listarray4.get(i))){
							String electricity = Integer.toString(Integer.valueOf(str.subSequence(56, 60).toString(),16));
		                    String voltage = Integer.toString(Integer.valueOf(str.subSequence(60, 64).toString(),16)/10);
		                    String electricity1 = Integer.toString(Integer.valueOf(str.subSequence(56+80, 60+80).toString(),16));
		                    String voltage1 = Integer.toString(Integer.valueOf(str.subSequence(60+80, 64+80).toString(),16)/10);
		                    String electricity2 = Integer.toString(Integer.valueOf(str.subSequence(56+80+80, 60+80+80).toString(),16));
		                    String voltage2 = Integer.toString(Integer.valueOf(str.subSequence(60+80+80, 64+80+80).toString(),16)/10);
		                    
		                    String year = Integer.valueOf(str.subSequence(44, 46).toString(),16).toString();
		      	    		String month = Integer.valueOf(str.subSequence(46, 48).toString(),16).toString();
		      	    		String day = Integer.valueOf(str.subSequence(48, 50).toString(),16).toString();
		      	    		String hour = Integer.valueOf(str.subSequence(50, 52).toString(),16).toString();
		      	    		String minute = Integer.valueOf(str.subSequence(52, 54).toString(),16).toString();
		      	    		String second = Integer.valueOf(str.subSequence(54, 56).toString(),16).toString();
		      	    		String strdate = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
		      	    		String year1 = Integer.valueOf(str.subSequence(44+80, 46+80).toString(),16).toString();
		      	    		String month1 = Integer.valueOf(str.subSequence(46+80, 48+80).toString(),16).toString();
		      	    		String day1 = Integer.valueOf(str.subSequence(48+80, 50+80).toString(),16).toString();
		      	    		String hour1 = Integer.valueOf(str.subSequence(50+80, 52+80).toString(),16).toString();
		      	    		String minute1 = Integer.valueOf(str.subSequence(52+80, 54+80).toString(),16).toString();
		      	    		String second1 = Integer.valueOf(str.subSequence(54+80, 56+80).toString(),16).toString();
		      	    		String strdate1 = year1+"-"+month1+"-"+day1+" "+hour1+":"+minute1+":"+second1;
		      	    		String year2 = Integer.valueOf(str.subSequence(44+80+80, 46+80+80).toString(),16).toString();
		      	    		String month2 = Integer.valueOf(str.subSequence(46+80+80, 48+80+80).toString(),16).toString();
		      	    		String day2 = Integer.valueOf(str.subSequence(48+80+80, 50+80+80).toString(),16).toString();
		      	    		String hour2 = Integer.valueOf(str.subSequence(50+80+80, 52+80+80).toString(),16).toString();
		      	    		String minute2 = Integer.valueOf(str.subSequence(52+80+80, 54+80+80).toString(),16).toString();
		      	    		String second2 = Integer.valueOf(str.subSequence(54+80+80, 56+80+80).toString(),16).toString();
		      	    		String strdate2 = year2+"-"+month2+"-"+day2+" "+hour2+":"+minute2+":"+second2;
		      	    		
		      	    		String status = Integer.toString(Integer.parseInt(str.subSequence(84, 86).toString(),16));
		      	    		String status1 = Integer.toString(Integer.parseInt(str.subSequence(84+80, 86+80).toString(),16));
		      	    		String status2 = Integer.toString(Integer.parseInt(str.subSequence(84+80+80, 86+80+80).toString(),16));
							
		                    listarray4.set(i+2, electricity);
		                    listarray4.set(i+3, voltage);
		                    listarray4.set(i+4, strdate);
		                    listarray4.set(i+5, status);
		                    listarray4.set(i+6, electricity1);
		                    listarray4.set(i+7, voltage1);
		                    listarray4.set(i+8, strdate1);
		                    listarray4.set(i+9, status1);
		                    listarray4.set(i+10, electricity2);
		                    listarray4.set(i+11, voltage2);
		                    listarray4.set(i+12, strdate2);
		                    listarray4.set(i+13, status2);
						}
					}
				}
				synchronized (websocketlist) {
					mysql.Mysqlbase(str);
			        websocket.Websocketbase(str,listarray2,listarray3,websocketlist);
			        if(socketchannel!=null){
				        try {
							socketchannel.writeAndFlush(str).sync();
						} catch (Exception e) {
							socketchannel = null;
							e.printStackTrace();
						}
			        }
				}
				
			}else if(str.substring(0,2).equals("FA")){  //处理实时数据
				
				synchronized (websocketlist) {
				mysql.Mysqlrun(str);
		        websocket.Websocketrun(str,listarray2,listarray3,websocketlist);
		        if(socketchannel!=null){
			        try {
						socketchannel.writeAndFlush(str).sync();
					} catch (Exception e) {
						socketchannel = null;
						e.printStackTrace();
					}
		        }
				}
		        
	        }else if(str.substring(0,2).equals("þ")){   //处理android数据
	        	
	        	android.Androidrun(str);
	        	
	        }else if(str.substring(0,2).equals("JN")){  //江南任务派发 任务号、焊工、焊机、状态
	        	
	        	synchronized (socketlist) {
	        	ArrayList<String> listarraybuf = new ArrayList<String>();
	        	boolean ifdo = false;
	        	
	        	Iterator<Entry<String, SocketChannel>> iter = socketlist.entrySet().iterator();
                while(iter.hasNext()){
                	try{
                    	Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) iter.next();
                    	
                    	System.out.println(entry);
                    	
                    	socketfail = entry.getKey();

        				SocketChannel socketcon = entry.getValue();
                    	socketcon.writeAndFlush(str).sync();
                    	
                	}catch (Exception e) {
                		e.printStackTrace();
                		listarraybuf.add(socketfail);
                		ifdo = true;
   					 }
                }
	        	
                if(ifdo){
                	for(int i=0;i<listarraybuf.size();i++){
                    	socketlist.remove(listarraybuf.get(i));
                	}
                }
	        	}
                
	        } else if(str.equals("SS")){  //webservice获取实时数据
	        	
	        	synchronized (listarray4) {
	        	synchronized (socketlist) {
	        	ArrayList<String> listarraybuf = new ArrayList<String>();
	        	boolean ifdo = false;
	        	
	        	Iterator<Entry<String, SocketChannel>> iter = socketlist.entrySet().iterator();
                while(iter.hasNext()){
                	try{
                    	Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) iter.next();
                    	
                    	socketfail = entry.getKey();

        				SocketChannel socketcon = entry.getValue();
        				
        				JSONArray ja = new JSONArray();
        				JSONObject jo = new JSONObject();
        				for(int i=0;i<listarray4.size();i+=14){
        					if(listarray4.get(i+1) == null){
            					jo.put("num", "0000");
        					}else if(listarray4.get(i+1) != null){
            					jo.put("num", listarray4.get(i+1));
        					}
        					jo.put("ele1", listarray4.get(i+2));
        					jo.put("vol1", listarray4.get(i+3));
        					jo.put("time1", listarray4.get(i+4));
        					jo.put("status1", listarray4.get(i+5));
        					jo.put("ele2", listarray4.get(i+6));
        					jo.put("vol2", listarray4.get(i+7));
        					jo.put("time2", listarray4.get(i+8));
        					jo.put("status2", listarray4.get(i+9));
        					jo.put("ele3", listarray4.get(i+10));
        					jo.put("vol3", listarray4.get(i+11));
        					jo.put("time3", listarray4.get(i+12));
        					jo.put("status3", listarray4.get(i+13));
        					ja.add(jo);
        				}
        				
        				String data = ja.toString();
        				
                    	socketcon.writeAndFlush(data).sync();
                    	
                	}catch (Exception e) {
                		listarraybuf.add(socketfail);
                		ifdo = true;
   					 }
                }
	        	
                if(ifdo){
                	for(int i=0;i<listarraybuf.size();i++){
                    	socketlist.remove(listarraybuf.get(i));
                	}
                }
	        	}
	        	}
                
	        } else{    //处理焊机下发和上传
	        	
	        	//System.out.println(str);
	        	
	        	synchronized (websocketlist) {
	        	ArrayList<String> listarraybuf = new ArrayList<String>();
	        	boolean ifdo = false;
	        	
	        	Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
                while(webiter.hasNext()){
                	try{
                    	Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                    	websocketfail = entry.getKey();
                    	SocketChannel websocketcon = entry.getValue();
                    	websocketcon.writeAndFlush(new TextWebSocketFrame(str)).sync();
                	}catch (Exception e) {
                		
                		listarraybuf.add(websocketfail);
                		ifdo = true;
   					 }
                }
                
                if(ifdo){
                	for(int i=0;i<listarraybuf.size();i++){
                		websocketlist.remove(listarraybuf.get(i));
                	}
                }
	        	}
	        }
		}
	 }
	 
	 
	 @Override  
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception { 
	     ctx.flush();  
	 } 
     @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
         ctx.close();  
     } 
	 
}
