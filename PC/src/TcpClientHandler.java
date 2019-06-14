import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.SocketChannel;
@Sharable
public class TcpClientHandler extends SimpleChannelInboundHandler{

	public Client client;
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
	private String socketfail;
	
	public TcpClientHandler(Client client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}
	
	public TcpClientHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		String str = (String) arg1;
		
		if(str.substring(0,2).equals("JN")){  //江南任务派发 任务号、焊工、焊机、状态
        	
        	synchronized (socketlist) {
        	ArrayList<String> listarraybuf = new ArrayList<String>();
        	boolean ifdo = false;
        	
        	Iterator<Entry<String, SocketChannel>> iter = socketlist.entrySet().iterator();
            while(iter.hasNext()){
            	try{
                	Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) iter.next();
                	
                	socketfail = entry.getKey();

    				SocketChannel socketcon = entry.getValue();
                	socketcon.writeAndFlush(str).sync();
                	
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
            
        } else if(str.substring(0,12).equals("7E3501010152") || str.substring(0,10).equals("FE5AA5006e")){
        	
        	synchronized (socketlist) {
        	ArrayList<String> listarraybuf = new ArrayList<String>();
        	boolean ifdo = false;
        	
			Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
            while(webiter.hasNext()){
            	try{
                	Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                	socketfail = entry.getKey();
                	SocketChannel socketcon = entry.getValue();
            		socketcon.writeAndFlush(str).sync();
                	/*String[] socketip1 = socketcon.toString().split("/");
                	String[] socketip2 = socketip1[1].split(":");
                	String socketip = socketip2[0];
                	if(!socketip.equals("121.196.222.216")){
                		socketcon.writeAndFlush(str).sync();
                	}*/
                	
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
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
      ctx.channel().close();  
      ctx.close();
      ctx.disconnect();
      ctx.flush();
    }  
	
	@Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
	
      final EventLoop eventLoop = ctx.channel().eventLoop();  
      eventLoop.schedule(new Runnable() {  
    	@Override  
        public void run() {
    		client.createBootstrap(new Bootstrap(),eventLoop);
        }  
      }, 1L, TimeUnit.SECONDS);
      super.channelInactive(ctx);  
    }  
	
}
