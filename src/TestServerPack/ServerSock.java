package TestServerPack;

import java.io.*;
import java.net.*;

// SyncSock
public class ServerSock implements Closeable {
	private ServerSocket serverSock;
	private Socket clientSock; 
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

		
		clientSock = serverSock.accept();
		System.out.println("Client : " +clientSock.getRemoteSocketAddress() + "����\n");
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
