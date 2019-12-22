package de.ttt.game;

import de.ttt.ai.MinimaxAi;
import de.ttt.math.MathUtil;

public class Game {
	
	private Field[] grid=new Field[9];
	private Type whosTurn;
	
	private double reactionTimer=0;
	private boolean won=false;
	private int wonP1X=-1,wonP1Y,wonP2X,wonP2Y;
	
	private double xOffset=0;
	private int buttonAlpha=0;
	private double wonTimer=0;
	
	public Game() {
		whosTurn=Type.CROSS;
		for(int i=0; i<9; i++) grid[i]=new Field();
	}
	
	public void reset() {
		won=false;
		whosTurn=Type.CROSS;
		for(int i=0; i<9; i++) grid[i].setType(null);
	}
	
	public void update() {
		if(won) {
			if(wonTimer<1) {
				wonTimer+=0.015;
				if(wonTimer>1)wonTimer=1;
				xOffset=MathUtil.sigmoid(wonTimer)*155;
				buttonAlpha=(int) ((MathUtil.sigmoid(wonTimer)-0.5)*2*255);
				if(buttonAlpha<0) buttonAlpha=0;
			}
		} else {
			if(wonTimer>0) {
				wonTimer-=0.02;
				if(wonTimer<0)wonTimer=0;
				xOffset=MathUtil.sigmoid(wonTimer)*155;
				buttonAlpha=(int) ((MathUtil.sigmoid(wonTimer)-0.5)*2*255);
				if(buttonAlpha<0) buttonAlpha=0;
			}
			if(whosTurn==Type.KNOT) {
				reactionTimer=reactionTimer+1;
				if(reactionTimer>=30) {
					reactionTimer=0;
					doMove();
					checkForWinner();
					whosTurn=Type.CROSS;
				}
			} else reactionTimer=0;
		}
		
		for(Field f:grid) f.update();
	}
	private void doMove() {
		int m=MinimaxAi.doMove(this);
		getField(m).setType(whosTurn);
	}
	public void checkForWinner() {
		if(!won) {
			if(checkForWinner(Type.CROSS)||checkForWinner(Type.KNOT)) {
				won=true;
			} else {
				for(Field f:grid) {
					if(f.getType()==null) return;
				}
				won=true;
				wonP1X=-1;
			}
		}
	}
	public boolean checkForWinner(Type type) {
		return checkHor(type)||checkVer(type)||checkDia(type);
	}
	public boolean checkVer(Type type) {
		for(int x=0; x<3; x++) {
			boolean b=true;
			for(int y=0; y<3; y++) {
				if(getField(y*3+x).getType()!=type) {
					b=false;
					break;
				}
			}
			if(b) {
				wonP1X=x;
				wonP1Y=0;
				wonP2X=x;
				wonP2Y=2;
				return true;
			}
		}
		return false;
	}
	public boolean checkHor(Type type) {
		for(int y=0; y<3; y++) {
			boolean b=true;
			for(int x=0; x<3; x++) {
				if(getField(y*3+x).getType()!=type) {
					b=false;
					break;
				}
			}
			if(b) {
				wonP1X=0;
				wonP1Y=y;
				wonP2X=2;
				wonP2Y=y;
				return true;
			}
		}
		return false;
	}
	public boolean checkDia(Type type) {
		boolean b1=true;
		boolean b2=true;
		for(int x=0; x<3; x++) {
			if(getField(x*3+x).getType()!=type) b1=false;
			if(getField((2-x)*3+x).getType()!=type) b2=false;
		}
		if(b1) {
			wonP1X=0;
			wonP1Y=0;
			wonP2X=2;
			wonP2Y=2;
			return true;
		} else if(b2) {
			wonP1X=0;
			wonP1Y=2;
			wonP2X=2;
			wonP2Y=0;
			return true;
		}
		return false;
	}
	
	public boolean isOver() {
		return won;
	}
	public int getWonP1X() {
		return wonP1X;
	}
	public int getWonP1Y() {
		return wonP1Y;
	}
	public int getWonP2X() {
		return wonP2X;
	}
	public int getWonP2Y() {
		return wonP2Y;
	}
	
	public double getXOffset() {
		return xOffset;
	}
	public int getButtonAlpha() {
		return buttonAlpha;
	}
	
	public Field getField(int index) {
		return grid[index];
	}
	public void setWhosTurn(Type type) {
		whosTurn=type;
	}
	public Type getWhosTurn() {
		return whosTurn;
	}
	
	
}
