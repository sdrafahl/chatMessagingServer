package chatMessagingServer;

public class Message {
	
	String name;
	String message;
	
	public Message(String input) {
		input = input.substring(5);
		boolean name = true;
		this.message = "";
		this.name = "";
		for(int x=0;x<input.length();x++) {
			if(input.charAt(x) == '|') {
				name = false;
			} else {
				if(name) {
					this.name+=input.charAt(x);
				} else {
					this.message+=input.charAt(x);
				}
			}
			
		}
	}

}