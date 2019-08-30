import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
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

			if(str.substring(0,2).equals("7E") && (str.substring(10,12).equals("22")) && (str.length()==284 || str.length()==124)){

				mysql.Mysqlbase(str);
				websocket.Websocketbase(str,listarray2,listarray3,websocketlist);
				if(socketchannel!=null){
					synchronized (socketchannel) {
						try {
							socketchannel.writeAndFlush(str).sync();
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

			}else if(str.substring(0,2).equals("FA")){  //处理实时数据

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

			}else if(str.substring(0,2).equals("þ")){   //处理android数据

				android.Androidrun(str);

			}else if(str.substring(0,2).equals("JN")){  //江南任务派发 任务号、焊工、焊机、状态
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
                
	        
	        } else if(str.length()==38 && str.substring(10,12).equals("01")){    //处理焊层焊道信息
				mysql.db.ceng = Integer.valueOf(str.substring(18, 20),16);
				mysql.db.dao = Integer.valueOf(str.substring(20, 22),16);
				mysql.db.weldstatus = Integer.valueOf(str.substring(16, 18),16);
				if(socketchannel!=null){
					try {
						socketchannel.writeAndFlush(str).sync();
					} catch (Exception e) {
						socketchannel = null;
						e.printStackTrace();
					}
				}
			} else if(str.substring(0,2).equals("fe") && str.substring(str.length()-2, str.length()).equals("fe")){  //华域PLC
				//System.out.println("1");
				System.out.println("接收数据:"+str);
				if(socketchannel!=null){
					//System.out.println(socketchannel);
					synchronized (socketchannel) {
						try {
							socketchannel.writeAndFlush(str).sync();
							System.out.println("发送成功:"+str);
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

			} else{    //处理焊机下发和上传
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
