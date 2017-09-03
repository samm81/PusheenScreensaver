import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Sam Maynard
 * Abstract class to deal with making a Double Buffered Canvas.
 *
 */
@SuppressWarnings("serial")
abstract class DoubleBufferedCanvas extends Canvas implements Runnable {

	protected Thread thread;

	protected int fps;

	protected Image second;

	/**
	 * constructor.
	 * @param fps the frames per second for which the canvas is to run at
	 */
	public DoubleBufferedCanvas(int fps) {
		super();
		this.fps = fps;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		if(second == null)
			second = createImage(this.getWidth(), this.getHeight());

		second.getGraphics().clearRect(0, 0, second.getWidth(null), second.getHeight(null));
		prepare(second.getGraphics());
		g.drawImage(second, 0, 0, null);
	}

	/**
	 * actually draws the image
	 * @param g graphics to draw with
	 */
	abstract void prepare(Graphics g);

	@Override
	public void run() {
		while(Thread.currentThread() == thread){
			updateVars();
			repaint();
			try {
				Thread.sleep((int)(1000f/(float)fps));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * for any global variable updating that may need to be done
	 */
	abstract protected void updateVars();

}
