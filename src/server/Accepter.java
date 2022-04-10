package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Accepter extends Thread{
	public boolean running;
	public ServerSocket socket;
	Server server;
	public Accepter(boolean r,ServerSocket s,Server server) {
		socket=s;
		running=r;
		this.server=server;
		this.start();
	}
	public void run() {
		while(running) {
			try {
				Socket s=socket.accept();
				server.add(s);
			} catch (IOException e) {}
		}
	}
}
