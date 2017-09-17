package chatMessagingServer;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class ClientSender implements Runnable  {
	
	String fileName;
	Server server;
	Socket socket;
	private int socketNumber;
	private Scanner in;
	private PrintWriter writer;
	Message outGoing;
	
	public ClientSender(Server server, Socket socket, int clientNum) {
		this.socket = socket;
		this.socketNumber = clientNum;
		this.server = server;
		fileName = null;
	}
	
	public int getSocketNumber(){
		return this.socketNumber;
	}
	
public void sendImage(String fileName) {
		
		PrintWriter printOut = null;
		try {
			printOut = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		printOut.println("I" + fileName);
		printOut.flush();
		File file = new File("images/" + fileName);
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buffer = new byte[10000];
		
		try {
			while (fis.read(buffer) > 0) {
				dos.write(buffer);
			}
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				this.writer = new PrintWriter(new BufferedOutputStream(this.socket.getOutputStream()));
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
			
			if(fileName != null) {
				sendImage(this.fileName);
				fileName = null;
			}
		}
		
	}

}
