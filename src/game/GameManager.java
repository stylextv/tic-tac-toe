package de.ttt.game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Locale;

import de.ttt.main.Main;
import de.ttt.main.Vars;
import de.ttt.math.MathUtil;

public class GameManager {
	
	private static String buttonString="New Game";
	private static int boardSize=480;
	private static int symbolSize=100;
	
	private static int buttonWidth=210;
	private static int buttonHeight=50;
	
	private static Game game;
	
	private static double buttonHoverAlpha=0,buttonHoverAlphaTimer=0;
	
	public static void init() {
		game=new Game();
		
		String lang=Locale.getDefault().getLanguage();
		if(lang.equals(new Locale("de").getLanguage())) buttonString="Neues Spiel";
	}
	
	public static void update(int width, int height, int mouseX, int mouseY, boolean hasMouse) {
		if((int)game.getXOffset()!=0) {
			int x=Main.getWidth()/2+boardSize/2+100-310/2;
			int y=Main.getHeight()/2-buttonHeight/2;
			if(mouseX>=x&&mouseY>=y && mouseX<x+buttonWidth&&mouseY<y+buttonHeight) {
				buttonHoverAlphaTimer+=0.0125;
				if(buttonHoverAlphaTimer>1) buttonHoverAlphaTimer=1;
				Main.changeCursor(Cursor.HAND_CURSOR);
			} else {
				buttonHoverAlphaTimer-=0.025;
				if(buttonHoverAlphaTimer<0) buttonHoverAlphaTimer=0;
				Main.changeCursor(Cursor.DEFAULT_CURSOR);
			}
			buttonHoverAlpha=(MathUtil.sigmoid(buttonHoverAlphaTimer*0.5+0.5)-0.5)*2*0.2;
		} else if(buttonHoverAlphaTimer>0) {
			buttonHoverAlphaTimer-=0.025;
			if(buttonHoverAlphaTimer<0) buttonHoverAlphaTimer=0;
			buttonHoverAlpha=(MathUtil.sigmoid(buttonHoverAlphaTimer*0.5+0.5)-0.5)*2*0.2;
			Main.changeCursor(Cursor.DEFAULT_CURSOR);
		} else Main.changeCursor(Cursor.DEFAULT_CURSOR);
		
		game.update();
	}
	public static void draw(Graphics2D graphics, int width, int height) {
		if(game.getButtonAlpha()!=0) {
			graphics.setColor(new Color(66,52,248, game.getButtonAlpha()));
			graphics.setFont(new Font("Roboto Medium", 0, 19));
			RoundRectangle2D rect = new RoundRectangle2D.Double(width/2+boardSize/2+100-310/2, height/2-buttonHeight/2, buttonWidth, buttonHeight, 4, 4);
			graphics.fill(rect);
			graphics.setColor(new Color(255,255,255, game.getButtonAlpha()));
			graphics.drawString(buttonString, (int) (width/2+boardSize/2+100-310/2+buttonWidth/2-graphics.getFontMetrics().stringWidth(buttonString)/2)-2, height/2+graphics.getFontMetrics().getHeight()/3);
			graphics.setColor(new Color(255,255,255, (int) (buttonHoverAlpha*game.getButtonAlpha())));
			graphics.fill(rect);
		}
		
		graphics.setColor(Vars.GAME_MAINCOLOR);
		graphics.setStroke(Vars.GAME_STROKE);
		
		int k1=width/2-boardSize/2;
		int k2=height/2-boardSize/2;
		int k3=height/2+boardSize/2;
		int k4=width/2+boardSize/2;
		graphics.draw(new Line2D.Double(k1+boardSize/3-game.getXOffset(), k2, k1+boardSize/3-game.getXOffset(), k3));
		graphics.draw(new Line2D.Double(k1+boardSize/3*2-game.getXOffset(), k2, k1+boardSize/3*2-game.getXOffset(), k3));
		graphics.draw(new Line2D.Double(k1-game.getXOffset(), k2+boardSize/3, k4-game.getXOffset(), k2+boardSize/3));
		graphics.draw(new Line2D.Double(k1-game.getXOffset(), k2+boardSize/3*2, k4-game.getXOffset(), k2+boardSize/3*2));
	    
		for(int i=0; i<9; i++) {
			Field f=game.getField(i);
			if(f.getDisplayType()!=null) {
				int x=i%3;
				int y=i/3;
				
				if(f.getDisplayType()==Type.KNOT) drawKnot(graphics, width, height, x, y, f.getState());
				else drawCross(graphics, width, height, x, y, f.getState());
			}
		}
		
	    if((int)game.getXOffset()!=0&&game.getWonP1X()!=-1) {
	    	double l1=width/2-boardSize/2+boardSize/3/2-game.getXOffset();
	    	int l2=height/2-boardSize/2+boardSize/3/2;
	    	double d=game.getXOffset()/155;
			double x1=l1+boardSize/3*game.getWonP1X();
			double y1=l2+boardSize/3*game.getWonP1Y();
			double x2=l1+boardSize/3*game.getWonP2X();
			double y2=l2+boardSize/3*game.getWonP2Y();
			
			double diffX=x2-x1;
			double diffY=y2-y1;
			
			graphics.draw(new Line2D.Double(x1, y1, x1+diffX*d, y1+diffY*d));
	    }
	}
	public static void drawKnot(Graphics2D graphics, int width, int height, int gridX, int gridY, double state) {
		double x=width/2-boardSize/2+boardSize/3/2-symbolSize/2+boardSize/3*gridX;
		double y=height/2-boardSize/2+boardSize/3/2-symbolSize/2+boardSize/3*gridY;
		
		graphics.draw(new Arc2D.Double(x-game.getXOffset(), y, symbolSize, symbolSize, 90, state*360, 0));
	}
	public static void drawCross(Graphics2D graphics, int width, int height, int gridX, int gridY, double state) {
		double x=width/2-boardSize/2+boardSize/3/2+boardSize/3*gridX-game.getXOffset();
		double y=height/2-boardSize/2+boardSize/3/2+boardSize/3*gridY;
		
		graphics.draw(new Line2D.Double(x-symbolSize/2, y-symbolSize/2, x-symbolSize/2+symbolSize*state, y-symbolSize/2+symbolSize*state));
		graphics.draw(new Line2D.Double(x+symbolSize/2, y-symbolSize/2, x+symbolSize/2-symbolSize*state, y-symbolSize/2+symbolSize*state));
	}
	
	public static void onMouseReleased(MouseEvent e) {
		int mouseX=Main.getMouseX();
		int mouseY=Main.getMouseY();
		
		if(!game.isOver()&&game.getWhosTurn()==Type.CROSS) {
			if((int)game.getXOffset()==0) {
				mouseX-=Main.getWidth()/2-boardSize/2;
				mouseY-=Main.getHeight()/2-boardSize/2;
				if(mouseX>=0&&mouseY>=0&&mouseX<boardSize&&mouseY<boardSize) {
					int gridX=mouseX/(boardSize/3);
					int gridY=mouseY/(boardSize/3);
					
					Field f=game.getField(gridY*3+gridX);
					if(f.getType()==null) {
						f.setType(Type.CROSS);
						game.checkForWinner();
						game.setWhosTurn(Type.KNOT);
					}
				}
			}
		} else {
			int x=Main.getWidth()/2+boardSize/2+100-310/2;
			int y=Main.getHeight()/2-buttonHeight/2;
			if(mouseX>=x&&mouseY>=y && mouseX<x+buttonWidth&&mouseY<y+buttonHeight) {
				game.reset();
			}
		}
	}
	
}
