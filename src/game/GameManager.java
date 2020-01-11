package de.ttt.game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Locale;

import de.ttt.image.ImageUtil;
import de.ttt.main.Main;
import de.ttt.main.Vars;
import de.ttt.math.MathUtil;
import de.ttt.render.ColorScheme;
import de.ttt.render.ColorSchemeManager;

public class GameManager {
	
	private static String buttonString="New Game";
	private static int boardSize=480;
	private static int symbolSize=100;
	
	private static int buttonWidth=210;
	private static int buttonHeight=50;
	
	private static Game game;
	
	private static double buttonHoverAlpha=0,buttonHoverAlphaTimer=0;
	private static double settingsHoverAlpha=0,settingsHoverAlphaTimer=0,settingsOpenedTimer=0,settingsOpenedState=0;
	private static boolean settingsOpened=false;
	
	private static int hoveredSettingsElement=-1;
	
	public static void init() {
		game=new Game();
		
		String lang=Locale.getDefault().getLanguage();
		if(lang.equals(new Locale("de").getLanguage())) buttonString="Neues Spiel";
	}
	
	public static void update(int width, int height, int mouseX, int mouseY, boolean hasMouse) {
		boolean handCursor=false;
		
		if((int)game.getXOffset()!=0) {
			int x=Main.getWidth()/2+boardSize/2+100-310/2;
			int y=Main.getHeight()/2-buttonHeight/2;
			if(mouseX>=x&&mouseY>=y && mouseX<x+buttonWidth&&mouseY<y+buttonHeight) {
				buttonHoverAlphaTimer+=0.025;
				if(buttonHoverAlphaTimer>1) buttonHoverAlphaTimer=1;
				handCursor=true;
			} else {
				buttonHoverAlphaTimer-=0.035;
				if(buttonHoverAlphaTimer<0) buttonHoverAlphaTimer=0;
			}
			buttonHoverAlpha=(MathUtil.sigmoid(buttonHoverAlphaTimer*0.5+0.5)-0.5)*2*0.2;
		} else if(buttonHoverAlphaTimer>0) {
			buttonHoverAlphaTimer-=0.035;
			if(buttonHoverAlphaTimer<0) buttonHoverAlphaTimer=0;
			buttonHoverAlpha=(MathUtil.sigmoid(buttonHoverAlphaTimer*0.5+0.5)-0.5)*2*0.2;
		}
		
		double dis=Math.pow(mouseX-(width-buttonHeight/2-25), 2)+Math.pow(mouseY-(25+buttonHeight/2), 2);
		double max=buttonHeight/2;
		max=max*max;
		if(dis<=max) {
			if(settingsHoverAlphaTimer<1) {
				settingsHoverAlphaTimer+=0.025;
				if(settingsHoverAlphaTimer>1) settingsHoverAlphaTimer=1;
				settingsHoverAlpha=(MathUtil.sigmoid(settingsHoverAlphaTimer*0.5+0.5)-0.5)*2*0.2;
			}
			handCursor=true;
		} else {
			if(settingsHoverAlphaTimer>0) {
				settingsHoverAlphaTimer-=0.035;
				if(settingsHoverAlphaTimer<0) settingsHoverAlphaTimer=0;
				settingsHoverAlpha=(MathUtil.sigmoid(settingsHoverAlphaTimer*0.5+0.5)-0.5)*2*0.2;
			}
		}
		if(settingsOpened) {
			if(settingsOpenedTimer<1) {
				settingsOpenedTimer+=0.03;
				if(settingsOpenedTimer>1) {
					settingsOpenedTimer=1;
					settingsOpenedState=1;
				} else settingsOpenedState=MathUtil.sigmoid(settingsOpenedTimer);
			}
		} else {
			if(settingsOpenedTimer>0) {
				settingsOpenedTimer-=0.03;
				if(settingsOpenedTimer<0) {
					settingsOpenedTimer=0;
					settingsOpenedState=0;
				} else settingsOpenedState=MathUtil.sigmoid(settingsOpenedTimer);
			}
		}
		
		int currentHoveredSettingsElement=-1;
		if(settingsOpened) {
			mouseX-=width-buttonHeight-25+10;
			if(mouseX>=0&&mouseX<buttonHeight-20) {
				mouseY-=25+buttonHeight+20;
				int size=buttonHeight-20;
				int y1=(int) (10*settingsOpenedState);
				int y2=(int) ((10*2+size)*settingsOpenedState);
				int y3=(int) ((10*3+size*2)*settingsOpenedState);
				if(mouseY>=y1&&mouseY<y1+size) currentHoveredSettingsElement=0;
				else if(mouseY>=y2&&mouseY<y2+size) currentHoveredSettingsElement=1;
				else if(mouseY>=y3&&mouseY<y3+size) currentHoveredSettingsElement=2;
			}
		}
		hoveredSettingsElement=currentHoveredSettingsElement;
		if(hoveredSettingsElement!=-1) handCursor=true;
		
		if(handCursor) Main.changeCursor(Cursor.HAND_CURSOR);
		else Main.changeCursor(Cursor.DEFAULT_CURSOR);
		
		game.update();
	}
	public static void draw(Graphics2D graphics, int width, int height) {
		Color text=ColorSchemeManager.getButtonText();
		Color hover=ColorSchemeManager.getButtonHover();
		if(game.getButtonAlpha()!=0) {
			Color base=ColorSchemeManager.getButtonColor();
			graphics.setColor(new Color(base.getRed(),base.getGreen(),base.getBlue(), game.getButtonAlpha()));
			graphics.setFont(new Font("Roboto Medium", 0, 19));
			RoundRectangle2D rect = new RoundRectangle2D.Double(width/2+boardSize/2+100-310/2, height/2-buttonHeight/2, buttonWidth, buttonHeight, 4, 4);
			graphics.fill(rect);
			graphics.setColor(new Color(text.getRed(),text.getGreen(),text.getBlue(), game.getButtonAlpha()));
			graphics.drawString(buttonString, (int) (width/2+boardSize/2+100-310/2+buttonWidth/2-graphics.getFontMetrics().stringWidth(buttonString)/2)-2, height/2+graphics.getFontMetrics().getHeight()/3);
			if(buttonHoverAlpha!=0) {
				graphics.setColor(new Color(hover.getRed(),hover.getGreen(),hover.getBlue(), (int) (buttonHoverAlpha*game.getButtonAlpha())));
				graphics.fill(rect);
			}
		}
		drawSettingsMenu(graphics, width, height);
		
		graphics.setColor(ColorSchemeManager.getGrid());
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
	private static void drawSettingsMenu(Graphics2D graphics, int width, int height) {
		Color grid=ColorSchemeManager.getGrid();
		graphics.setColor(grid);
		int x=width-buttonHeight-25;
		int y=25;
		Arc2D arc=new Arc2D.Double(x, y, buttonHeight, buttonHeight, 0, 360, 0);
		graphics.fill(arc);
		AffineTransform trans=new AffineTransform();
		trans.translate(x, y);
		trans.rotate(Math.toRadians(settingsOpenedState*90),buttonHeight/2,buttonHeight/2);
		graphics.drawImage(ImageUtil.getSettingsIcon(), trans, null);
		graphics.setColor(new Color(128,128,128,(int) (settingsHoverAlpha*255)));
		graphics.fill(arc);
		if(settingsOpenedState>0) {
			graphics.setColor(grid);
			AffineTransform trans2=new AffineTransform();
			trans2.translate(x+buttonHeight/2-15/2.0, y+buttonHeight+20);
			trans2.scale(1, -settingsOpenedState);
			graphics.drawImage(ImageUtil.getSettingsPointer(), trans2, null);
			int size=buttonHeight-20;
			int rectHeight=size*3+4*10;
			graphics.fill(new RoundRectangle2D.Double(x,y+buttonHeight+20,buttonHeight,rectHeight*settingsOpenedState, 4, 4));
			graphics.setColor(ColorScheme.DARK_BLUE.getDisplayColor(settingsOpenedState));
			graphics.fill(new RoundRectangle2D.Double(x+10,y+buttonHeight+20 +10*settingsOpenedState,size,size*settingsOpenedState, 4, 4));
			graphics.setColor(ColorScheme.LIGHT_GREEN.getDisplayColor(settingsOpenedState));
			graphics.fill(new RoundRectangle2D.Double(x+10,y+buttonHeight+20 +(10*2+size)*settingsOpenedState,size,size*settingsOpenedState, 4, 4));
			graphics.setColor(ColorScheme.RED.getDisplayColor(settingsOpenedState));
			graphics.fill(new RoundRectangle2D.Double(x+10,y+buttonHeight+20 +(10*3+size*2)*settingsOpenedState,size,size*settingsOpenedState, 4, 4));
		}
	}
	private static void drawKnot(Graphics2D graphics, int width, int height, int gridX, int gridY, double state) {
		double x=width/2-boardSize/2+boardSize/3/2-symbolSize/2+boardSize/3*gridX;
		double y=height/2-boardSize/2+boardSize/3/2-symbolSize/2+boardSize/3*gridY;
		
		graphics.draw(new Arc2D.Double(x-game.getXOffset(), y, symbolSize, symbolSize, 90, state*360, 0));
	}
	private static void drawCross(Graphics2D graphics, int width, int height, int gridX, int gridY, double state) {
		double x=width/2-boardSize/2+boardSize/3/2+boardSize/3*gridX-game.getXOffset();
		double y=height/2-boardSize/2+boardSize/3/2+boardSize/3*gridY;
		
		graphics.draw(new Line2D.Double(x-symbolSize/2, y-symbolSize/2, x-symbolSize/2+symbolSize*state, y-symbolSize/2+symbolSize*state));
		graphics.draw(new Line2D.Double(x+symbolSize/2, y-symbolSize/2, x+symbolSize/2-symbolSize*state, y-symbolSize/2+symbolSize*state));
	}
	
	public static void onMouseReleased(MouseEvent e) {
		if(hoveredSettingsElement!=-1) {
			switch(hoveredSettingsElement) {
			case 0:
				ColorSchemeManager.setScheme(ColorScheme.DARK_BLUE);
				break;
		    case 1:
			    ColorSchemeManager.setScheme(ColorScheme.LIGHT_GREEN);
			    break;
		    case 2:
			    ColorSchemeManager.setScheme(ColorScheme.RED);
			    break;
		    }
			return;
		}
		
		int mouseX=Main.getMouseX();
		int mouseY=Main.getMouseY();
		
		double dis=Math.pow(mouseX-(Main.getWidth()-buttonHeight/2-25), 2)+Math.pow(mouseY-(25+buttonHeight/2), 2);
		double max=buttonHeight/2;
		max=max*max;
		if(dis<=max) {
			settingsOpened=!settingsOpened;
			return;
		} else settingsOpened=false;
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
