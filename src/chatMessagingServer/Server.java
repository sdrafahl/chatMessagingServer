package chatMessagingServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	
	static int numberOfClients = 0;
	
	ArrayList<ClientSender> clients = new ArrayList<ClientSender>();

	public static void main(String[] args) {
		
		Server server = new Server();
		
		System.out.println("Serving is Starting");
		
		Config conf = Config.getInstance();
		
		System.out.println("The port number is: " + conf.getPortNumber());
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(conf.getPortNumber());
			
			System.out.println(serverSocket);
			
		} catch (IOException e) {
			System.out.println("Could not listen on port:" + conf.getPortNumber());
			System.exit(-1);
		}
		
		while(true) {
			Socket clientSocket = null;
			try {
				System.out.println("Waiting for client " + (numberOfClients));
				clientSocket = serverSocket.accept();
				ClientSender client = new ClientSender(server, clientSocket, numberOfClients);
				new Thread(client).start();
				new Thread(new ClientListener(server, clientSocket, numberOfClients)).start();;
				server.clients.add(client);
				numberOfClients++;
			}  catch (IOException e) {
				System.out.println("Accept failed: " + conf.getPortNumber());
				System.exit(-1);
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
		for(int x=0;x<this.clients.size();x++) {
			if(this.clients.get(x).getSocketNumber() != socketNumberFrom) {
				this.clients.get(x).outGoing = msg;
			}
		}
	}
}
