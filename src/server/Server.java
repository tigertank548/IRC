package server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Server extends JFrame implements ActionListener{
	ServerSocket server;
	Accepter accepter;
	public ArrayList<ClientHandler> clientHandlers=new ArrayList<ClientHandler>();
	JButton button;
	
	Server(int port) throws IOException{
		super("IRC Server");
		ServerSocket server = new ServerSocket(port);
		makeWindow();
		System.out.println("Server is waiting for an incoming connection on host= " + InetAddress.getLocalHost().getCanonicalHostName() + " port= " + server.getLocalPort());
		accepter=new Accepter(true, server,this);
		
	}
	
	public void add(Socket s) throws IOException {
		clientHandlers.add(new ClientHandler(s,this));
		System.out.println("connected");
	}
	
	void stop() throws IOException {
		accepter.running=false;
		for(ClientHandler cH:clientHandlers) 
			cH.running=false;
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));		
	}
	
	void messageRecive(String message) throws IOException {
		System.out.println(message);
		for(ClientHandler cH:clientHandlers)
			cH.sendMessage(message);
	}
	
	void makeWindow() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(500,30));
		
		button=new JButton("Stop");
		button.addActionListener(this);
		
		JTextArea textArea = new JTextArea(20,50);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		System.setOut(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				textArea.append(String.valueOf((char) b));
			}
		}));
		
		this.add(textArea);
		this.add(button);
		this.pack();
		this.setVisible(true);
		this.setSize(600,420);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button) {
			try {
				stop();
			} catch (IOException e1) {e1.printStackTrace();}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Server(9999);
	}
}
