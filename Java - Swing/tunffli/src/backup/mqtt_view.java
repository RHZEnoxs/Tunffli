package backup;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;

public class mqtt_view extends JFrame {

	private JPanel contentPane;
	private JTextField edt_msg;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mqtt_view frame = new mqtt_view();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	JLabel txt_msg;
	JFrame myFrame;
	public mqtt_view() {
		myFrame = this;
		mqtt_sample mqtt = new mqtt_sample(0,myFrame);
		mqtt.connect();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 853, 759);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btn_link = new JButton("Connect");
		btn_link.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_link.setBounds(15, 32, 111, 31);
		contentPane.add(btn_link);
		
		JButton btn_unlink = new JButton("Disconnect");
		btn_unlink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btn_unlink.setBounds(154, 32, 111, 31);
		contentPane.add(btn_unlink);
		
		txt_msg = new JLabel("message");
		txt_msg.setFont(new Font("²Ó©úÅé", Font.PLAIN, 25));
		txt_msg.setBounds(15, 111, 345, 147);
		contentPane.add(txt_msg);
		
		edt_msg = new JTextField();
		edt_msg.setBounds(15, 578, 392, 29);
		contentPane.add(edt_msg);
		edt_msg.setColumns(10);
		
		JButton btn_publish = new JButton("publish");
		btn_publish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mqtt.publish_message();
			}
		});
		btn_publish.setBounds(467, 577, 111, 31);
		contentPane.add(btn_publish);
	}
	public void reback(int id,String topic,String msg){
		txt_msg.setText(topic + "\n" + msg);
	}
}
