package hoa.vku.udn.vn;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;


import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JCheckBox;

public class ClientChatter extends JFrame {

	private JPanel contentPane;
	private JTextField txtUser;
	private JTextField txtServerHost;
	private JTextField txtPort;
	private JPasswordField passwordField;
	private JButton btnConnect;

	
	Socket mngSocket = null;
	String mngServer = "";
	int mngPort = 0;
	String staffName = "";
	BufferedReader bf = null;
	DataOutputStream os = null;
	OutputThread t = null;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChatter frame = new ClientChatter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientChatter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 829, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Staff and server info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 7, 5, 0));
		
		JLabel lblNewLabel = new JLabel("User name:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(lblNewLabel);	
		
		txtUser = new JTextField();
		txtUser.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(txtUser);
		txtUser.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Password:");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(lblNewLabel_3);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(passwordField);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("");
		chckbxNewCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxNewCheckBox.isSelected()){
				     passwordField.setEchoChar((char)0);
				    }else{				    
				     passwordField.setEchoChar('*');
				    }
				   }
				  });
		panel.add(chckbxNewCheckBox);
				
		JLabel lblNewLabel_1 = new JLabel("Server Host:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel_1);
		
		txtServerHost = new JTextField();
		txtServerHost.setText("localhost");
		txtServerHost.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(txtServerHost);
		txtServerHost.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Port:");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(lblNewLabel_2);
		
		txtPort = new JTextField();
		txtPort.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(txtPort);
		txtPort.setColumns(10);
		
		JFrame thisFrame = this;
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tenTaiKhoan = txtUser.getText();
                String matKhau = passwordField.getText();
				mngServer = txtServerHost.getText();
				mngPort = Integer.parseInt(txtPort.getText());
				staffName = txtUser.getText();
				try {
                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/swing_demo",
                        "root", "Hoa30025091");

                    PreparedStatement st = (PreparedStatement) connection
                        .prepareStatement("Select name, password from client_info where name=? and password=?");

                    st.setString(1, tenTaiKhoan);
                    st.setString(2, matKhau);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                    	
                        ClientChatter frame = new ClientChatter();                      
                        
                        mngSocket = new Socket(mngServer, mngPort);
    					if (mngSocket != null) {
    						ChatPanel p = new ChatPanel(mngSocket, staffName, "Manager");
    						thisFrame.getContentPane().add(p);
    						p.getTextMessages().append("Manager is running");
    						p.updateUI();
    						
    						bf = new BufferedReader(new InputStreamReader(mngSocket.getInputStream()));
    						os = new DataOutputStream(mngSocket.getOutputStream());
    						
    						os.writeBytes("Staff:" + staffName);
    						os.write(13);
    						os.flush();
    						JOptionPane.showMessageDialog(btnConnect, "Successful connect");
    					}
                    } else {
                        JOptionPane.showMessageDialog(btnConnect, "User name or password invalid");
                    }
                							
				} catch (Exception e2) {
					e2.printStackTrace();
					
				}					
			}
		});
		btnConnect.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(btnConnect);
	}

}






