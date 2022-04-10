package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientHandler extends Thread{
	Server server;
	Socket socket;
	BufferedReader input;
	DataOutputStream output;
	public boolean running=true;
	ClientHandler(Socket skt, Server srv) throws IOException{
		server=srv;
		socket=skt;
		input = new BufferedReader(new InputStreamReader(skt.getInputStream()));
		output = new DataOutputStream(skt.getOutputStream());	
		this.start();
		sendMessage("to client");
	}
	public void run() {
		while(running) {
			try {
				System.out.println("waiting");
				String message=input.readLine();
				System.out.println("CH: got message:"+message);
				server.messageRecive(message);
			} catch (IOException e) {
				e.printStackTrace();
				running=false;
			}
		}
	}
	void sendMessage(String s) throws IOException {
		output.writeBytes(s + "\n\r");
        output.flush();
	}
}
