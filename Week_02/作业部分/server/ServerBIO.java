package cn.zs.mstpxu.io.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerBIO {
	private final static int Def_CorePool_Size = 50;
	private final static long Def_KeepAlived_Time = 200L;
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8081);
		ExecutorService service = new ThreadPoolExecutor(0,Def_CorePool_Size,Def_KeepAlived_Time, 
														TimeUnit.MILLISECONDS,
														new LinkedBlockingQueue<Runnable>(50));
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				service.execute(()->service(socket));
			}catch(IOException e) {
				
			}
		}
		
	}

	private static void service(Socket socket) {
		try{
			PrintWriter pWriter = new PrintWriter(socket.getOutputStream(), true);
			pWriter.println("HTTP/1.1 200 OK");
			pWriter.println("Content-Type:text/html;charset=utf-8");
			pWriter.println();
			pWriter.write("hello world");
			
			pWriter.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
