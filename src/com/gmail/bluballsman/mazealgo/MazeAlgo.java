package com.gmail.bluballsman.mazealgo;

import java.awt.Canvas;

import javax.swing.JFrame;

public class MazeAlgo {
	static final int tileSize = 20;
	static final int width = 63;
	static final int height = 23;
	static Maze m = new Maze(width, height);
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Maze");
		Canvas canvas = new MazeCanvas(m, tileSize);
		m.fillMaze();
		m.test();
		canvas.setSize(tileSize * width, tileSize * height);
		frame.setSize(canvas.getSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.add(canvas);
		frame.pack();
	}
	
	
}
