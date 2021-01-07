package com.gmail.bluballsman.mazealgo;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import com.gmail.bluballsman.mazealgo.loc.Direction;
import com.gmail.bluballsman.mazealgo.loc.Point;
import com.gmail.bluballsman.mazealgo.tile.Tile;

public class Maze {
	private int length;
	private int width;
	private boolean[][] tiles;
	private Random random = new Random();
	
	public Maze(int length, int width) {
	   this.length = length;
	   this.width = width;
		tiles = new boolean[length][width];
		for(int y = 0; y < width; y++) {
			for(int x = 0; x < length; x++) {
				tiles[x][y] = false;
			}
		}
	}
	
	public int getLength() {
		return length;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isGround(int x, int y) {
		return x < 0 || x >= length || y < 0 || y >= width || tiles[x][y];
	}
	
	public boolean isGround(Point p) {
		return isGround(p.x, p.y);
	}
	
	public Tile getTile(int x, int y) {
		boolean[] surroundingTiles = {
				isGround(x, y + 1),
				isGround(x + 1, y),
				isGround(x, y - 1),
				isGround(x - 1, y)
		};
		boolean isGround = isGround(x, y);
		
		return new Tile(isGround, surroundingTiles);
	}
	
	public Tile getTile(Point p) {
		return getTile(p.x, p.y);
	}
	
	public void setGround(int x, int y, boolean isGround) {
		tiles[x][y] = isGround;
	}
	
	public void setGround(Point p, boolean isGround) {
		setGround(p.x, p.y, isGround);
	}
	
	public void fillMaze() {
		Point currentPoint = new Point(1, 1);
		Stack<Point> path = new Stack<Point>();
		path.push(currentPoint);
		setGround(currentPoint, true);
		
		while(!path.isEmpty()) {
			ArrayList<Direction> availableDirections = new ArrayList<Direction>();
			if(!isGround(currentPoint.x, currentPoint.y + 2)) {
				availableDirections.add(Direction.NORTH);
			}
			if(!isGround(currentPoint.x + 2, currentPoint.y)) {
				availableDirections.add(Direction.EAST);
			}
			if(!isGround(currentPoint.x, currentPoint.y - 2)) {
				availableDirections.add(Direction.SOUTH);
			}
			if(!isGround(currentPoint.x - 2, currentPoint.y)) {
				availableDirections.add(Direction.WEST);
			}
			
			if(!availableDirections.isEmpty()) {
				Direction chosenDirection = availableDirections.get(random.nextInt(availableDirections.size()));
				Point dummyPoint = new Point(currentPoint);
				
				setGround(dummyPoint.translate(chosenDirection), true);
				setGround(dummyPoint.translate(chosenDirection), true);
				path.push(dummyPoint);
				currentPoint = dummyPoint;
			}
			else {
				currentPoint = path.pop();
			}
		}
	}
	
}
