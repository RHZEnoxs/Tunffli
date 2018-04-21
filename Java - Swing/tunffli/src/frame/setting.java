package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import main.index;

import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class setting extends JFrame {

	private JPanel contentPane;
	private JTextField edt_host;
	private JTextField edt_port;
	private JTextField edt_username;
	private JTextField edt_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					setting frame = new setting();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	JButton btn_link;
	public setting() {
		super("Tunffli");
		setSize(385,410);
		setLocationRelativeTo(null);
		setResizable(false);
		//setIconImage(index.icon.getImage());
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#111111"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel side_pan = new JPanel();
		side_pan.setBorder(new LineBorder(Color.decode("#EB961C"), 3, true));
		side_pan.setBackground(Color.decode("#1D1F21"));
		side_pan.setBounds(15, 15, 350, 280);
		contentPane.add(side_pan);
		side_pan.setLayout(null);
		
		JLabel text_title = new JLabel("Host IP");
		text_title.setBounds(15, 15, 320, 37);
		side_pan.add(text_title);
		text_title.setHorizontalAlignment(SwingConstants.CENTER);
		text_title.setForeground(Color.decode("#EB961C"));
		text_title.setFont(new Font("標楷體", Font.BOLD, 30));
		
		JLabel text_host = new JLabel("Host:");
		text_host.setBounds(15, 60, 85, 36);
		side_pan.add(text_host);
		text_host.setHorizontalAlignment(SwingConstants.LEFT);
		text_host.setForeground(Color.decode("#FBED7E"));//#FBED7E
		text_host.setFont(new Font("標楷體", Font.BOLD, 30));
		
		JLabel text_port = new JLabel("Port:");
		text_port.setHorizontalAlignment(SwingConstants.LEFT);
		text_port.setForeground(new Color(251, 237, 126));
		text_port.setFont(new Font("標楷體", Font.BOLD, 30));
		text_port.setBounds(15, 110, 85, 36);
		side_pan.add(text_port);
		
		JLabel text_username = new JLabel("Username:");
		text_username.setHorizontalAlignment(SwingConstants.LEFT);
		text_username.setForeground(new Color(251, 237, 126));
		text_username.setFont(new Font("標楷體", Font.BOLD, 25));
		text_username.setBounds(15, 178, 130, 36);
		side_pan.add(text_username);
		
		JLabel text_password = new JLabel("Password:");
		text_password.setHorizontalAlignment(SwingConstants.LEFT);
		text_password.setForeground(new Color(251, 237, 126));
		text_password.setFont(new Font("標楷體", Font.BOLD, 25));
		text_password.setBounds(15, 229, 130, 36);
		side_pan.add(text_password);
		
		edt_host = new JTextField();
		edt_host.setFont(new Font("Arial", Font.BOLD, 25));
		edt_host.setForeground(Color.decode("#FBFFFB"));
		edt_host.setBackground(Color.decode("#111111"));
		edt_host.setBounds(110, 67, 225, 30);
		side_pan.add(edt_host);
		edt_host.setColumns(25);
		
		edt_port = new JTextField();
		edt_port.setText("1883");
		edt_port.setFont(new Font("Arial", Font.BOLD, 25));
		edt_port.setForeground(Color.decode("#FBFFFB"));
		edt_port.setBackground(Color.decode("#111111"));
		edt_port.setColumns(25);
		edt_port.setBounds(110, 110, 225, 30);
		side_pan.add(edt_port);
		
		edt_username = new JTextField();
		edt_username.setFont(new Font("Arial", Font.BOLD, 25));
		edt_username.setForeground(Color.decode("#FBFFFB"));
		edt_username.setBackground(Color.decode("#111111"));
		edt_username.setColumns(25);
		edt_username.setBounds(142, 180, 180, 30);
		side_pan.add(edt_username);
		
		edt_password = new JPasswordField();
		edt_password.setFont(new Font("Arial", Font.BOLD, 25));
		edt_password.setForeground(Color.decode("#FBFFFB"));
		edt_password.setBackground(Color.decode("#111111"));
		edt_password.setColumns(25);
		edt_password.setBounds(142, 231, 180, 30);
		side_pan.add(edt_password);
		
		JPanel hr_pan = new JPanel();
		hr_pan.setBounds(15, 155, 325, 3);
		side_pan.add(hr_pan);
		hr_pan.setBackground(Color.decode("#EB961C"));
		
		btn_link = new JButton("LINK");
		btn_link.setFont(new Font("Arial Black", Font.BOLD, 30));
		btn_link.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(index.mqtt.link_flag){
					index.mqtt.disconnect();
				}else{
					boolean flag = index.mqtt.connect(edt_host.getText(),edt_port.getText(),edt_username.getText(),edt_password.getText());
					if(flag){
						index.mPrefer.setLink_flag(flag);
						index.mPrefer.setHost(edt_host.getText());
						index.mPrefer.setPort(edt_port.getText());
						index.mPrefer.setUsername(edt_port.getText());
						index.mPrefer.setPassword(edt_password.getText());
						index.init_status();
					}
				}
			}
		});
		btn_link.setBackground(Color.decode("#EB961C"));
		btn_link.setForeground(Color.decode("#FBFFFB"));
		btn_link.setBounds(15, 310, 350, 36);
		contentPane.add(btn_link);
		// --- init * ---
		edt_host.setText(index.mPrefer.getHost());
		edt_port.setText(index.mPrefer.getPort());
		edt_username.setText(index.mPrefer.getUsername());
		edt_password.setText(index.mPrefer.getPassword());
	}
	public void reback(boolean flag){
		if(flag){
			btn_link.setText("UNLINK");
		}else{
			btn_link.setText("LINK");
		}
	}
}
