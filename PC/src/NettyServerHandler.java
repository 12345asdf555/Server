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
public class NettyServerHandler extends ChannelHandlerAdapter{
	
	public String ip;
    public String ip1;
    public String connet;
    public Server server;
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
	public AlertEmail alert = new AlertEmail();
    public Websocket websocket = new Websocket();
	public byte[] b;
    public int a=0;
    
	public NettyServerHandler(Server server) {
		// TODO Auto-generated constructor stub
		this.server = server;
	}
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

		        mysql.Mysqlbase(str);
		        alert.Alert(str);
		        
				//存webservice数据
				/*synchronized (listarray4) {
					long gatherid = Integer.valueOf(str.substring(16, 20),16);
					for(int i=0;i<listarray4.size();i+=23){
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
							
		      	    		String setelectricity = Integer.toString(Integer.valueOf(str.subSequence(68, 72).toString(),16));
		                    String setvoltage = Integer.toString(Integer.valueOf(str.subSequence(72, 76).toString(),16)/10);
		                    String setelectricity1 = Integer.toString(Integer.valueOf(str.subSequence(68+80, 72+80).toString(),16));
		                    String setvoltage1 = Integer.toString(Integer.valueOf(str.subSequence(72+80, 76+80).toString(),16)/10);
		                    String setelectricity2 = Integer.toString(Integer.valueOf(str.subSequence(68+80+80, 72+80+80).toString(),16));
		                    String setvoltage2 = Integer.toString(Integer.valueOf(str.subSequence(72+80+80, 76+80+80).toString(),16)/10);
		      	    		
		                    String warn = "0";
		                    if(status.equals("0") || status.equals("3") || status.equals("5") || status.equals("7")){
		                    	warn = "0";
		                    }else{
		                    	warn = Integer.toString(Integer.parseInt(str.subSequence(84, 86).toString(),16));
		                    	status = "0";
		                    }
		                    String warn1 = "0";
		                    if(status1.equals("0") || status1.equals("3") || status1.equals("5") || status1.equals("7")){
		                    	warn1 = "0";
		                    }else{
		                    	warn1 = Integer.toString(Integer.parseInt(str.subSequence(84+80, 86+80).toString(),16));
		                    	status1 = "0";
		                    }
		                    String warn2 = "0";
		                    if(status2.equals("0") || status2.equals("3") || status2.equals("5") || status2.equals("7")){
		                    	warn2 = "0";
		                    }else{
		                    	warn2 = Integer.toString(Integer.parseInt(str.subSequence(84+80+80, 86+80+80).toString(),16));
		                    	status2 = "0";
		                    }
		                    
		                    listarray4.set(i+2, electricity);
		                    listarray4.set(i+3, voltage);
		                    listarray4.set(i+4, strdate);
		                    listarray4.set(i+5, status);
		                    listarray4.set(i+6, setelectricity);
		                    listarray4.set(i+7, setvoltage);
		                    listarray4.set(i+8, warn);
		                    listarray4.set(i+9, electricity1);
		                    listarray4.set(i+10, voltage1);
		                    listarray4.set(i+11, strdate1);
		                    listarray4.set(i+12, status1);
		                    listarray4.set(i+13, setelectricity1);
		                    listarray4.set(i+14, setvoltage1);
		                    listarray4.set(i+15, warn1);
		                    listarray4.set(i+16, electricity2);
		                    listarray4.set(i+17, voltage2);
		                    listarray4.set(i+18, strdate2);
		                    listarray4.set(i+19, status2);
		                    listarray4.set(i+20, setelectricity2);
		                    listarray4.set(i+21, setvoltage2);
		                    listarray4.set(i+22, warn2);
						}
					}
				}*/

				
		    //处理断网续传数据
			}else if(str.substring(0,2).equals("7E") && (str.substring(10,12).equals("23")) && str.length()==290){
				
		        mysql.Mysqlbaseoutline(str,server);
				
		    //欧华纬华
			}else if(str.substring(0,2).equals("7E") && (str.substring(10,12).equals("22")) && str.length()==320){
				synchronized (listarray4) {
					long gatherid = Integer.valueOf(str.substring(16, 20));
					for(int i=0;i<listarray4.size();i+=23){
						if(gatherid == Integer.valueOf(listarray4.get(i))){
							String electricity = Integer.toString(Integer.valueOf(str.subSequence(86, 90).toString(),16));
		                    String voltage = Integer.toString(Integer.valueOf(str.subSequence(90, 94).toString(),16)/10);
		                    String electricity1 = Integer.toString(Integer.valueOf(str.subSequence(86+80, 90+80).toString(),16));
		                    String voltage1 = Integer.toString(Integer.valueOf(str.subSequence(90+80, 94+80).toString(),16)/10);
		                    String electricity2 = Integer.toString(Integer.valueOf(str.subSequence(86+80+80, 90+80+80).toString(),16));
		                    String voltage2 = Integer.toString(Integer.valueOf(str.subSequence(90+80+80, 94+80+80).toString(),16)/10);
		                    
		                    String year = Integer.valueOf(str.subSequence(74, 76).toString(),16).toString();
		      	    		String month = Integer.valueOf(str.subSequence(76, 78).toString(),16).toString();
		      	    		String day = Integer.valueOf(str.subSequence(78, 80).toString(),16).toString();
		      	    		String hour = Integer.valueOf(str.subSequence(80, 82).toString(),16).toString();
		      	    		String minute = Integer.valueOf(str.subSequence(82, 84).toString(),16).toString();
		      	    		String second = Integer.valueOf(str.subSequence(84, 86).toString(),16).toString();
		      	    		String strdate = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
		      	    		String year1 = Integer.valueOf(str.subSequence(74+80, 76+80).toString(),16).toString();
		      	    		String month1 = Integer.valueOf(str.subSequence(76+80, 78+80).toString(),16).toString();
		      	    		String day1 = Integer.valueOf(str.subSequence(78+80, 80+80).toString(),16).toString();
		      	    		String hour1 = Integer.valueOf(str.subSequence(80+80, 82+80).toString(),16).toString();
		      	    		String minute1 = Integer.valueOf(str.subSequence(82+80, 84+80).toString(),16).toString();
		      	    		String second1 = Integer.valueOf(str.subSequence(84+80, 86+80).toString(),16).toString();
		      	    		String strdate1 = year1+"-"+month1+"-"+day1+" "+hour1+":"+minute1+":"+second1;
		      	    		String year2 = Integer.valueOf(str.subSequence(74+80+80, 76+80+80).toString(),16).toString();
		      	    		String month2 = Integer.valueOf(str.subSequence(76+80+80, 78+80+80).toString(),16).toString();
		      	    		String day2 = Integer.valueOf(str.subSequence(78+80+80, 80+80+80).toString(),16).toString();
		      	    		String hour2 = Integer.valueOf(str.subSequence(80+80+80, 82+80+80).toString(),16).toString();
		      	    		String minute2 = Integer.valueOf(str.subSequence(82+80+80, 84+80+80).toString(),16).toString();
		      	    		String second2 = Integer.valueOf(str.subSequence(84+80+80, 86+80+80).toString(),16).toString();
		      	    		String strdate2 = year2+"-"+month2+"-"+day2+" "+hour2+":"+minute2+":"+second2;
		      	    		
		      	    		String status = Integer.toString(Integer.parseInt(str.subSequence(114, 116).toString(),16));
		      	    		String status1 = Integer.toString(Integer.parseInt(str.subSequence(114+80, 116+80).toString(),16));
		      	    		String status2 = Integer.toString(Integer.parseInt(str.subSequence(114+80+80, 116+80+80).toString(),16));
							
		      	    		String[] codebuf1 = str.subSequence(24, 66).toString().split("2A");
		      	    		String codebuf2 = codebuf1[codebuf1.length-1];
		      	    		String code = "";
		      	    		for(int i1=0;i1<codebuf2.length();i1+=2){
		      	    			code = code + codebuf2.substring(i1+1,i1+2);
		      	    		}
		      	    		
		      	    		String weldspeed = Integer.toString(Integer.parseInt(str.subSequence(66, 70).toString(),16));
		                    
		                    listarray4.set(i+2, electricity);
		                    listarray4.set(i+3, voltage);
		                    listarray4.set(i+4, strdate);
		                    listarray4.set(i+5, status);
		                    listarray4.set(i+6, code);
		                    listarray4.set(i+7, weldspeed);
		                    //listarray4.set(i+8, warn);
		                    listarray4.set(i+9, electricity1);
		                    listarray4.set(i+10, voltage1);
		                    listarray4.set(i+11, strdate1);
		                    listarray4.set(i+12, status1);
		                    listarray4.set(i+13, code);
		                    listarray4.set(i+14, weldspeed);
		                    //listarray4.set(i+15, warn1);
		                    listarray4.set(i+16, electricity2);
		                    listarray4.set(i+17, voltage2);
		                    listarray4.set(i+18, strdate2);
		                    listarray4.set(i+19, status2);
		                    listarray4.set(i+20, code);
		                    listarray4.set(i+21, weldspeed);
		                    //listarray4.set(i+22, warn2);
		                    break;
						}
					}
				}
				mysql.Mysqlohwh(str);
		        //websocket.Websocketohwh(str,listarray2,listarray3,websocketlist);
				
			} else if(str.equals("SS")){  //欧华纬华调用webservice获取实时数据
	        	
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
        				for(int i=0;i<listarray4.size();i+=23){
        					if(listarray4.get(i+1) == null){
            					jo.put("num", "0000");
        					}else if(listarray4.get(i+1) != null){
            					jo.put("num", listarray4.get(i+1));
        					}
        					jo.put("ele1", listarray4.get(i+2));
        					jo.put("vol1", listarray4.get(i+3));
        					jo.put("time1", listarray4.get(i+4));
        					jo.put("status1", listarray4.get(i+5));
        					jo.put("code1", listarray4.get(i+6));
        					jo.put("speed1", listarray4.get(i+7));
        					jo.put("airflow1", listarray4.get(i+8));
        					jo.put("ele2", listarray4.get(i+9));
        					jo.put("vol2", listarray4.get(i+10));
        					jo.put("time2", listarray4.get(i+11));
        					jo.put("status2", listarray4.get(i+12));
        					jo.put("code2", listarray4.get(i+13));
        					jo.put("speed2", listarray4.get(i+14));
        					jo.put("airflow2", listarray4.get(i+15));
        					jo.put("ele3", listarray4.get(i+16));
        					jo.put("vol3", listarray4.get(i+17));
        					jo.put("time3", listarray4.get(i+18));
        					jo.put("status3", listarray4.get(i+19));
        					jo.put("code3", listarray4.get(i+20));
        					jo.put("speed3", listarray4.get(i+21));
        					jo.put("airflow3", listarray4.get(i+22));
        					ja.add(jo);
        				}
        				
        				data = ja.toString();
        				
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
                
	        }
			
			//处理焊机下发和上传
	        else{    
	        	
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
