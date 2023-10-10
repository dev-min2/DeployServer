package TestServerPack;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Main {
	private static ServerSock sock = null;
	private static List<String> commandList = null;
	
	private static void init() {
		commandList = new ArrayList<String>( 10 );
		
		Document xml = null;
	    try {
	    	sock = new ServerSock(9999);
			sock.init();
			

			String accessXmlPath = "resource/commandList.xml";
			
			Path currentRelativePath = Paths.get("");
			String currentAbsoulutePath = currentRelativePath.toAbsolutePath().toString();
			
			String[] splitArray = currentAbsoulutePath.split("\\\\");
			if(splitArray[splitArray.length - 1].equals("bin")) {
				accessXmlPath = "../resource/commandList.xml";
			}
	    	
			InputSource is = new InputSource(new FileReader(accessXmlPath));
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			NodeList childNodelist = xml.getDocumentElement().getChildNodes();

			if(childNodelist.getLength() > 0) {
				for(int nodeIndex = 0; nodeIndex < childNodelist.getLength(); nodeIndex++) {
					if(childNodelist.item(nodeIndex).getNodeName().equals("command")) {
						String command = childNodelist.item(nodeIndex).getTextContent();
						
						commandList.add(command);     
					}
				}
			}
	    } 
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	private static void processCommand(String command) {
		
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		init();
		
		while(true) {	
			String recvStr = sock.recvData();
			System.out.println(recvStr + " Command ����");
			sock.sendData(recvStr);
			
			if(recvStr.equals("4"))
				break;
			
			Thread.currentThread().sleep(1);
		}
	}
}
