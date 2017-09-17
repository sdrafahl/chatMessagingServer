package chatMessagingServer;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ImageReader implements Runnable {

	public String fileNameBuffer;
	private Socket socket;
	public Server server;
	int socketNumber;
	
	public ImageReader(Socket socket, Server server, int socketNumber) {
		this.fileNameBuffer = "";
		this.socketNumber = socketNumber;
		this.socket = socket;
		this.server = server;
	}
	
	@Override
	public void run() {
		
		while(true) {
			if (!fileNameBuffer.isEmpty()) {
				DataInputStream dis = null;
				try {
					dis = new DataInputStream(this.socket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream("images/" + this.fileNameBuffer);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				String name = "";
				char buf;
				int marker = 0;
				int counter = 0;
				Chat c = Chat.getInstance();
				c.append(this.fileNameBuffer);
				int filesize = 8192;
				byte[] buffer = new byte[filesize];
				int read = 0;
				int totalRead = 0;
				int remaining = filesize;
				this.server.broadCastFile(this.fileNameBuffer, this.socketNumber);
				this.fileNameBuffer = "";
				try {
					fos.flush();
						while((read = dis.read(buffer, 0, filesize)) != -1) {
							totalRead += read;
							remaining -= read;
							System.out.println("read " + totalRead + " bytes.");
							fos.write(buffer, 0, read);
						}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	}
}
