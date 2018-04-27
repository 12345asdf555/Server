import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public HashMap<String, Socket> websocket;
    public ArrayList<String> dbdata = new ArrayList<String>();
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public HashMap<String, SocketChannel> websocketlist = new HashMap<>();
	
    public int a=0;
    
	@Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		 
		 try{
			 String str = (String) msg;
			 Workspace ws = new Workspace(str);
	         workThread = new Thread(ws);  
	         workThread.start(); 
	         
			 /*ByteBuf buf=(ByteBuf)msg; 
			 byte[] req=new byte[buf.readableBytes()];  
		     buf.readBytes(req);
		     Workspace ws = new Workspace(req);
	         workThread = new Thread(ws);  
	         workThread.start();*/
			      
	         
		 }finally{
			 
			 ReferenceCountUtil.release(msg);
			 
		 }
		 
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
			//try {
				/*for(int i=0;i<req.length;i++){
	             
	            //判断为数字还是字母，若为字母+256取正数
	            if(req[i]<0){
	              String r = Integer.toHexString(req[i]+256);
	              String rr=r.toUpperCase();
	                //数字补为两位数
	                if(rr.length()==1){
	                	rr='0'+rr;
	                }
	                //strdata为总接收数据
	                str += rr;
	               
	            }
	            else{
	              String r = Integer.toHexString(req[i]);
	              if(r.length()==1)
	              r='0'+r;
	              r=r.toUpperCase();
	              str+=r;  
	            }
	        }*/
				
			//System.out.println(str);
			/*new Mysql(str,connet,listarray1);
	        new Socketsend(str,ip1);
	        new Websocket(str,connet,websocket,listarray2,listarray3,websocketlist);*/
			
			
			if(str.substring(0,2).equals("FA")){
				
				String[] strlist =str.split("F5");
		        for(int i=0;i<strlist.length;i++){
		        	
		        	str = strlist[i];
		        	str = str + "F5";
		        	
		        	try{
		        		if(stmt==null || stmt.isClosed()==true)
			        	{
			        		try {
								Class.forName("com.mysql.jdbc.Driver");
								stmt = DriverManager.getConnection(connet).createStatement();
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
		        	}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
		        	
		        	new Mysql(str,stmt,listarray2);
			        new Websocket(str,stmt,websocket,listarray2,listarray3,websocketlist,dbdata);
			        //System.out.println("1");
			        //new Socketsend(str,ip1);
			        if(socketchannel!=null){
				        try {
							socketchannel.writeAndFlush(str).sync();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
		        }
	        
	        }else{
	        	Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
                while(webiter.hasNext()){
                	try{
                    	Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                    	websocketfail = entry.getKey();
                    	SocketChannel websocketcon = entry.getValue();
                    	websocketcon.writeAndFlush(new TextWebSocketFrame(str)).sync();
                	}catch (Exception e) {
						websocketlist.remove(websocketfail);
						webiter = websocketlist.entrySet().iterator();
   					 }
                }
	        }
					
			/*} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		        //new Websocket(str,connet,websocket,listarray2,listarray3);
		        //new Sqlite(str);
		    
			
	        /*for(int i=0;i<req.length;i++){
	             
	            //判断为数字还是字母，若为字母+256取正数
	            if(req[i]<0){
	              String r = Integer.toHexString(req[i]+256);
	              String rr=r.toUpperCase();
	                //数字补为两位数
	                if(rr.length()==1){
	                	rr='0'+rr;
	                }
	                //strdata为总接收数据
	                str += rr;
	               
	            }
	            else{
	              String r = Integer.toHexString(req[i]);
	              if(r.length()==1)
	              r='0'+r;
	              r=r.toUpperCase();
	              str+=r;  
	            }
	        }*/
			
		}
 
	 }
	 
	 /*public Runnable work = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			new Mysql(str,connet,listarray1);
	        new Socketsend(str,ip1);
	        new Websocket(str,connet,websocket,listarray2,listarray3);
		} 
		 
	 };*/
	 
	 @Override  
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
		 super.channelReadComplete(ctx);  
	     ctx.flush();  
	 } 
     @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
         ctx.close();  
     } 
	 
}
