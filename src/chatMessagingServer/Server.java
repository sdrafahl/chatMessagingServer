package chatMessagingServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	static int numberOfClients = 0;

	public static void main(String[] args) {
		
		System.out.println("Serving is Starting");
		
		Config conf = Config.getInstance();
		
		System.out.println("The port number is: " + conf.getPortNumber());
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(conf.getPortNumber());
			System.out.println(serverSocket);
			
		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
			System.exit(-1);
		}
		
		while(true) {
			Socket clientSocket = null;
			try {
				System.out.println("Waiting for client " + (numberOfClients + 1));
				clientSocket = serverSocket.accept();
				Thread t = new Thread(new ClientHandler(clientSocket, numberOfClients));
				t.start();
				
			}  catch (IOException e) {
				System.out.println("Accept failed: " + conf.getPortNumber());
				System.exit(-1);
			}
		}
	}
	
	

}
