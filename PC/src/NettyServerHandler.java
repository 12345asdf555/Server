import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.DriverManager;
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
		
			if(str.substring(0,2).equals("7E") && (str.substring(10,12).equals("22")) && str.length()==290){
				
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
                
	        }else{    //处理焊机下发和上传
	        	
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
