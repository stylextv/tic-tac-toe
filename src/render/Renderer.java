package de.ttt.render;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import de.ttt.main.Main;

public class Renderer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics graphics) {
		Main.setWidth(getWidth());
		Main.setHeight(getHeight());
		Main.render((Graphics2D) graphics);
	}
	
}
