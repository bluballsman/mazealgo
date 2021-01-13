package com.gmail.bluballsman.mazealgo;

import java.awt.Canvas;

import javax.swing.JFrame;

public class Main {
	static final int tileSize = 10;
	static final int width = 61;
	static final int height = 63;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Maze");
		Canvas canvas = new MazeCanvas(width, height, tileSize);
		frame.add(canvas);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
}
