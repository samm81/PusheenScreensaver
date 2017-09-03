import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

import javax.swing.*;


@SuppressWarnings("serial")
public class Pusheen extends DoubleBufferedCanvas {

	final int numPusheens = 34;

	ImageIcon defaultPusheen;
	ImageIcon[] pusheens;

	int floorHieght = 10;

	int x = 300;
	double pos = 300.0;
	double speed = 1.2;

	boolean flipped = false;
	boolean defaulted = true;

	Image pusheen = null;

	Random r = new Random();
	double poseThreshold = .006;
	double defaultThreshold = .008;
	double flipThreshold = 0.003;

	long pause = 3000;
	long maxPause = 6300;
	long lastTime = System.currentTimeMillis();

	private static Color backgroundColor = new Color(252, 240, 228);

	static int mouseMovements = 0;  //required for a windows bug

	public static void main(String[] args) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)screen.getWidth();
		int screenHeight = (int)screen.getHeight();
		JFrame f = new JFrame();
		f.setBounds(0,0,screenWidth,screenHeight);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setUndecorated(true);

        DoubleBufferedCanvas c = new Pusheen(30);
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

	public Pusheen(int fps) {
		super(fps);

		defaultPusheen = new ImageIcon(getClass().getResource("default.gif"));
		pusheen = defaultPusheen.getImage();

		pusheens = new ImageIcon[numPusheens];
		for(int i=1;i<=pusheens.length;i++){

			 // When I compiled this with IntelliJ I needed the following line, but if you JAR everything up sanely you'll probably want the second line.
			String f = i + ".gif";
			//String f = "/pusheens/" + i + ".gif";
			pusheens[i-1] = new ImageIcon(getClass().getResource(f));
		}
	}

	@Override
	void prepare(Graphics g) {
		if(pusheen != null)
			drawPusheen(g, pusheen, x, flipped);
	}

	private void drawPusheen(Graphics g, Image pusheen, int x, boolean flip) {
		int width = pusheen.getWidth(null);
		int height = pusheen.getHeight(null);

		int y = this.getHeight() - (pusheen.getHeight(null) + 10);
		if(!flip)
			g.drawImage(pusheen, x, y, null);
		else
			g.drawImage(pusheen, x + width, y, -width, height, null);
	}

	@Override
	protected void updateVars() {
		if(pusheen == null || this.getWidth() == 0)
			return;

		//flipping
		if(defaulted){
			double flipChance = r.nextDouble();
			if(flipChance < flipThreshold)
				flipped = !flipped;
		}

		// position
		if(defaulted){
			if(((int)(pos + speed) + pusheen.getWidth(null)) > this.getWidth() ||
					(int)(pos + speed) < 0){
				flipped = !flipped;
			}
			if(!flipped) pos += speed; else pos -= speed;
			x = (int)pos;
		}

		//pose
		long interval = System.currentTimeMillis() - lastTime;
		if(interval > pause){
			if(defaulted){
				double poseChance = r.nextDouble();
				if(poseChance < poseThreshold){
					int pose = r.nextInt(pusheens.length);

					pusheen = pusheens[pose].getImage();
					defaulted = false;
					lastTime = System.currentTimeMillis();
				}
			}else{
				double defaultChance = r.nextDouble();
				if(defaultChance < defaultThreshold || interval > maxPause){
					pusheen = defaultPusheen.getImage();
					defaulted = true;
					lastTime = System.currentTimeMillis();
				}
			}
		}

	}

}
