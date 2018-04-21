package frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

public class About_Dialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			About_Dialog dialog = new About_Dialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public About_Dialog() {
		setSize(525, 280);
		setResizable(false); 
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JLabel text_title = new JLabel("Tunffli");
		text_title.setForeground(Color.decode("#EB961C"));
		text_title.setFont(new Font("Arial Black", Font.BOLD, 36));
		text_title.setBounds(315, 33, 150, 36);
		getContentPane().add(text_title);
		
		int [] version_arr = new int[5];

		/*
		double verson = TabFrame.SRS_Verson/10000;
		DecimalFormat df= new DecimalFormat("#.####");
		String s=df.format(verson); */
		
		
		JLabel text_info = new JLabel("7688 - turn off the light"	);
		text_info.setForeground(Color.decode("#FBED7E"));
		text_info.setFont(new Font("Arial", Font.BOLD, 16));
		text_info.setBounds(296, 73, 185, 25);
		getContentPane().add(text_info);
		
		JLabel text_1 = new JLabel("");
		text_1.setText("Java \u5716\u5F62\u4ECB\u9762");
		text_1.setForeground(Color.decode("#FBED7E"));
		text_1.setFont(new Font("標楷體", Font.BOLD, 16));
		text_1.setBounds(296, 172, 185, 25);
		getContentPane().add(text_1);
		
		JLabel text_2 = new JLabel("Mqtt \u901A\u8A0A\u6307\u4EE4");
		text_2.setForeground(Color.decode("#FBED7E"));
		text_2.setFont(new Font("標楷體", Font.BOLD, 16));
		text_2.setBounds(296, 195, 185, 25);
		getContentPane().add(text_2);
		
		JLabel text_3 = new JLabel("\u96FB\u8166\u7AEF\uFF0C\u63A7\u5236\u9762\u677F:");
		text_3.setForeground(Color.decode("#FBED7E"));
		text_3.setFont(new Font("標楷體", Font.BOLD, 16));
		text_3.setBounds(296, 138, 195, 25);
		getContentPane().add(text_3);
		
		JLabel img_bg = new JLabel("");
		img_bg.setFont(new Font("新細明體", Font.PLAIN, 10));
		img_bg.setHorizontalAlignment(SwingConstants.CENTER);
		img_bg.setBounds(15, 15, 240, 240);
		img_bg.setIcon(new ImageIcon(About_Dialog.class.getResource("/images/icon_turn_off.png")));
		getContentPane().add(img_bg);
		setUndecorated(true);
		addFocusListener(new FocusAdapter() {  
             public void focusLost(final FocusEvent arg0) {  
            	 dispose();
             }  
         });   
		addMouseListener(new MouseAdapter(){
			 public void mouseClicked(MouseEvent e)
	          {
	            	dispose();
	          }
		});
		getContentPane().setBackground(Color.decode("#111111"));
	}
}
