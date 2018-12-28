import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class IOSthread implements Runnable{

	public String connet;
	public ArrayList<String> listarray2;
	public Socket socket;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			InputStream input = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(input);
            int n = 0;
            String data = "";
    		System.out.println("开始接收安卓数据");
    		
            while ( (n = bis.read()) != 255 || (n = bis.read()) != -1){
            	if(Integer.toHexString(n).length() == 1){
                	data = data + "0" + Integer.toHexString(n);
            	}else if(Integer.toHexString(n).length() == 2){
            		data = data + Integer.toHexString(n);
            	}
            }
            if(!data.equals("")){
            	Android android = new Android(listarray2,connet);
            	android.Androidrun(data.toUpperCase());
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IOSthread() {
		// TODO Auto-generated constructor stub
	}


}
