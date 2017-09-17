package chatMessagingServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	
	static int numberOfClients = 0;
	
	ArrayList<ClientSender> clientSenders = new ArrayList<ClientSender>();
	ArrayList<ClientListener> clientListeners = new ArrayList<ClientListener>();
	
	ServerSocket serverSocket;
	
	public Server() {
		
		try {
			Config conf = Config.getInstance();
			serverSocket = new ServerSocket();
			InetSocketAddress addr = new InetSocketAddress(conf.getPortNumber());
			serverSocket.setReuseAddress(true);
			serverSocket.setSoTimeout(0);
			serverSocket.bind(addr);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		
		System.out.println("Serving is Starting");
		
		Config conf = Config.getInstance();
		
		System.out.println("The port number is: " + conf.getPortNumber());
		
		System.out.println(server.serverSocket);
		
		while(true) {
			Socket clientSocket = null;
			try {
				System.out.println("Waiting for client " + (numberOfClients));
				clientSocket = server.serverSocket.accept();
				ClientSender client = new ClientSender(server, clientSocket, numberOfClients);
				ClientListener listener = new ClientListener(server, clientSocket, numberOfClients);
				server.clientSenders.add(client);
				server.clientListeners.add(listener);
				new Thread(client).start();
				new Thread(listener).start();;
				numberOfClients++;
			}  catch (IOException e) {
				System.out.println("Accept failed: " + conf.getPortNumber());
				System.exit(-1);
			}
		}
	}
	
	public synchronized void broadCastFile(String fileName, int socket) {
		for(int x=0;x<this.clientSenders.size();x++) {
			if(this.clientSenders.get(x).getSocketNumber() != socket) {
				this.clientSenders.get(x).fileName = fileName;
			}
		}
	}
	
	public synchronized void broadCast(Message msg, int socketNumberFrom) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int x=0;x<this.clientSenders.size();x++) {
			if(this.clientSenders.get(x).getSocketNumber() != socketNumberFrom) {
				this.clientSenders.get(x).outGoing = msg;
			}
		}
	}
	
	public synchronized void broadCastString(String message, int socketNumberFrom) {
		Message msg = new Message();
		msg.name = "";
		msg.message = message;
		for(int x=0;x<this.clientSenders.size();x++) {
			if(this.clientSenders.get(x).getSocketNumber() != socketNumberFrom) {
				this.clientSenders.get(x).outGoing = msg;
			}
		}
		
	}
}
