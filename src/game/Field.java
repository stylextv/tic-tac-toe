package de.ttt.game;

import de.ttt.math.MathUtil;

public class Field {
	
	private Type displayType,type;
	
	private double state=0;
	private double animationTimer=0;
	
	public Field() {
		
	}
	
	public void update() {
		if(type!=null) {
			displayType=type;
			animationTimer+=0.025;
			if(animationTimer>1) animationTimer=1;
		} else {
			animationTimer=animationTimer*0.95-0.025;
			if(animationTimer<0) animationTimer=0;
			if(animationTimer==0) displayType=null;
		}
		state=(MathUtil.sigmoid(animationTimer*0.5+0.5)-0.5)*2;
	}
	
	public Type getDisplayType() {
		return displayType;
	}
	public double getState() {
		return state;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
}
