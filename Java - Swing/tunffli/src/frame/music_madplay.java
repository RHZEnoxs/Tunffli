package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import main.index;

import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class music_madplay extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					music_madplay frame = new music_madplay();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	JList list;
	JSONObject obj;
	final ImageIcon icon_fold = new ImageIcon();
	final ImageIcon icon_music = new ImageIcon();
	public boolean media_flag = false;
	JButton btn_play;
	public music_madplay() {
		super("Tunffli");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(678,590);
		setLocationRelativeTo(null);
		setResizable(false);
		//setIconImage(index.icon.getImage());
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#111111"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		obj = new JSONObject(LoadData());
		JTabbedPane tab_pan = new JTabbedPane(JTabbedPane.TOP);
		tab_pan.setForeground(Color.decode("#EB961C"));
		tab_pan.setBackground(Color.decode("#1D1F21"));
		tab_pan.setFont(new Font("Arial Black", Font.BOLD, 20));
		contentPane.add(tab_pan);
		
		JPanel default_pan = new JPanel();
		tab_pan.addTab("default", null, default_pan, null);
		default_pan.setBorder(new LineBorder(Color.decode("#EB961C"), 3, true));
		default_pan.setBackground(Color.decode("#1D1F21"));
		default_pan.setLayout(null);
		icon_fold.setImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/icon_fold.png")));
		icon_music.setImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/icon_music.png")));
		
		list = new JList();
		list.setBorder(new LineBorder(Color.decode("#EB961C"), 3, true));
		list.setForeground(Color.decode("#FBED7E"));
		list.setBackground(Color.decode("#1D1F21"));
		list.setFont(new Font("Arial", Font.BOLD, 25));
		list.setBounds(0, 0, 657, 503);
		list.addMouseListener(mouseListener);
		default_pan.add(list);
		filepath_position(-1);
		JPanel playlist_pan = new JPanel();
		playlist_pan.setLayout(null);
		playlist_pan.setBorder(new LineBorder(Color.decode("#EB961C"), 3, true));
		playlist_pan.setBackground(Color.decode("#1D1F21"));
		
		tab_pan.addTab("playlist", null, playlist_pan, null);
		
		JPanel btn_pan = new JPanel();
		btn_pan.setBorder(new LineBorder(Color.decode("#EB961C"), 3, true));
		btn_pan.setBackground(new Color(29, 31, 33));
		contentPane.add(btn_pan, BorderLayout.SOUTH);
		btn_pan.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btn_play = new JButton("");
		btn_play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(media_flag){
					ctrl_media("pause","");
				}else{
					ctrl_media("play","");
				}
			}
		});
		btn_play.setBackground(Color.decode("#1D1F21"));
		btn_play.setIcon(new ImageIcon(music_madplay.class.getResource("/images/madplay_play.png")));
		btn_pan.add(btn_play);
		
		JButton btn_stop = new JButton("");
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl_media("stop","");
			}
		});
		btn_stop.setBackground(Color.decode("#1D1F21"));
		btn_stop.setIcon(new ImageIcon(music_madplay.class.getResource("/images/madplay_stop.png")));
		btn_pan.add(btn_stop);
		
		JButton btn_back = new JButton("");
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl_media("back","");
			}
		});
		btn_back.setBackground(Color.decode("#1D1F21"));
		btn_back.setIcon(new ImageIcon(music_madplay.class.getResource("/images/madplay_back.png")));
		btn_pan.add(btn_back);
		
		JButton btn_next = new JButton("");
		btn_next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl_media("next","");
			}
		});
		btn_next.setBackground(Color.decode("#1D1F21"));
		btn_next.setIcon(new ImageIcon(music_madplay.class.getResource("/images/madplay_next.png")));
		btn_pan.add(btn_next);
		
		JButton btn_music = new JButton("");
		btn_music.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl_media("fold_file","refresh");
			}
		});
		btn_music.setBackground(Color.decode("#1D1F21"));
		btn_music.setIcon(new ImageIcon(music_madplay.class.getResource("/images/madplay_music.png")));
		btn_pan.add(btn_music);
	}
	String[] fold_file;
	int fold_flag = -1;
	public void filepath_position(int position){
		if(position == -1){
			fold_flag = -1;
			fold_file = new String[obj.getJSONArray("file_list").length()];
			fold_count = obj.getJSONArray("fold_list").length();
			for(int i=0;i<fold_count;i++){
				fold_file[i] = obj.getJSONArray("fold_list").getString(i);
			}
			for(int i=fold_count;i<obj.getJSONArray("file_list").length();i++){
				fold_file[i] = obj.getJSONArray("file_list").getString(i);
			}
		}else{
			fold_flag = position;
			fold_file = new String[obj.getJSONArray("file_list").getJSONArray(position).length()+1];
			fold_file[0] ="..";
			fold_count = 1;
			for(int i=0;i<obj.getJSONArray("file_list").getJSONArray(position).length();i++){
				fold_file[i+1] = obj.getJSONArray("file_list").getJSONArray(position).getString(i);
			}
		}
		reload_list();
		//System.out.println(obj.get("fold_list"));
		//System.out.println(obj.getJSONArray("file_list").getJSONArray(0).get(0));
	}
	public void reload_list(){
		list.setModel(new AbstractListModel() {
			public int getSize() {
				return fold_file.length;
			}
			public Object getElementAt(int index) {
				return fold_file[index];
			}
		});
		list.setCellRenderer(renderer);
	}
	public String LoadData(){
        FileReader fr;
        StringBuilder StrShow = new StringBuilder();;
        try {
            fr = new FileReader("config/music_path.json");
            //將BufferedReader與FileReader做連結
            BufferedReader br = new BufferedReader(fr);
            String temp = br.readLine(); //readLine()讀取一整行
            ArrayList list = new ArrayList();
            while (temp!=null ){
                if(temp.length()>3){
                    list.add(temp+"\n");
                }
                temp=br.readLine();
            }
            for(int i=0;i<list.size();i++){
            	StrShow.append(list.get(i)+"");
            }
            br.close();
        } catch (IOException e) {
            // TODO 自動產生的 catch 區塊
            e.printStackTrace();
        }
        return StrShow.toString();
    }
	public void SaveData(String content){//1.csv 2.txt 
		String title="music_path.json";
		String path="config/";
	      File file1=new File(path);
	      if(!file1.exists())
	      {
	        file1.mkdirs();
	      }
		 try{
		        FileWriter fw = new FileWriter(path + title, false);
		        BufferedWriter bw = new BufferedWriter(fw); 
		        //bw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
		        bw.write(content);
		        bw.newLine();
		        bw.close();
		    }catch(IOException e){
		       e.printStackTrace();
		    }
	}
	int fold_count = 0;
	ListCellRenderer renderer = new DefaultListCellRenderer() {
	      public Component getListCellRendererComponent(JList list, Object value, int index,
	        boolean isSelected, boolean cellHasFocus)
	      {
	        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        if (index < fold_count) {
	        	setIcon(icon_fold);
	        }else{
	        	setIcon(icon_music);
	        }
	        return c;
	      }
	};
	MouseListener mouseListener = new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
	        if (e.getClickCount() == 2) {
	        	int position = list.getSelectedIndex();
	        	if(position < fold_count){
	        		if(fold_flag == -1){
	        			filepath_position(position);
	        		}else{
	        			filepath_position(-1);
	        		}
	        	}else{
	        		String file;
	        		String selectedItem = (String) list.getSelectedValue();
	        		if(fold_flag == -1){
	        			file = selectedItem;
	        		}else{
	        			file = obj.getJSONArray("fold_list").getString(fold_flag)+"/"+selectedItem;
	        		}
	        		ctrl_media("play",file);
	        		//JOptionPane.showMessageDialog(null,file);
	        	}
	        	//JOptionPane.showMessageDialog(null,position);
	         }
	    }
	};
	public void ctrl_media(String ctrl,String file){
		JSONObject obj = new JSONObject();
        obj.put("id", "Madplay");
        obj.put("reply",false);
        obj.put("action",ctrl);
        obj.put("value",file);
        index.mqtt.publish_message(obj.toString());
	}
	public void reback(String action ,boolean flag){
		media_flag = flag;
		if(media_flag){
			btn_play.setIcon(new ImageIcon(music_madplay.class.getResource("/images/madplay_pause.png")));
		}else{
			btn_play.setIcon(new ImageIcon(music_madplay.class.getResource("/images/madplay_play.png")));
		}
	}
	public void reback(JSONObject msg_obj){
		SaveData(msg_obj.toString());
		obj = new JSONObject(LoadData());
		filepath_position(-1);
	}
	
}
