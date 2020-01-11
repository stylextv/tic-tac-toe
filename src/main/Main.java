package de.ttt.main;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import de.ttt.background.BackgroundManager;
import de.ttt.game.GameManager;
import de.ttt.image.ImageUtil;
import de.ttt.render.ColorSchemeManager;
import de.ttt.render.Renderer;

public class Main {
	
	private static JFrame frame;
	private static Renderer renderer;
	
	private static boolean running=true;
	private static int width, height;
	private static int mouseX, mouseY;
	
	private static int currentCursor=-1;
	
	public static void main(String[] args) {
		try {
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    Font font=Font.createFont(Font.TRUETYPE_FONT, Main.class.getClassLoader().getResourceAsStream("assets/fonts/Roboto-Medium.ttf"));
		    ge.registerFont(font);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		
		ImageUtil.loadResources();
		GameManager.init();
		System.gc();
		createFrame();
		startGameLoop();
	}
	private static void createFrame() {
		frame = new JFrame(Vars.WINDOW_TITLE);
		frame.setSize(Vars.WINDOW_WIDTH, Vars.WINDOW_HEIGHT);
		Dimension monitor=Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(monitor.width/2-Vars.WINDOW_WIDTH/2, (monitor.height-40)/2-Vars.WINDOW_HEIGHT/2);
		
		frame.setIconImages(ImageUtil.getIcons());
		
		frame.add(renderer=new Renderer());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		    	frame.setVisible(false);
		    	running=false;
		    }
		});
		frame.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				GameManager.onMouseReleased(e);
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		frame.setResizable(false);
		frame.setVisible(true);
	}
	public static void startGameLoop() {
		while(running) {
			runGameLoop();
		}
		
		System.exit(0);
	}
	
	public static void runGameLoop() {
		long before=System.nanoTime();
		
		if(!(width==0||height==0)) {
			Point mouse=renderer.getMousePosition();
			boolean hasMouse=false;
			if(mouse!=null) {
				mouseX=mouse.x;
				mouseY=mouse.y;
				hasMouse=true;
			}
			
			BackgroundManager.update(width, height, mouseX, mouseY, hasMouse);
			GameManager.update(width, height, mouseX, mouseY, hasMouse);
			ColorSchemeManager.update();
		}
		
		renderer.paintImmediately(0, 0, renderer.getWidth(), renderer.getHeight());
		
		long after=System.nanoTime();
		int diff=(int) (after-before);
		int sleepNano=(int)(1000/70.0*1000000) - diff;
		if(sleepNano>0) try {
			Thread.sleep(sleepNano/1000000, sleepNano%1000000);
		} catch (InterruptedException ex) {}
	}
	public static void render(Graphics2D graphics) {
		BackgroundManager.draw(graphics, width, height);
		GameManager.draw(graphics, width, height);
	}
	
	public static void changeCursor(int cursor) {
		if(currentCursor==-1||cursor!=currentCursor) {
			renderer.setCursor(new Cursor(cursor));
			currentCursor=cursor;
		}
	}
	
	public static int getWidth() {
		return width;
	}
	public static int getHeight() {
		return height;
	}
	public static void setWidth(int width) {
		Main.width = width;
	}
	public static void setHeight(int height) {
		Main.height = height;
	}
	public static int getMouseX() {
		return mouseX;
	}
	public static int getMouseY() {
		return mouseY;
	}
	
}
