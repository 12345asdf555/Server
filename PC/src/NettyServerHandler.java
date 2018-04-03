import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyServerHandler extends ChannelHandlerAdapter{
	
	public String ip;
    public String ip1;
    public String str;
    public String connet;
    public HashMap<String, Socket> websocket;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
	
	 @Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		 
		 ByteBuf buf=(ByteBuf)msg;  
         byte[] req=new byte[buf.readableBytes()];  
         buf.readBytes(req);  
         str=new String(req,"UTF-8");   
         //System.out.println(str);
         new Thread(work).start();
         
	 }
	 
	 public Runnable work = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			new Mysql(str,connet,listarray1);
	        new Socketsend(str,ip1);
	        new Websocket(str,connet,websocket,listarray2,listarray3);
		} 
		 
	 };
	 
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
