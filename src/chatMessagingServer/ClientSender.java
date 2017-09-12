package chatMessagingServer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSender implements Runnable  {
	
	Server server;
	private Socket s;
	private int socketNumber;
	private Scanner in;
	private PrintWriter writer;
	Message outGoing;
	
	public ClientSender(Server server, Socket socket, int clientNum) {
		this.s = socket;
		this.socketNumber = clientNum;
		this.server = server;
	}
	
	public int getSocketNumber(){
		return this.socketNumber;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				this.writer = new PrintWriter(new BufferedOutputStream(this.s.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(outGoing != null ) {
				this.writer.println("From: " + outGoing.name);
				this.writer.println("Message: " + outGoing.message);
				this.writer.flush();
				outGoing = null;
			}
		}
		
	}

}
