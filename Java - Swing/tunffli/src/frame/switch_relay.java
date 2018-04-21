package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import main.index;

public class switch_relay extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					switch_relay frame = new switch_relay();
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
	public switch_relay() {
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
		side_pan.setBounds(15, 15, 350, 340);
		side_pan.setLayout(null);
		contentPane.add(side_pan);
		
		JButton btn_x1 = new JButton("0x01");
		btn_x1.setBounds(15, 15, 320, 50);
		side_pan.add(btn_x1);
		btn_x1.setFont(new Font("Arial Black", Font.BOLD, 30));
		btn_x1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrl_command(1);
			}
		});
		btn_x1.setBackground(Color.decode("#EB961C"));
		btn_x1.setForeground(Color.decode("#FBFFFB"));
		
		JButton btn_x2 = new JButton("0x02");
		btn_x2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrl_command(2);
			}
		});
		btn_x2.setForeground(new Color(251, 255, 251));
		btn_x2.setFont(new Font("Arial Black", Font.BOLD, 30));
		btn_x2.setBackground(new Color(235, 150, 28));
		btn_x2.setBounds(15, 80, 320, 50);
		side_pan.add(btn_x2);
		
		JButton btn_x3 = new JButton("0x03");
		btn_x3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl_command(3);
			}
		});
		btn_x3.setForeground(new Color(251, 255, 251));
		btn_x3.setFont(new Font("Arial Black", Font.BOLD, 30));
		btn_x3.setBackground(new Color(235, 150, 28));
		btn_x3.setBounds(15, 145, 320, 50);
		side_pan.add(btn_x3);
		
		JButton btnx_4 = new JButton("0x04");
		btnx_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl_command(4);
			}
		});
		btnx_4.setForeground(new Color(251, 255, 251));
		btnx_4.setFont(new Font("Arial Black", Font.BOLD, 30));
		btnx_4.setBackground(new Color(235, 150, 28));
		btnx_4.setBounds(15, 210, 320, 50);
		side_pan.add(btnx_4);
		
		JButton btnx_5 = new JButton("0x05");
		btnx_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl_command(5);
			}
		});
		btnx_5.setForeground(new Color(251, 255, 251));
		btnx_5.setFont(new Font("Arial Black", Font.BOLD, 30));
		btnx_5.setBackground(new Color(235, 150, 28));
		btnx_5.setBounds(15, 275, 320, 50);
		side_pan.add(btnx_5);
	}
	public void  ctrl_command(int which){
		JSONObject obj = new JSONObject();
        obj.put("id", "Relay");
        obj.put("reply",false);
        obj.put("dev_id","0x0"+which);
        obj.put("value",true);
        index.mqtt.publish_message(obj.toString());
	}

}
