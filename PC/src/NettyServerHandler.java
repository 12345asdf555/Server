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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
	public MyMqttClient mqtt;

	@Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		String str = "";
		try{
			str = (String) msg;
			Workspace ws = new Workspace(str,ctx);
			workThread = new Thread(ws);
			workThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					System.err.println(Thread.currentThread().getName() + "==>" + e.getMessage());
				}
			});
			workThread.start();

			//Thread.sleep(1000);
			//workThread.interrupt();

			ReferenceCountUtil.release(msg);
			ReferenceCountUtil.release(str);
		}catch(Exception e){
			//e.printStackTrace();
		}

		ctx.flush();
	}

	public class Workspace implements Runnable{

		private String str="";
		public byte[] req;
		private String socketfail;
		private String websocketfail;
		private ChannelHandlerContext ctx;

		public Workspace(String str, ChannelHandlerContext ctx) {
			// TODO Auto-generated constructor stub

			this.str=str;
			this.ctx=ctx;

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (this) {
				try{
					if(!str.substring(0,2).equals("7E")){
						JSONObject strjson = JSONObject.parseObject(str);

						if(strjson.getString("type").equals("wpsdown")){
							String mach = Integer.toHexString(Integer.valueOf(strjson.getString("mach")));
							if(mach.length() != 4){
								int count = mach.length();
								for(int i=0;i<4-count;i++){
									mach = "0" + mach;
								}
							}
							int wpstype = Integer.valueOf(strjson.getString("machinetype"));
							JSONArray strarray = JSONArray.parseArray(strjson.getString("data"));
							for(int i=0;i<strarray.size();i++){
								String channel = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("channel")));
								if (channel.length() < 2)
								{
									int count = 2 - channel.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										channel = "0" + channel;
									}
								}
								String ele = Integer.toHexString(Double.valueOf(strarray.getJSONObject(i).getString("ele")).intValue());
								if (ele.length() < 4)
								{
									int count = 4 - ele.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										ele = "0" + ele;
									}
								}
								String vol = Integer.toHexString(Double.valueOf(strarray.getJSONObject(i).getString("vol")).intValue());
								if (vol.length() < 4)
								{
									int count = 4 - vol.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										vol = "0" + vol;
									}
								}
								String eletuny = Integer.toHexString(Double.valueOf(strarray.getJSONObject(i).getString("eletuny")).intValue());
								if (eletuny.length() < 2)
								{
									int count = 2 - eletuny.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										eletuny = "0" + eletuny;
									}
								}
								String voltuny = Integer.toHexString(Double.valueOf(strarray.getJSONObject(i).getString("voltuny")).intValue());
								if (voltuny.length() < 2)
								{
									int count = 2 - voltuny.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										voltuny = "0" + voltuny;
									}
								}
								String eleup = Integer.toHexString(Double.valueOf(strarray.getJSONObject(i).getString("ele")).intValue()+Double.valueOf(strarray.getJSONObject(i).getString("eletuny")).intValue());
								if (eleup.length() < 4)
								{
									int count = 4 - eleup.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										eleup = "0" + eleup;
									}
								}
								String eledown = Integer.toHexString(Double.valueOf(strarray.getJSONObject(i).getString("ele")).intValue()-Double.valueOf(strarray.getJSONObject(i).getString("eletuny")).intValue());
								if (eledown.length() < 4)
								{
									int count = 4 - eledown.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										eledown = "0" + eledown;
									}
								}
								String volup = Integer.toHexString(Double.valueOf(strarray.getJSONObject(i).getString("vol")).intValue()+Double.valueOf(strarray.getJSONObject(i).getString("voltuny")).intValue());
								if (volup.length() < 4)
								{
									int count = 4 - volup.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										volup = "0" + volup;
									}
								}
								String voldown = Integer.toHexString(Double.valueOf(strarray.getJSONObject(i).getString("vol")).intValue()-Double.valueOf(strarray.getJSONObject(i).getString("voltuny")).intValue());
								if (voldown.length() < 4)
								{
									int count = 4 - voldown.length();
									for (int i1 = 0; i1 < count; i1++)
									{
										voldown = "0" + voldown;
									}
								}


								if(wpstype == 1 ){
									if(Integer.valueOf(strarray.getJSONObject(i).getString("channel")) < 10){
										
										String material = "00";
										try{
											material = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("material")));
											if (material.length() < 2)
											{
												int count = 2 - material.length();
												for (int i1 = 0; i1 < count; i1++)
												{
													material = "0" + material;
												}
											}
										}catch(Exception e){
											System.out.println("材质赋值有误！");
										}
										
										String diameter = "00";
										try{
											diameter = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("diameter")));
											if (diameter.length() < 2)
											{
												int count = 2 - diameter.length();
												for (int i1 = 0; i1 < count; i1++)
												{
													diameter = "0" + diameter;
												}
											}
										}catch(Exception e){
											System.out.println("丝径赋值有误！");
										}
										
										String gas = "00";
										try{
											gas = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("gas")));
											if (gas.length() < 2)
											{
												int count = 2 - gas.length();
												for (int i1 = 0; i1 < count; i1++)
												{
													gas = "0" + gas;
												}
											}
										}catch(Exception e){
											System.out.println("气体赋值有误！");
										}
										
										String control = "01";
										try{
											control = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("control")));
											if (control.length() < 2)
											{
												int count = 2 - control.length();
												for (int i1 = 0; i1 < count; i1++)
												{
													control = "0" + control;
												}
											}
										}catch(Exception e){
											System.out.println("控制方式赋值有误！");
										}
										
										String pulse = "00";
										try{
											pulse = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("pulse")));
											if (pulse.length() < 2)
											{
												int count = 2 - pulse.length();
												for (int i1 = 0; i1 < count; i1++)
												{
													pulse = "0" + pulse;
												}
											}
										}catch(Exception e){
											System.out.println("脉冲赋值有误！");
										}
										
										String wpsstr = "FE5AA5006e" + mach + "0000000000000000000000001f021102" + channel + "0000" + eleup + volup + eledown + voldown + "00a000be008c00b4008c00be007800b4" + material + diameter + gas + control + pulse + "000a000000000004d204d204d204d204d204d20a0a00a000dc007800a004d2153804d2153804d2153804d2153814147b007b7b7b7b6400";
										String wpsreturn = "{type:\"wpsreturn\",result:\"0\"}";
										synchronized (socketlist) {
											ArrayList<String> listarraybuf = new ArrayList<String>();
											boolean ifdo= false;
	
											Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
											while(webiter.hasNext()){
												try{
													Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
													websocketfail = entry.getKey();
													SocketChannel websocketcon = entry.getValue();
													if(websocketcon.remoteAddress().equals(ctx.channel().remoteAddress())) {
														websocketcon.writeAndFlush(wpsstr).sync();
														Thread.sleep(1000);
														websocketcon.writeAndFlush(wpsreturn).sync();
													}else {
														Thread.sleep(1000);
														websocketcon.writeAndFlush(wpsstr).sync();
													}
												}catch (Exception e) {
													listarraybuf.add(websocketfail);
													ifdo = true;
												}
											}
	
											if(ifdo){
												for(int i1=0;i1<listarraybuf.size();i1++){
													socketlist.remove(listarraybuf.get(i1));
												}
											}
										}
									}
								}else{
									String material = "00";
									try{
										material = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("material")));
										if (material.length() < 2)
										{
											int count = 2 - material.length();
											for (int i1 = 0; i1 < count; i1++)
											{
												material = "0" + material;
											}
										}
									}catch(Exception e){
										System.out.println("材质赋值有误！");
									}
									
									String diameter = "10";
									try{
										diameter = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("diameter")));
										if (diameter.length() < 2)
										{
											int count = 2 - diameter.length();
											for (int i1 = 0; i1 < count; i1++)
											{
												diameter = "0" + diameter;
											}
										}
									}catch(Exception e){
										System.out.println("丝径赋值有误！");
									}
									
									String gas = "00";
									try{
										gas = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("gas")));
										if (gas.length() < 2)
										{
											int count = 2 - gas.length();
											for (int i1 = 0; i1 < count; i1++)
											{
												gas = "0" + gas;
											}
										}
									}catch(Exception e){
										System.out.println("气体赋值有误！");
									}
									
									String control = "0040";
									try{
										Integer controlbuf = Integer.valueOf(strarray.getJSONObject(i).getString("control"));
										if (controlbuf == 0)
										{
											control = "0040";
										}else if(controlbuf == 1){
											control = "0042";
										}
									}catch(Exception e){
										System.out.println("控制方式赋值有误！");
									}
									
									
									String wpsstr = "7E3501010152" + mach + channel + "001e0001006400be0000" + ele + vol + "0000006400be000000010000" + gas + diameter + material + "00" + control + eletuny + voltuny + "00000000000000000000247D";
									String wpsreturn = "{type:\"wpsreturn\",result:\"0\"}";
									synchronized (socketlist) {
										ArrayList<String> listarraybuf = new ArrayList<String>();
										boolean ifdo= false;

										Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
										while(webiter.hasNext()){
											try{
												Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
												websocketfail = entry.getKey();
												SocketChannel websocketcon = entry.getValue();
												if(websocketcon.remoteAddress().equals(ctx.channel().remoteAddress())) {
													websocketcon.writeAndFlush(wpsstr).sync();
													Thread.sleep(1000);
													websocketcon.writeAndFlush(wpsreturn).sync();
												}else {
													Thread.sleep(1000);
													websocketcon.writeAndFlush(wpsstr).sync();
												}
											}catch (Exception e) {
												listarraybuf.add(websocketfail);
												ifdo = true;
											}
										}

										if(ifdo){
											for(int i1=0;i1<listarraybuf.size();i1++){
												socketlist.remove(listarraybuf.get(i1));
											}
										}
									}
								}

								Thread.sleep(500);
							}

						}else if(strjson.getString("type").equals("taskdown")){
							String wpsstr = "";
							String junctionsend = "";
							String cengdao = "";
							int cengdaocount = 0;

							String mach = Integer.toHexString(Integer.valueOf(strjson.getString("mach")));
							if(mach.length() != 4){
								int count = mach.length();
								for(int i=0;i<4-count;i++){
									mach = "0" + mach;
								}
							}

							String task = strjson.getString("task");

							String taskstatus = strjson.getString("taskstatus");
							if(taskstatus.length() != 2){
								int count = taskstatus.length();
								for(int i=0;i<2-count;i++){
									taskstatus = "0" + taskstatus;
								}
							}

							//层道信息
							JSONArray strarray = JSONArray.parseArray(strjson.getString("data"));
							for(int i=0;i<strarray.size();i++){
								String layer = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("layer")));
								if(layer.length()!=2){
									layer = "0" + layer;
								}
								String avenue = Integer.toHexString(Integer.valueOf(strarray.getJSONObject(i).getString("avenue")));
								if(avenue.length()!=2){
									avenue = "0" + avenue;
								}
								cengdao = cengdao + layer + avenue;
								cengdaocount++;
							}

							if(cengdao.length() != 200){
								int count = cengdao.length();
								for(int i=0;i<200-count;i++){
									cengdao = cengdao + "0";
								}
							}

							String cdcount = Integer.toHexString(cengdaocount);
							if(cdcount.length() != 4){
								int count = cdcount.length();
								for(int i=0;i<4-count;i++){
									cdcount = "0" + cdcount;
								}
							}

							//任务名转换char
							char[] buf = task.toCharArray();
							for(int i=0;i<buf.length;i++){
								int buf1 = buf[i];
								junctionsend = junctionsend + Integer.toString(buf1,16);
							}


							if(junctionsend.length() != 60){
								int count = junctionsend.length();
								for(int i=0;i<60-count;i++){
									junctionsend = junctionsend + "0";
								}
							}

							String taskstr = "7E8D01010102" + mach + "00" + junctionsend + cengdao + cdcount + "017D";
							String taskreturn = "{type:\"taskreturn\",result:\"0\"}";

							synchronized (socketlist) {
								ArrayList<String> listarraybuf = new ArrayList<String>();
								boolean ifdo= false;

								Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
								while(webiter.hasNext()){
									try{
										Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
										websocketfail = entry.getKey();
										SocketChannel websocketcon = entry.getValue();
										if(websocketcon.remoteAddress().equals(ctx.channel().remoteAddress())) {
											websocketcon.writeAndFlush(taskstr).sync();
											Thread.sleep(1000);
											websocketcon.writeAndFlush(taskreturn).sync();
										}else {
											Thread.sleep(1000);
											websocketcon.writeAndFlush(taskstr).sync();
										}
									}catch (Exception e) {
										listarraybuf.add(websocketfail);
										ifdo = true;
									}
								}

								if(ifdo){
									for(int i1=0;i1<listarraybuf.size();i1++){
										socketlist.remove(listarraybuf.get(i1));
									}
								}
							}
						}

					}else if(str.substring(0,2).equals("7E") && (str.length()==38)){
						String mach = Integer.toString(Integer.valueOf(str.substring(12,16), 16));
						String status = Integer.toString(Integer.valueOf(str.substring(16,18), 16));
						String layer = Integer.toString(Integer.valueOf(str.substring(18,20), 16));
						String avenue = Integer.toString(Integer.valueOf(str.substring(20,22), 16));

						String wpsstr = "{mach:\""+mach+"\",type:\"statusup\",status:\""+status+"\",layer:\""+layer+"\",avenue:\""+avenue+"\"}";

						try{
							synchronized (socketlist) {
								ArrayList<String> listarraybuf = new ArrayList<String>();
								boolean ifdo= false;

								Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
								while(webiter.hasNext()){
									try{
										Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
										websocketfail = entry.getKey();
										SocketChannel websocketcon = entry.getValue();

										if(websocketcon.remoteAddress().equals(ctx.channel().remoteAddress())) {
										}else {
											Thread.sleep(1000);
											websocketcon.writeAndFlush(wpsstr).sync();
										}
									}catch (Exception e) {
										listarraybuf.add(websocketfail);
										ifdo = true;
									}
								}

								if(ifdo){
									for(int i1=0;i1<listarraybuf.size();i1++){
										socketlist.remove(listarraybuf.get(i1));
									}
								}
							}
						}catch(Exception e){
							//e.printStackTrace();
						}
					}

				} catch(Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
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
