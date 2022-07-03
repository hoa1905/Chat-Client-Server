package hoa.vku.udn.vn;


import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class ChatPanel extends JPanel {
	
	Socket socket = null;
	BufferedReader bf = null;
	DataOutputStream os = null;
	OutputThread t = null;
	String sender;
	String receiver;
	JTextArea txtMessages;
	

	/**
	 * Create the panel.
	 */
	public ChatPanel(Socket s, String sender, String receiver) {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Message", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 2, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		JTextArea txtMessage = new JTextArea();
		txtMessage.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		scrollPane.setViewportView(txtMessage);
		
		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtMessages.getText().trim().length() == 0)
					return;
				try {
					os.writeBytes(txtMessage.getText());
					os.write(13);
					os.write(10);
					os.flush();
					txtMessages.append("\n" + sender + ": " + txtMessage.getText());
					txtMessage.setText("");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		panel.add(btnSend);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, BorderLayout.CENTER);
		
		txtMessages = new JTextArea();
		txtMessages.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		scrollPane_1.setViewportView(txtMessages);
		
		socket = s;
		this.sender = sender;
		this.receiver = receiver;
		try {
			bf= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new DataOutputStream(socket.getOutputStream());
			t = new OutputThread(s, txtMessages, sender, receiver);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public JTextArea getTextMessages() {
		return this.txtMessages;
	}

}

