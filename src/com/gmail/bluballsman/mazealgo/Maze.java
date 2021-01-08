package com.gmail.bluballsman.mazealgo;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import com.gmail.bluballsman.mazealgo.loc.Direction;
import com.gmail.bluballsman.mazealgo.structure.StructureBlueprint;
import com.gmail.bluballsman.mazealgo.tile.Tile;

public class Maze {
	private int width;
	private int height;
	private boolean[][] walls;
	private boolean[][] structures;
	private HashMap<Point, StructureBlueprint> structureMap = new HashMap<Point, StructureBlueprint>();
	private Random random = new Random();
	
	public Maze(int width, int height) {
	   this.width = width;
	   this.height = height;
		walls = new boolean[width][height];
		structures = new boolean[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				walls[x][y] = false;
				structures[x][y] = false;
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Point getMirroredPoint(int x, int y) {
		return new Point(width - x - 1, height - y - 1);
	}
	
	public boolean isGround(int x, int y) {
		return x < 0 || x >= width || y < 0 || y >= height || walls[x][y];
	}
	
	public boolean isGround(Point p) {
		return isGround(p.x, p.y);
	}
	
	public boolean isStructure(int x, int y) {
		return x > 0 && x < width && y > 0 && y < height && structures[x][y];
	}
	
	public boolean isStructure(Point p) {
		return isStructure(p.x, p.y);
	}
	
	public Tile getTile(int x, int y) {
		boolean[] surroundingTiles = {
				isGround(x, y + 1),
				isGround(x + 1, y),
				isGround(x, y - 1),
				isGround(x - 1, y)
		};
		boolean isGround = isGround(x, y);
		boolean isStructure = isStructure(x, y);
		
		return new Tile(isGround, isStructure, surroundingTiles);
	}
	
	public Tile getTile(Point p) {
		return getTile(p.x, p.y);
	}
	
	public boolean matchesStructurePattern(int px, int py, StructureBlueprint structure) {
		for(int y = 0; y < structure.getHeight(); y++) {
			for(int x = 0; x < structure.getWidth(); x++) {
				if(structure.getBlueprint()[x][y] != isGround(x + px, y + py)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean matchesStructurePattern(Point p, StructureBlueprint structure) {
		return matchesStructurePattern(p.x, p.y, structure);
	}
	
	public boolean doesStructureCollide(int px, int py, StructureBlueprint structure) {
		Rectangle rect = new Rectangle();
		rect.setBounds(px, py, structure.getWidth(), structure.getHeight());
		
		for(Point p : structureMap.keySet()) {
			StructureBlueprint s = structureMap.get(p);
			Rectangle r = new Rectangle();
			r.setBounds(p.x, p.y, s.getWidth(), s.getHeight());
			
			if(r.intersects(rect)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean doesStructureCollide(Point point, StructureBlueprint structure) {
		return doesStructureCollide(point.x, point.y, structure);
	}
	
	public Point randomOddPoint(int minX, int minY, int maxX, int maxY) {
		int nOddsx = ((maxX - minX) / 2) + 1;
		int nOddsy = ((maxY - minY) / 2) + 1;
		int x = (2 * random.nextInt(nOddsx)) + minX;
		int y = (2 * random.nextInt(nOddsy)) + minY;
		
		return new Point(x, y);
	}
	
	public void setGround(int x, int y, boolean isGround) {
		walls[x][y] = isGround;
	}
	
	public void setGround(Point p, boolean isGround) {
		setGround(p.x, p.y, isGround);
	}
	
	public void setStructure(int x, int y, boolean isStructure) {
		structures[x][y] = isStructure;
	}
	
	public void setStructure(Point p, boolean isStructure) {
		setStructure(p.x, p.y, isStructure);
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
				
				currentPoint.translate(chosenDirection.X_OFFSET, chosenDirection.Y_OFFSET);
				setGround(currentPoint, true);
				currentPoint.translate(chosenDirection.X_OFFSET, chosenDirection.Y_OFFSET);
				setGround(currentPoint, true);
				path.push(new Point(currentPoint));
			}
			else {
				currentPoint = path.pop();
			}
		}
	}
	
	public void test() {
		String string = "000.010";
		StructureBlueprint s = new StructureBlueprint(string);
		
		s.rotate(0);
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(matchesStructurePattern(x, y, s)) {
					setStructure(x, y, true);
				}
			}
		}

	}
	
}
