package chatMessagingServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Config {
	
	private static Config config = new Config();
	
	private final String FILE = "config";
	
	private int portNumber;
	
	private Config() {
		try {
			FileReader fr  = new FileReader(FILE);
			BufferedReader br = new BufferedReader(fr);
			
			String current = null;
			while((current = br.readLine()) != null) {
				readLine(current);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readLine(String line) {
		String buffer = "";
		boolean port = false;
		for(int x=0;x<line.length();x++) {
			buffer += line.charAt(x);
			if(buffer.equals("Port:")) {
				buffer = "";
				port = true;
			}
		}
		if(port) {
			this.portNumber = Integer.parseInt(buffer.trim());
		}
	}
	
	public int getPortNumber() {
		return this.portNumber;
	}
	
	public static Config getInstance() {
		return config;
	}
	
}
