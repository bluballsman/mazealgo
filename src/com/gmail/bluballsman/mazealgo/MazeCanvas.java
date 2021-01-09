package com.gmail.bluballsman.mazealgo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.gmail.bluballsman.mazealgo.tile.Tile;

public class MazeCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private int mazeWidth;
	private int mazeHeight;
	private int tileSize;
	private Maze maze;
	
	public MazeCanvas(int mazeWidth, int mazeHeight, int tileSize) {
		this.mazeWidth = mazeWidth;
		this.mazeHeight = mazeHeight;
		this.tileSize = tileSize;
		addMouseListener(new CanvasListener());
		setSize(mazeWidth * tileSize, mazeHeight * tileSize);
	}
	
	@Override
	public void paint(Graphics g) {
		maze = new Maze(mazeWidth, mazeHeight, 4);
		maze.test();
		maze.fillMaze(true);
		maze.knockDownWalls(.06F, true);
		maze.openUpCenter();
		for(int y = 0; y < maze.getHeight(); y++) {
			for(int x = 0; x < maze.getWidth(); x++) {
				int paintX = tileSize * x;
				int paintY = tileSize * y;
				Tile t = maze.getTile(x, y);
				Color color = t.isGround() ? new Color(255, 255, 255) : new Color(0, 0, 0);
				g.setColor(color);
				g.fillRect(paintX, paintY, tileSize, tileSize);
			}
		}
		Point centerPoint = maze.getCenterPoint();
		g.setColor(new Color(255, 0, 0));
		g.fillRect(centerPoint.x * tileSize, centerPoint.y * tileSize, tileSize, tileSize);
	}
	
	
	private class CanvasListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
		
	}
	
}
