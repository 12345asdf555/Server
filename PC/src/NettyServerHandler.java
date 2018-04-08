import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelHandler.Sharable;
@Sharable 
public class NettyServerHandler extends ChannelHandlerAdapter{
	
	public String ip;
    public String ip1;
    public String connet;
    public Thread workThread;
    public HashMap<String, Socket> websocket;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
	
    public int a=0;
    
	 @Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		 
		 try{
			 
			 ByteBuf buf=(ByteBuf)msg; 
			 byte[] req=new byte[buf.readableBytes()];  
		     buf.readBytes(req);
			 Workspace ws = new Workspace(req);
	         workThread = new Thread(ws);  
	         workThread.start();      
	         
		 }finally{
			 
			 ReferenceCountUtil.release(msg);
			 
		 }
		 
	 }
	 
	 public class Workspace implements Runnable{

		private byte[] req;
		private String str="";

		public Workspace(byte[] req) {
			// TODO Auto-generated constructor stub
			
			this.req=req;
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
	         
	        for(int i=0;i<req.length;i++){
	             
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
	        }
	        
	        System.out.println(str);
	        
	        String[] strlist =str.split("F5");
	        for(int i=0;i<strlist.length;i++){
	        	
	        	str = strlist[i];
	        	str = str + "F5";
	        	
	        	new Mysql(str,connet,listarray1);
		        new Socketsend(str,ip1);
		        new Websocket(str,connet,websocket,listarray2,listarray3);
		        //new Sqlite(str);
		        
	        }

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
