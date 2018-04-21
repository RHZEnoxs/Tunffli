package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import main.index;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class webcam_streamer extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					webcam_streamer frame = new webcam_streamer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public boolean webcam_flag = false;
	JButton btn_webcam;
	public webcam_streamer() {
		super("Tunffli");
		setSize(480,575);
		setLocationRelativeTo(null);
		setResizable(false);
		//setIconImage(index.icon.getImage());
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#111111"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel side_pan = new JPanel();
		side_pan.setBorder(new LineBorder(Color.decode("#EB961C"), 3, true));
		side_pan.setBackground(Color.decode("#1D1F21"));
		side_pan.setLayout(null);
		contentPane.add(side_pan);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(webcam_streamer.class.getResource("/images/flask_camer_300.png")));
		lblNewLabel.setBounds(15, 15, 434, 428);
		side_pan.add(lblNewLabel);
		
		JPanel btn_pan = new JPanel();
		btn_pan.setBorder(new LineBorder(Color.decode("#EB961C"), 3, true));
		btn_pan.setBackground(Color.decode("#1D1F21"));
		contentPane.add(btn_pan, BorderLayout.SOUTH);
		
		JButton btn_view = new JButton(" VIEW ");
		btn_view.setBounds(15, 15, 320, 50);
		btn_view.setFont(new Font("Arial Black", Font.BOLD, 30));
		btn_view.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
				String url = "http://"+index.mPrefer.getHost()+":8080/?action=stream";
				Runtime.getRuntime().exec("cmd /c start " + url);
				}
				catch (java.io.IOException exception)
				{
				}
			}
		});
		btn_view.setBackground(Color.decode("#EB961C"));
		btn_view.setForeground(Color.decode("#FBFFFB"));
		btn_pan.add(btn_view);
		
		btn_webcam = new JButton("START");
		btn_webcam.setBounds(15, 15, 320, 50);
		btn_webcam.setFont(new Font("Arial Black", Font.BOLD, 30));
		btn_webcam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSONObject obj = new JSONObject();
		        obj.put("id", "Mjpg");
		        obj.put("reply",false);
		        obj.put("dev_id","0x01");
		        obj.put("value",!webcam_flag);
		        index.mqtt.publish_message(obj.toString());
			}
		});
		btn_webcam.setBackground(Color.decode("#EB961C"));
		btn_webcam.setForeground(Color.decode("#FBFFFB"));
		btn_pan.add(btn_webcam);
		
	}
	public void reback(boolean flag){
		webcam_flag = flag;
		if(webcam_flag){
			btn_webcam.setText(" STOP ");
		}else{
			btn_webcam.setText("START");
		}
	}
	public void webcam_sta(){
		JSONObject obj_mjpg = new JSONObject();
		obj_mjpg.put("id", "Mjpg");
		obj_mjpg.put("reply",false);
		obj_mjpg.put("dev_id","all");
        index.mqtt.publish_message(obj_mjpg.toString());
	}

}
