package n4;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.sun.opengl.util.FPSAnimator;

public class Frame extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final int FPS = 15;
	private static final Mundo renderer = Mundo.getInstance();
	private static GLCanvas canvas;
	private static FPSAnimator animator;
	
	public Frame() {
		setTitle("F-Zero");
		setSize(1500, 1300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());	

		GLCapabilities glCaps = new GLCapabilities();
 		glCaps.setRedBits(8);
 		glCaps.setBlueBits(8);
 		glCaps.setGreenBits(8);
 		glCaps.setAlphaBits(8);

 		canvas = new GLCanvas(glCaps);
 		canvas.addGLEventListener(renderer);
 		canvas.addKeyListener(renderer);
 		canvas.addMouseListener(renderer);
 		canvas.addMouseMotionListener(renderer);
 		add(canvas, BorderLayout.CENTER);	
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Frame frame = new Frame();
				
				animator = new FPSAnimator(canvas, FPS, true);
				renderer.setAnimator(animator);
				
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						new Thread() {
							public void run() {
								if (animator.isAnimating())
									animator.stop();
								System.exit(0);
							}
						}.start();
					}
				});
				frame.setVisible(true);
				animator.start();
			}
		});
	}	
}
