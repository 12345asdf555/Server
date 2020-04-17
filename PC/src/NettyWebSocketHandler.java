import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
@Sharable
public class NettyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

	public HashMap<String, SocketChannel> socketlist = new HashMap<>();
	public ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());
	private WebSocketServerHandshaker handsharker;
	private String socketfail;
	public ArrayList<String> taskarray = new ArrayList<String>();
	public Server server;

	public NettyWebSocketHandler(Server server) {
		// TODO Auto-generated constructor stub
		this.server = server;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 娣诲姞
		//group.add(ctx.channel());
		//System.out.println("瀹㈡埛绔笌鏈嶅姟绔繛鎺ュ紑鍚細" + ctx.channel().remoteAddress().toString());
	}

	/**
	 * channel 閫氶亾 Inactive 涓嶆椿璺冪殑 褰撳鎴风涓诲姩鏂紑鏈嶅姟绔殑閾炬帴鍚庯紝杩欎釜閫氶亾灏辨槸涓嶆椿璺冪殑銆備篃灏辨槸璇村鎴风涓庢湇鍔＄鍏抽棴浜嗛�氫俊閫氶亾骞朵笖涓嶅彲浠ヤ紶杈撴暟鎹�
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 绉婚櫎
		//group.remove(ctx.channel());
		//System.out.println("瀹㈡埛绔笌鏈嶅姟绔繛鎺ュ叧闂細" + ctx.channel().remoteAddress().toString());
	}
	/**
	 * exception 寮傚父 Caught 鎶撲綇 鎶撲綇寮傚父锛屽綋鍙戠敓寮傚父鐨勬椂鍊欙紝鍙互鍋氫竴浜涚浉搴旂殑澶勭悊锛屾瘮濡傛墦鍗版棩蹇椼�佸叧闂摼鎺�
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	/**
	 * 鎺ュ彈鏁版嵁
	 */
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 濡傛灉鏄疕TTP璇锋眰
		if (msg instanceof FullHttpRequest) {
			//handleHttpRequest(ctx, (FullHttpRequest) msg);
		}
		// WEBSOCKET鎺ュ叆
		else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
	/**
	 * 澶勭悊HTTP璇锋眰
	 * 
	 * @param ctx
	 * @param msg
	 */
	/*private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 濡傛灉瑙ｇ爜澶辫触,杩斿洖寮傚父
        if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // 鏋勯�犳彙鎵嬬浉搴�
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8080/websocket", null, Boolean.FALSE);
        handsharker = wsFactory.newHandshaker(req);
        if (handsharker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handsharker.handshake(ctx.channel(), req);
        }
    }*/


	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 鍒ゆ柇閾捐矾鏄惁鍏抽棴

		if (frame instanceof CloseWebSocketFrame) {
			handsharker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			new PongWebSocketFrame(frame.content().retain());
			return;
		}
		// 鍙敮鎸佹枃鏈秷鎭�,涓嶆敮鎸佷簩杩涘埗娑堟伅
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(
					String.format("%s frame type not supported", frame.getClass().getName()));
		}

		String str = ((TextWebSocketFrame) frame).text();
		if (str.length()<2)
		{
			return ;
		}
		HashMap<String, SocketChannel> socketlist_cl;
		synchronized (socketlist){
			socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
		}
		if(str.substring(0,2).equals("7E")){

			//synchronized (socketlist)
			//{
			ArrayList<String> listarraybuf = new ArrayList<String>();
			boolean ifdo = false;

			Iterator<Entry<String, SocketChannel>> webiter = socketlist_cl.entrySet().iterator();
			while(webiter.hasNext())
			{
				try{
					Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
					socketfail = entry.getKey();
					SocketChannel socketcon = entry.getValue();
					String[] socketip1 = socketcon.toString().split("/");
					String[] socketip2 = socketip1[1].split(":");
					String socketip = socketip2[0];
					//if(!socketip.equals("192.168.1.101")){
					if(!socketip.equals("121.196.222.216")){
						//socketcon.writeAndFlush(str).sync();
						socketcon.writeAndFlush(str);
					}

				}catch (Exception e) {
					listarraybuf.add(socketfail);
					ifdo = true;
				}
			}

			if(ifdo){
				synchronized (socketlist){
					//socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
					for(int i=0;i<listarraybuf.size();i++){
						socketlist.remove(listarraybuf.get(i));
					}
				}

			}
			//}

		}else if(str.substring(0,1).equals("0") || str.substring(0,1).equals("1")){

			//synchronized (socketlist)
			//{
			ArrayList<String> listarraybuf = new ArrayList<String>();
			boolean ifdo = false;

			Iterator<Entry<String, SocketChannel>> webiter = socketlist_cl.entrySet().iterator();
			while(webiter.hasNext()){
				try{
					Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
					socketfail = entry.getKey();
					SocketChannel socketcon = entry.getValue();
					String[] socketip1 = socketcon.toString().split("/");
					String[] socketip2 = socketip1[1].split(":");
					String socketip = socketip2[0];
					//if(!socketip.equals("192.168.1.101")){
					if(!socketip.equals("121.196.222.216")){
						socketcon.writeAndFlush(str).sync();
					}

				}catch (Exception e) {
					listarraybuf.add(socketfail);
					ifdo = true;
				}
			}

			if(ifdo){
				synchronized (socketlist){
					//socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
					for(int i=0;i<listarraybuf.size();i++){
						socketlist.remove(listarraybuf.get(i));
					}
				}
			}
		}else if(str.substring(0,1).equals("{")){
		   try {
	            JSONObject taskstr = JSONObject.parseObject(str);
	            
	            if(taskstr.getString("type").equals("starttask") || taskstr.getString("type").equals("nexttask")){
		            if(taskarray.isEmpty()){
		            	taskarray.add(taskstr.getString("machine"));
		            	taskarray.add(taskstr.getString("welderid"));
		            	taskarray.add(taskstr.getString("cardid"));
		            	taskarray.add(taskstr.getString("wpsid"));
		            	taskarray.add(taskstr.getString("productid"));
		            	taskarray.add(taskstr.getString("workprocedureid"));
		            	taskarray.add(taskstr.getString("workstepid"));
		            	taskarray.add(taskstr.getString("weldlineid"));
		            }else{
		            	if(taskarray.contains(taskstr.getString("machine"))){
		            		int index = taskarray.indexOf(taskstr.getString("machine"));
		            		taskarray.set(index+1, taskstr.getString("welderid"));
		            		taskarray.set(index+2, taskstr.getString("cardid"));
		            		taskarray.set(index+3, taskstr.getString("wpsid"));
		            		taskarray.set(index+4, taskstr.getString("productid"));
		            		taskarray.set(index+5, taskstr.getString("workprocedureid"));
		            		taskarray.set(index+6, taskstr.getString("workstepid"));
		            		taskarray.set(index+7, taskstr.getString("weldlineid"));
		            		
		            	}else{
		            		taskarray.add(taskstr.getString("machine"));
			            	taskarray.add(taskstr.getString("welderid"));
			            	taskarray.add(taskstr.getString("cardid"));
			            	taskarray.add(taskstr.getString("wpsid"));
			            	taskarray.add(taskstr.getString("productid"));
			            	taskarray.add(taskstr.getString("workprocedureid"));
			            	taskarray.add(taskstr.getString("workstepid"));
			            	taskarray.add(taskstr.getString("weldlineid"));
		            	}
		            }
	            }else if(taskstr.getString("type").equals("overtask")){
	            	if(!taskarray.isEmpty() && taskarray.contains(taskstr.getString("machine"))){
	            		int index = taskarray.indexOf(taskstr.getString("machine"));
	            		taskarray.remove(index);
	            		taskarray.remove(index);
	            		taskarray.remove(index);
	            		taskarray.remove(index);
	            		taskarray.remove(index);
	            		taskarray.remove(index);
	            		taskarray.remove(index);
	            		taskarray.remove(index);
	            	}
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		    
		   this.server.NS.mysql.db.taskarray = taskarray;
		   this.server.NS.websocket.taskarray = taskarray;
		   
		}
	}

}
