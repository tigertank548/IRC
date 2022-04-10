package client;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Client extends JFrame implements ActionListener,Runnable{
	public Socket socket;
	public JTextField textField;
	PrintStream output;
	BufferedReader input;
	String username="user";
	boolean running;
	public Client(String host,int port) throws UnknownHostException, IOException{
		super("IRC");
		makeWindow();
		connectTo(host,port);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintStream(socket.getOutputStream());
		System.out.println("connected");
		new Thread(this).start();
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==textField) {
			String message="<"+username+"> "+textField.getText();
			System.out.println("seen:"+message);
			output.print(message);
			textField.setText("");
		}
	}
	public void connectTo(String host,int port) throws UnknownHostException, IOException {
		socket = new Socket(host,port);
	}
	public void makeWindow() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(500,30));
		textField.addActionListener(this);
		
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
		this.add(textField);
		this.pack();
		this.setVisible(true);
		this.setSize(600,420);
	}
	
	@Override
	public void run() {
		while(running) {
			try {
				System.out.println(input.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new Client("localhost",9999);
		} catch (UnknownHostException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
	}
}
