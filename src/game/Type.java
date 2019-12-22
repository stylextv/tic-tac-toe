package de.ttt.game;

public enum Type {
	
	KNOT,CROSS;
	
	public Type opposite() {
		if(this==KNOT) return KNOT;
		return CROSS;
	}
	
}
