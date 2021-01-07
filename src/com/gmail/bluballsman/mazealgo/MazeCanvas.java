package com.gmail.bluballsman.mazealgo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.gmail.bluballsman.mazealgo.tile.Tile;
import com.gmail.bluballsman.mazealgo.tile.Tile.Type;

public class MazeCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private int tileSizePixels;
	private Maze maze;
	
	public MazeCanvas(Maze maze, int tileSizePixels) {
		this.tileSizePixels = tileSizePixels;
		this.maze = maze;
		this.addMouseListener(new CanvasListener());
	}
	
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	@Override
	public void paint(Graphics g) {
		for(int y = 0; y < maze.getWidth(); y++) {
			for(int x = 0; x < maze.getLength(); x++) {
				int paintX = tileSizePixels * x;
				int paintY = tileSizePixels * y;
				Tile t = maze.getTile(x, y);
				Color color = t.isGround() ? new Color(255, 255, 255) : new Color(0, 0, 0);
				
				if(t.getType() == Type.STRAIGHT && !t.isGround()) {
					switch(t.getRotations()) {
					case 0:
						color = new Color(0, 0, 123);
						break;
					case 1:
						color = new Color(0, 123, 0);
						break;
					case 2:
						color = new Color(123, 0, 0);
						break;
					default:
						color = new Color(123, 123, 123);
						break;
					}
				}
				
				g.setColor(color);
				g.fillRect(paintX, paintY, tileSizePixels, tileSizePixels);
			}
		}
	}
	
	
	private class CanvasListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {
			maze = new Maze(maze.getLength(), maze.getWidth());
			maze.fillMaze();
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
		
	}
	
}
