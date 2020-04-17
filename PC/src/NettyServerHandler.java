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
    public Thread workThread;
    public java.sql.Statement stmt =null;
	public java.sql.Connection conn = null;
    public SocketChannel socketchannel = null;
    public ArrayList<String> dbdata = new ArrayList<String>();
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public ArrayList<String> listarray4 = new ArrayList<String>();
    public ArrayList<String> listarrayplc = new ArrayList<String>();  //鐒婂伐銆佸伐浣嶅搴�
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
		private String data;

		public Workspace(String str) {
			// TODO Auto-generated constructor stub
			
			this.str=str;
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
		
			//西安处理
			if(str.substring(0,2).equals("7E") && (str.substring(10,12).equals("22")) && str.length()==596){

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
		       
		    //娆у崕绾崕
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
		        websocket.Websocketohwh(str,listarray2,listarray3,websocketlist);
				
			} else if(str.substring(0,2).equals("FA")){  //澶勭悊瀹炴椂鏁版嵁
				
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
		        
	        }else if(str.substring(0,2).equals("镁")){   //澶勭悊android鏁版嵁
	        	
	        	android.Androidrun(str);
	        	
	        }else if(str.substring(0,2).equals("JN")){  //姹熷崡浠诲姟娲惧彂 浠诲姟鍙枫�佺剨宸ャ�佺剨鏈恒�佺姸鎬�
	        	String[] datainf = str.split(",");

				String datasend = "";
	        	String junction = "";
	        	String cengdao = "";
	            String gather = "";
	        	int cengdaocount = 0;
	        	
	        	try{
					if(stmt==null || stmt.isClosed()==true || !conn.isValid(1))
					{
						try {
							Class.forName("com.mysql.jdbc.Driver");
							conn = DriverManager.getConnection(connet);
							stmt = conn.createStatement();
						} catch (ClassNotFoundException e) {  
							System.out.println("Broken driver");
							e.printStackTrace();
							return;
						} catch (SQLException e) {
							System.out.println("Broken conn");
							e.printStackTrace();
							return;
						}  
					}
					
					String inSql = "SELECT tb_welded_junction.fwelded_junction_no,tb_specification.fsolder_layer,tb_specification.fweld_bead FROM tb_welded_junction INNER JOIN tb_specification ON tb_welded_junction.fwpslib_id = tb_specification.fwpslib_id WHERE tb_welded_junction.fid = '" + datainf[1] + "' ORDER BY tb_specification.fweld_bead asc";
					ResultSet rs =stmt.executeQuery(inSql);
		            
		            while (rs.next()) {
		            	junction = rs.getString("tb_welded_junction.fwelded_junction_no");
		            	String ceng = Integer.toString(Integer.valueOf(rs.getString("tb_specification.fsolder_layer"),16));
		            	if(ceng.length()!=2){
	            			ceng = "0" + ceng;
		            	}
		            	String dao = Integer.toString(Integer.valueOf(rs.getString("tb_specification.fweld_bead"),16));
		            	if(dao.length()!=2){
		            		dao = "0" + dao;
		            	}
		            	cengdao = cengdao + ceng + dao;
		            	cengdaocount++;
		            }
		            
		            String inSql1 = "SELECT fgather_no FROM tb_gather INNER JOIN tb_welding_machine ON tb_gather.fid = tb_welding_machine.fgather_id WHERE tb_welding_machine.fid = '" + datainf[3] + "'";
					ResultSet rs1 =stmt.executeQuery(inSql1);
					while (rs1.next()) {
		            	gather = Integer.toString(Integer.valueOf(rs.getString("fgather_no"),16));
		            	if(gather.length()!=4){
		            		for(int i=0;i<4-gather.length();i++){
		            			gather = "0" + gather;
		            		}
		            	}
		            }
					
	        	}catch (Exception e){
	        		e.getStackTrace();
	        	}

	            if(cengdao.length() != 100){
	            	for(int i=0;i<100-cengdao.length();i++){
	            		cengdao = cengdao + "0";
	            	}
	            }
	            
	            if(junction.length() != 30){
	            	for(int i=0;i<30-junction.length();i++){
	            		cengdao = cengdao + "0";
	            	}
	            }
	            
	            String cdcount = Integer.toString(cengdaocount);
	            if(cdcount.length() != 4){
	            	for(int i=0;i<4-cdcount.length();i++){
	            		cdcount = cdcount + "0";
	            	}
	            }
	            
				if(datainf[4].equals("0")){
		            datasend = "7E0001010122" + gather + "00" + junction + cengdao + cdcount + "017D";
	            }else if(datainf[4].equals("1")){
		            datasend = "7E0001010122" + gather + "01" + junction + cengdao + cdcount + "017D";
	            }
	            
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
                    	socketcon.writeAndFlush(datasend).sync();
                    	
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
                
	        
	        } else if(str.substring(0,2).equals("fe") && str.length() == 298){
	        	
	        	mysql.Mysqlplc(str,listarrayplc);
	        	System.out.println(str);
	        	
	        	if(socketchannel!=null){
					//System.out.println(socketchannel);
					synchronized (socketchannel) {
						try {
							socketchannel.writeAndFlush(str).sync();
							System.out.println("鍙戦�佹垚鍔�:"+str);
						} catch (Exception e) {
							try {
								socketchannel.close().sync();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							socketchannel = null;
							e.printStackTrace();
						}
					}
				}
	        	
	        } else if(str.equals("SS")){  //娆у崕绾崕璋冪敤webservice鑾峰彇瀹炴椂鏁版嵁
	        	
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
			//閲嶅伐webservice鑾峰彇瀹炴椂鏁版嵁
			/*else if(str.equals("SS")){  
	        	
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
        					jo.put("setele1", listarray4.get(i+6));
        					jo.put("setvol1", listarray4.get(i+7));
        					jo.put("warn1", listarray4.get(i+8));
        					jo.put("ele2", listarray4.get(i+9));
        					jo.put("vol2", listarray4.get(i+10));
        					jo.put("time2", listarray4.get(i+11));
        					jo.put("status2", listarray4.get(i+12));
        					jo.put("setele2", listarray4.get(i+13));
        					jo.put("setvol2", listarray4.get(i+14));
        					jo.put("warn2", listarray4.get(i+15));
        					jo.put("ele3", listarray4.get(i+16));
        					jo.put("vol3", listarray4.get(i+17));
        					jo.put("time3", listarray4.get(i+18));
        					jo.put("status3", listarray4.get(i+19));
        					jo.put("setele3", listarray4.get(i+20));
        					jo.put("setvol3", listarray4.get(i+21));
        					jo.put("warn3", listarray4.get(i+22));
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
                
	        }*/
			//澶勭悊鐒婃満涓嬪彂鍜屼笂浼�
	        else{    
	        	
	        	//mqtt处理
	        	//mqtt.publishMessage("weldmeswebdataup", str, 0);
	        	
	        	
	        	//基本版2处理
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
         ctx.close().sync();  
    }
	 
}
