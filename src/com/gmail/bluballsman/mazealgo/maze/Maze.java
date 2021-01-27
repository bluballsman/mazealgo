package com.gmail.bluballsman.mazealgo.maze;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import com.gmail.bluballsman.mazealgo.loc.Direction;
import com.gmail.bluballsman.mazealgo.structure.StructureSlot;
import com.gmail.bluballsman.mazealgo.tile.Tile;

public class Maze {
	protected int width;
	protected int height;
	protected Tile[][] tiles;
	protected ArrayList<StructureSlot> structures = new ArrayList<StructureSlot>();
	protected StructureSlot centerSlot;
	protected Random random = new Random();
	
	public Maze(int width, int height) {
	   this.width = width;
	   this.height = height;
		tiles = new Tile[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				tiles[x][y] = new Tile(this, x, y);
			}
		}
	}
	
	public Maze(int width, int height, long randomSeed) {
		this(width, height);
		random.setSeed(randomSeed);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
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
		Tile t = getTile(x, y);
		
		return t == null || t.isGround();
	}
	
	public boolean isGround(Point p) {
		return isGround(p.x, p.y);
	}
	
	public boolean isStructure(int x, int y) {
		Tile t = getTile(x, y);
		
		return t != null && t.isStructure();
	}
	
	public boolean isStructure(Point p) {
		return isStructure(p.x, p.y);
	}
	
	public Tile getTile(int x, int y) {
		return (x >= 0 && x < width && y >= 0 && y < height) ? tiles[x][y] : null;
	}
	
	public Tile getTile(Point p) {
		return getTile(p.x, p.y);
	}
	
	public Point freeRandomEvenPoint(int minX, int minY, int maxX, int maxY) {
		minX = minX % 2 == 0 ? minX : minX + 1;
		minY = minY % 2 == 0 ? minY : minY + 1;
		maxX = maxX % 2 == 0 ? maxX : maxX - 1;
		maxY = maxY % 2 == 0 ? maxY : maxY - 1;
		
		Point p = new Point();
		int nEvensX = ((maxX - minX) / 2) + 1;
		int nEvensY = ((maxY - minY) / 2) + 1;
		
		do {
			p.x = 2 * random.nextInt(nEvensX) + minX;
			p.y = 2 * random.nextInt(nEvensY) + minY;
		} 
		while(isStructure(p));

		return p;
	}
	
	public Point freeRandomOddPoint(int minX, int minY, int maxX, int maxY) {
		minX = minX % 2 == 1 ? minX : minX + 1;
		minY = minY % 2 == 1 ? minY : minY + 1;
		maxX = maxX % 2 == 1 ? maxX : maxX - 1;
		maxY = maxY % 2 == 1 ? maxY : maxY - 1;
		
		Point p = new Point();
		int nOddsX = ((maxX - minX) / 2) + 1;
		int nOddsY = ((maxY - minY) / 2) + 1;
		
		do {
			p.x = (2 * random.nextInt(nOddsX)) + minX;
			p.y = (2 * random.nextInt(nOddsY)) + minY;
		} 
		while(isStructure(p));
		
		return p;
	}
	
	public ArrayList<StructureSlot> findMatches(String strucPattern) {
		ArrayList<StructureSlot> matches = new ArrayList<StructureSlot>();
		StructureSlot s = new StructureSlot(this, strucPattern);
		
		for(int rotations = 0; rotations < 4; rotations++) {
			s.rotate(1);
			for(int y = 0; y < height - s.getHeight(); y++) {
				for(int x = 0; x < width - s.getWidth(); x++) {
					s.setLocation(x, y);
					
					if(s.canPlace()) {
						matches.add(s.clone());
					}
				}
			}
		}
		
		return matches;
	}
	
	public StructureSlot placeStructure(String strucPattern) {
		ArrayList<StructureSlot> matches = findMatches(strucPattern);
		
		if(matches.size() == 0) {
			return null;
		}
		else {
			StructureSlot chosenSlot = matches.get(random.nextInt(matches.size()));
			chosenSlot.markStructureTiles();
			structures.add(chosenSlot);
			
			return chosenSlot;
		}
	}
	
	public StructureSlot excavateRoom(String roomPattern) {
		Point randomEven;
		StructureSlot roomSlot = new StructureSlot(this, roomPattern, random.nextInt(4));
		
		do { 
			randomEven = freeRandomEvenPoint(1, 1, width - 1 - roomSlot.getWidth(), height - 1 - roomSlot.getHeight());
			roomSlot.setLocation(randomEven);
		}
		while(roomSlot.doesSlotHaveStructure());
		
		roomSlot.markStructureTiles();
		roomSlot.drawStructureTiles();
		structures.add(roomSlot);
		
		return roomSlot;
	}
	
	public void setGround(int x, int y, boolean isGround) {
		tiles[x][y].setGround(isGround);
	}
	
	public void setGround(Point p, boolean isGround) {
		setGround(p.x, p.y, isGround);
	}
	
	public void setStructureFlag(int x, int y, boolean isStructure) {
		tiles[x][y].setStructureFlag(isStructure);
	}
	
	public void setStructureFlag(Point p, boolean isStructure) {
		setStructureFlag(p.x, p.y, isStructure);
	}
	
	public void fillMaze() {
		Point currentPoint = freeRandomOddPoint(1, 1, width - 1, height - 1);
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
	
	public void knockDownWalls(float openWallPercentage) {
		ArrayList<Point> availableWalls = new ArrayList<Point>();
		
		// All the available walls to knock down are on points where x and y are even and odd
		// but not both. That's what (1 + y % 2) is for, it makes it so that x starts on an even number if y is odd
		// and vice versa
		for(int y = 1; y < height - 1; y++) {
			for(int x = 1 + y % 2; x < width - 1; x+=2) {
				if(!isGround(x, y) && !isStructure(x, y)) {
					availableWalls.add(new Point(x, y));
				}
			}
		}
		
		int wallsToDestroy = Math.round(availableWalls.size() * openWallPercentage);
		for(int i = 0; i < wallsToDestroy; i++) {
			Point randomWall = availableWalls.get(random.nextInt(availableWalls.size()));
			
			availableWalls.remove(randomWall);
			setGround(randomWall, true);
		}
	}
	
	public void markCenter() {
		String centerBlueprint = "XX11111XX.X1111111X.111111111.111111111.111111111.111111111.111111111.X1111111X.XX11111XX";
		centerSlot = new StructureSlot(this, centerBlueprint);
		int radius = (centerSlot.getWidth() / 2);
		Point center = getCenterPoint();
		Point corner = new Point(center.x - radius, center.y - radius);
		
		centerSlot.setLocation(corner);
		centerSlot.markStructureTiles();
		structures.add(centerSlot);
	}
	
	public void drawStartingPositions() {
		String blueprintString = "00000.01110.01110.01110.01110";
		StructureSlot startingSlot = new StructureSlot(this, blueprintString, -random.nextInt(2));
		StructureSlot mirrorSlot = startingSlot.getMirrorSlot();
		
		startingSlot.markStructureTiles();
		mirrorSlot.markStructureTiles();
		startingSlot.drawStructureTiles();
		mirrorSlot.drawStructureTiles();
		structures.add(startingSlot);
		structures.add(mirrorSlot);
	}
	
	public void drawCenter() {
		centerSlot.drawStructureTiles();
	}
}
