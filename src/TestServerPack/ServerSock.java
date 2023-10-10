package TestServerPack;

import java.io.*;
import java.net.*;

// SyncSock
public class ServerSock implements Closeable {
	private ServerSocket serverSock;
	private Socket clientSock; // 연결된 클라이언트 소켓. 1개만 받는다.
	private InputStream clientInputStream;
    private OutputStream clientOutputStream;
	
	ServerSock(int port) throws IOException {
		serverSock = new ServerSocket(port);
		
		clientSock = null;
		clientInputStream = null;
		clientOutputStream = null;
	}
	
	public void init() throws IOException {
		if(clientSock != null && clientInputStream != null && clientOutputStream != null)
			return;

		// 먼저 접속대기. 단 한번만 accept처리.
		clientSock = serverSock.accept();
		System.out.println("Client : " +clientSock.getRemoteSocketAddress() + "연결\n");
		clientInputStream = clientSock.getInputStream();
		clientOutputStream = clientSock.getOutputStream();
	}
	
	public boolean sendData(String data) {
		try {
			clientOutputStream.write(data.getBytes());
			clientOutputStream.flush();
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// 덜 수신되는 경우는 없다고 가정 및 에코로 데이터 주고받음. 
	public String recvData() {
		byte[] readBuffer = new byte[100];
		int len = 0;
		try {
			len = clientInputStream.read(readBuffer);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return new String(readBuffer,0,len);
	}
	
	
	@Override
	public void close() throws IOException {
		if(serverSock != null)
			serverSock.close();
		if(clientSock != null) {
			clientSock.close();
			clientInputStream.close();
			clientOutputStream.close();
		}
	}
	
	
}
