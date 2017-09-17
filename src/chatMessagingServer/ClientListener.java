package chatMessagingServer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ClientListener implements Runnable {

	
	Server server;
	Socket socket;
	private int socketNumber;
	private Scanner in;
	private PrintWriter writer;
	Message outGoing;
	
	public ClientListener(Server server, Socket socket, int clientNum) {
		this.socket = socket;
		this.socketNumber = clientNum;
		this.server = server;
	}
	
	public int getSocketNumber(){
		return this.socketNumber;
	}
	
	
	@Override
	public void run() {
		try {
			this.in = new Scanner(new BufferedInputStream(this.socket.getInputStream()));
		} catch (IOException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		boolean readFile = false;
		String fileName = "";
		String line = "";
		while (true) {
			if(readFile) {
				readFile = false;
				ImageReader reader = new ImageReader(socket, server, socketNumber);
				new Thread(reader).start();
				reader.fileNameBuffer = fileName;
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.server.broadCastString(fileName, this.socketNumber);
				this.server.broadCastFile(fileName, socketNumber);
			} else {
					if(in.hasNextLine()) {
						line = in.nextLine();
						System.out.println("Server Recieved: " + line);
						if(line.length() > 4 && line.substring(0, 5).equals("TEXT:")) {
							Message msg = new Message(line);
							Chat chat = Chat.getInstance();
							chat.append(msg.name + " " + msg.message);
							this.server.broadCast(msg, this.socketNumber);
						} else {
							if(line.length() > 0 && line.charAt(0) == 'I') {
								readFile = true;
								fileName = line.substring(1);
							}
						}
					}
					
			}
	}

}
}
