package backup;
import java.awt.Component;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
 
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
public class DynamicListItemDemo extends JFrame{
	 public static void main(String[] args)
	  {
	    JFrame frm = new DynamicListItemDemo("http://www.javaworld.com.tw/jute/post/view?bid=5&id=169007&sty=1&tpg=1&age=0");
	    frm.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    frm.pack();
	    frm.setVisible(true);
	  }
	  
	  public DynamicListItemDemo()
	  {
	    this("DynamicListItem demo.");
	  }
	  
	  public DynamicListItemDemo(String title)
	  {
	    super(title);
	    
	    initComponents();
	  }
	 
	  private void initComponents()
	  {
	    final JList choice = new JList(new Object[] {"Animation", "Static Text 1", "Static Text 2", "Static Text 3"});
	    choice.setVisibleRowCount(3);
	    final ImageIcon icon = new ImageIcon();
	    
	    icon.setImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/icon_fold.png")));
	    ListCellRenderer renderer = new DefaultListCellRenderer() {
	      public Component getListCellRendererComponent(JList list, Object value, int index,
	        boolean isSelected, boolean cellHasFocus)
	      {
	        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        if (index == 0) {
	          setIcon(icon);
	        }
	        if (index == 1) {
		          setIcon(icon);
		        }
	        return c;
	      }
	    };
	    choice.setCellRenderer(renderer);
	    
	    getContentPane().add(new JScrollPane(choice));
	  }
}
