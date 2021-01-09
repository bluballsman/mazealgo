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
	private int centerRadius;
	private boolean[][] walls;
	private boolean[][] structureFlags;
	private HashMap<Point, StructureBlueprint> structures = new HashMap<Point, StructureBlueprint>();
	private Random random = new Random();
	
	public Maze(int width, int height, int centerRadius) {
	   this.width = width;
	   this.height = height;
	   this.centerRadius = centerRadius;
		walls = new boolean[width][height];
		structureFlags = new boolean[width][height];
		markCenter();
	}
	
	public Maze(int width, int height, int centerRadius, long randomSeed) {
		this(width, height, centerRadius);
		random.setSeed(randomSeed);
	}
	
	private void markCenter() {
		int diameter = (centerRadius * 2) + 1;
		String ones = "1".repeat(diameter);
		String centerBlueprintString = ones + ("." + ones).repeat(diameter - 1);
		StructureBlueprint centerBlueprint = new StructureBlueprint(centerBlueprintString);
		Point center = getCenterPoint();
		Point corner = new Point(center.x - centerRadius, center.y - centerRadius);
		
		for(int y = corner.y; y < diameter; y++) {
			for(int x = corner.x; x < diameter; x++) {
				setStructureFlag(x, y, true, false);
			}
		}
		structures.put(corner, centerBlueprint);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getCenterRadius() {
		return centerRadius;
	}
	
	public Point getCenterPoint() {
		return new Point((width - 1) / 2, (height - 1) / 2);
	}
	
	public Point getMirrorPoint(int x, int y) {
		return new Point(width - x - 1, height - y - 1);
	}
	
	public Point getMirrorPoint(Point p) {
		return getMirrorPoint(p.x, p.y);
	}
	
	public boolean isGround(int x, int y) {
		return x < 0 || x >= width || y < 0 || y >= height || walls[x][y];
	}
	
	public boolean isGround(Point p) {
		return isGround(p.x, p.y);
	}
	
	public boolean isStructure(int x, int y) {
		return x > 0 && x < width && y > 0 && y < height && structureFlags[x][y];
	}
	
	public Tile getTile(int x, int y) {
		boolean[] surroundingTiles = {
				isGround(x, y + 1),
				isGround(x + 1, y),
				isGround(x, y - 1),
				isGround(x - 1, y)
		};
		boolean isGround = isGround(x, y);
		boolean structureFlag = isStructure(x, y);
		
		return new Tile(isGround, structureFlag, surroundingTiles);
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
		if(isStructure(px, py)) {
			return true;
		}
		
		Rectangle rect = new Rectangle();
		rect.setBounds(px, py, structure.getWidth(), structure.getHeight());
		
		for(Point p : structures.keySet()) {
			StructureBlueprint s = structures.get(p);
			Rectangle r = new Rectangle();
			r.setBounds(p.x, p.y, s.getWidth(), s.getHeight());
			
			if(r.intersects(rect)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean doesStructureCollide(Point point, StructureBlueprint structure) {
		return doesStructureCollide(point.x, point.y, structure);
	}
	
	public Point randomEvenPoint(int minX, int minY, int maxX, int maxY) {
		minX = minX % 2 == 0 ? minX : minX + 1;
		minY = minY % 2 == 0 ? minY : minY + 1;
		maxX = maxX % 2 == 0 ? maxX : maxX - 1;
		maxY = maxY % 2 == 0 ? maxY : maxY - 1;

		int nEvensX = ((maxX - minX) / 2) + 1;
		int nEvensY = ((maxY - minY) / 2) + 1;
		int x = 2 * random.nextInt(nEvensX) + minX;
		int y = 2 * random.nextInt(nEvensY) + minY;
		
		return new Point(x, y);
	}
	
	public Point randomOddPoint(int minX, int minY, int maxX, int maxY) {
		minX = minX % 2 == 1 ? minX : minX + 1;
		minY = minY % 2 == 1 ? minY : minY + 1;
		maxX = maxX % 2 == 1 ? maxX : maxX - 1;
		maxY = maxY % 2 == 1 ? maxY : maxY - 1;
		
		int nOddsX = ((maxX - minX) / 2) + 1;
		int nOddsY = ((maxY - minY) / 2) + 1;
		int x = (2 * random.nextInt(nOddsX)) + minX;
		int y = (2 * random.nextInt(nOddsY)) + minY;
		
		return new Point(x, y);
	}
	
	public void setGround(int x, int y, boolean isGround, boolean symmetrical) {
		walls[x][y] = isGround;
		
		if(symmetrical) {
			walls[width - x - 1][height - y - 1] = isGround; 
		}
	}
	
	public void setGround(Point p, boolean isGround, boolean symmetrical) {
		setGround(p.x, p.y, isGround, symmetrical);
	}
	
	public void setStructureFlag(int x, int y, boolean isStructure, boolean symmetrical) {
		structureFlags[x][y] = isStructure;
		
		if(symmetrical) {
			structureFlags[width - x - 1][height - y - 1] = isStructure; 
		}
	}
	
	public void setStructureFlag(Point p, boolean isStructure, boolean symmetrical) {
		structureFlags[p.x][p.y] = isStructure;
		
		if(symmetrical) {
			structureFlags[width - p.x - 1][height - p.y - 1] = isStructure; 
		}
	}
	
	public void fillMaze(boolean symmetrical) {
		Point currentPoint = new Point(1, 1);
		Stack<Point> path = new Stack<Point>();
		path.push(currentPoint);
		setGround(currentPoint, true, symmetrical);
		
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
				setGround(currentPoint, true, symmetrical);
				currentPoint.translate(chosenDirection.X_OFFSET, chosenDirection.Y_OFFSET);
				setGround(currentPoint, true, symmetrical);
				path.push(new Point(currentPoint));
				
			}
			else {
				currentPoint = path.pop();
			}
		}
	}
	
	public void excavateRoom(StructureBlueprint blueprint, boolean symmetrical) {
		Point randomEven;
		blueprint.rotate(random.nextInt(4));
		
		do { 
			randomEven = randomEvenPoint(1, 1, width - 1 - blueprint.getWidth(), height - 1 - blueprint.getHeight());
		}
		while(doesStructureCollide(randomEven, blueprint));
		
		for(int y = 0; y < blueprint.getHeight(); y++) {
			for(int x = 0; x < blueprint.getWidth(); x++) {
				int tileX = x + randomEven.x;
				int tileY = y + randomEven.y;
				setGround(tileX, tileY, blueprint.getBlueprint()[x][y], symmetrical);
				setStructureFlag(tileX, tileY, true, symmetrical);
			}
		}
		structures.put(randomEven, blueprint);
		
		if(symmetrical) {
			StructureBlueprint mirrorBlueprint = new StructureBlueprint(blueprint);
			Point mirrorPoint = getMirrorPoint(randomEven);
			mirrorBlueprint.rotate(2);
			structures.put(mirrorPoint, mirrorBlueprint);
		}
	}
	
	public void knockDownWalls(float openWallPercentage, boolean symmetrical) {
		ArrayList<Point> availableWalls = new ArrayList<Point>();
		
		for(int y = 1; y < height - 1; y++) {
			for(int x = 1 + y % 2; x < width - 1; x+=2) {
				if(!isGround(x, y) && !isStructure(x, y)) {
					availableWalls.add(new Point(x, y));
				}
			}
		}
		
		int wallsToDestroy = Math.round(availableWalls.size() * openWallPercentage);
		
		while(wallsToDestroy > 0) {
			Point randomWall = availableWalls.get(random.nextInt(availableWalls.size()));
			availableWalls.remove(randomWall);
			setGround(randomWall, true, symmetrical);
			wallsToDestroy--;
			
			if(symmetrical) {
				Point mirrorPoint = getMirrorPoint(randomWall);
				availableWalls.remove(mirrorPoint);
				wallsToDestroy--;
			}
		}
	}
	
	public void openUpCenter() {
		Point center = getCenterPoint();
		
		for(int y = -centerRadius; y <= centerRadius; y++) {
			for(int x = -centerRadius; x <= centerRadius; x++) {
				setGround(center.x + x, center.y + y, true, false);
			}
		}
	}
	
	public void test() {
		String string = "0000000.0111110.0111110.1111110.0000000";
		StructureBlueprint s = new StructureBlueprint(string);
		
		excavateRoom(s, true);
	}
	
}
