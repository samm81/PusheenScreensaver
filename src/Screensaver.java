import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

public class Screensaver {

	private static DoubleBufferedCanvas c = new Pusheen(30);
	
	private static Color backgroundColor = new Color(252, 240, 228);
	
	static int mouseMovements = 0;  //required for a windows bug
	
	public static void main(String[] args) {
		System.gc();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)screen.getWidth();
		int screenHeight = (int)screen.getHeight();
		JFrame f = new JFrame();
		f.setBounds(0,0,screenWidth,screenHeight);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		
		c.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				System.exit(0);
			}
		});
		c.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent e){
				// required for a windows bug
				mouseMovements++;
				if(mouseMovements > 2)
					System.exit(0);
			}
		});
		f.add(c);
		c.setBackground(backgroundColor);
		f.setVisible(true);
		f.toFront();
	}
	
}
