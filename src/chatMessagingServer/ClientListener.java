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
import java.net.Socket;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;




public class ClientListener implements Runnable {

	
	Server server;
	private Socket socket;
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
		while (true) {
			String line = "";
			if (readFile) {
				
				DataInputStream dis = null;
				try {
					dis = new DataInputStream(this.socket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream("images/" + fileName );
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				byte[] buffer = new byte[4096];
				
				int filesize = 15123; // Send file size in separate msg
				int read = 0;
				int totalRead = 0;
				int remaining = filesize;
				try {
					while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
						totalRead += read;
						remaining -= read;
						System.out.println("read " + totalRead + " bytes.");
						fos.write(buffer, 0, read);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					fos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					dis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
				
			} else {
					line = in.nextLine();
					System.out.println("Server Recieved: " + line);
					if(line.substring(0, 5).equals("TEXT:")) {
						Message msg = new Message(line);
						this.server.broadCast(msg, this.socketNumber);
					} else {
						readFile = true;
						fileName = line;
					}
				}
				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}

}
}
