package chatMessagingServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Chat {

	String fileData;
	
	static Chat chat = new Chat();
	
	int lineNumber;
	
	public static Chat getInstance() {
		return chat;
	}
	
	private Chat() {
		lineNumber = 0;
		fileData = "";
		try {
			File fr  = new File("chat.txt");
			Scanner scan = new Scanner(fr);
			
			String current = null;
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				fileData += line;
				fileData += '\n';
				
				String numbe ="";
				int local = 0;
				char buffer;
				while(true) {
					buffer = line.charAt(local);
					if(buffer == ':') {
						lineNumber = Integer.parseInt(numbe);
						break;
					} else {
						local++;
						numbe += buffer;
						buffer = line.charAt(local);
					}
				}
				
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		try {
			PrintWriter writer = new PrintWriter("chat.txt");
			for(int x=0;x<fileData.length();x++) {
				writer.write(fileData.charAt(x));
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void append(String data) {
		this.lineNumber++;
		this.fileData += lineNumber + ": " + data + '\n';
		update();
	}
	
}
