import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Relay extends JFrame implements ActionListener{
	public static void main(String[] args) {
		new Relay();
	}
	JTextField textField;

	Relay(){
		super("IRC");
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
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==textField) {
			System.out.println("<user>"+textField.getText());
			textField.setText("");
		}
	}
}
