package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import frame.About_Dialog;
import frame.music_madplay;
import frame.setting;
import frame.switch_relay;
import frame.webcam_streamer;

public class index extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					index frame = new index();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static public ImageIcon icon;
	static public mqtt_service mqtt;
	static public Prefer mPrefer;
	static public index frame_main;
	static public music_madplay frame_music;
	static public switch_relay frame_sw_rly;
	static public webcam_streamer frame_webcam;
	static public setting frame_setting;
	public index() {
		super("Tunffli");
		//int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		//int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		setSize(450,420);
		setLocationRelativeTo(null);
		setResizable(false);
		icon = new ImageIcon(index.class.getResource("/images/icon_turn_off.png"));
		setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#111111"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		// --- init *---
		mPrefer = new Prefer();
		frame_main = this;
		frame_sw_rly  = new switch_relay();
		frame_music = new music_madplay();
		frame_webcam = new webcam_streamer();
		frame_setting = new setting();
		mqtt = new mqtt_service(frame_main,frame_sw_rly,frame_music,frame_webcam,frame_setting);
		//index.mPrefer.setLink_flag(false);
		if(index.mPrefer.getLink_flag()){
			boolean flag = index.mqtt.connect(mPrefer.getHost(),mPrefer.getPort(),mPrefer.getUsername(),mPrefer.getPassword());
			if(flag){
				//init_status();
			}
		}
		JButton btn_sw_rly = new JButton("");
		btn_sw_rly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame_sw_rly.setVisible(true);	
			}
		});
		btn_sw_rly.setForeground(Color.decode("#EB961C"));
		btn_sw_rly.setBackground(Color.decode("#1D1F21"));
		btn_sw_rly.setIcon(new ImageIcon(index.class.getResource("/images/flask_power.png")));
		btn_sw_rly.setBounds(15, 15, 128, 128);
		contentPane.add(btn_sw_rly);
		
		JButton btn_music = new JButton("");
		btn_music.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame_music.setVisible(true);
			}
		});
		btn_music.setIcon(new ImageIcon(index.class.getResource("/images/flask_music.png")));
		btn_music.setForeground(new Color(235, 150, 28));
		btn_music.setBackground(new Color(29, 31, 33));
		btn_music.setBounds(158, 15, 128, 128);
		contentPane.add(btn_music);
		
		JButton btn_webcam = new JButton("");
		btn_webcam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame_webcam.setVisible(true);
			}
		});
		btn_webcam.setIcon(new ImageIcon(index.class.getResource("/images/flask_camer.png")));
		btn_webcam.setForeground(new Color(235, 150, 28));
		btn_webcam.setBackground(new Color(29, 31, 33));
		btn_webcam.setBounds(301, 15, 128, 128);
		contentPane.add(btn_webcam);
		
		JButton btn_setting = new JButton("");
		btn_setting.setIcon(new ImageIcon(index.class.getResource("/images/flask_setting.png")));
		btn_setting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame_setting.setVisible(true);
			}
		});
		btn_setting.setForeground(new Color(235, 150, 28));
		btn_setting.setBackground(new Color(29, 31, 33));
		btn_setting.setBounds(15, 175, 128, 128);
		contentPane.add(btn_setting);
		
		JPanel hr_pan = new JPanel();
		hr_pan.setBackground(Color.decode("#EB961C"));
		hr_pan.setBounds(15, 158, 414, 2);
		contentPane.add(hr_pan);
		
		JButton btn_about = new JButton("");
		btn_about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About_Dialog dialog = new About_Dialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		btn_about.setIcon(new ImageIcon(index.class.getResource("/images/flask_about.png")));
		btn_about.setForeground(new Color(235, 150, 28));
		btn_about.setBackground(new Color(29, 31, 33));
		btn_about.setBounds(158, 175, 128, 128);
		contentPane.add(btn_about);
		
		JButton btn_close = new JButton("");
		btn_close.setIcon(new ImageIcon(index.class.getResource("/images/flask_cancel.png")));
		btn_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btn_close.setForeground(new Color(235, 150, 28));
		btn_close.setBackground(new Color(29, 31, 33));
		btn_close.setBounds(301, 175, 128, 128);
		contentPane.add(btn_close);
	}
	public static void init_status(){
		frame_webcam.webcam_sta();
		frame_music.ctrl_media("media_flag","status");
	}
}
