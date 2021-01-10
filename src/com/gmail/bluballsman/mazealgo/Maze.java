package com.gmail.bluballsman.mazealgo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import com.gmail.bluballsman.mazealgo.loc.Direction;
import com.gmail.bluballsman.mazealgo.structure.StructureSlot;
import com.gmail.bluballsman.mazealgo.tile.Tile;

public class Maze {
	private int width;
	private int height;
	private int centerRadius;
	private Tile[][] tiles;
	private ArrayList<StructureSlot> structures = new ArrayList<StructureSlot>();
	private StructureSlot centerStrucSlot;
	private Random random = new Random();
	
	public Maze(int width, int height, int centerRadius) {
	   this.width = width;
	   this.height = height;
	   this.centerRadius = centerRadius;
		tiles = new Tile[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				tiles[x][y] = new Tile(this, x, y);
			}
		}
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
		StructureSlot centerSlot = new StructureSlot(this, centerBlueprintString);
		Point center = getCenterPoint();
		Point corner = new Point(center.x - centerRadius, center.y - centerRadius);
		
		centerSlot.setLocation(corner);
		centerSlot.markStructureTiles();
		structures.add(centerSlot);
		centerStrucSlot = centerSlot;
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
	
	public boolean doesStructureSlotCollide(StructureSlot slot) {
		if(isStructure(slot.getLocation())) {
			return true;
		}
		
		for(StructureSlot s : structures) {
			if(slot.intersects(s)) {
				return true;
			}
		}
		return false;
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
		tiles[x][y].setGround(isGround);
		
		if(symmetrical) {
			tiles[width - x - 1][height - y - 1].setGround(isGround); 
		}
	}
	
	public void setGround(Point p, boolean isGround, boolean symmetrical) {
		setGround(p.x, p.y, isGround, symmetrical);
	}
	
	public void setStructureFlag(int x, int y, boolean isStructure, boolean symmetrical) {
		tiles[x][y].setStructureFlag(isStructure);
		
		if(symmetrical) {
			tiles[width - x - 1][height - y - 1].setStructureFlag(isStructure); 
		}
	}
	
	public void setStructureFlag(Point p, boolean isStructure, boolean symmetrical) {
		setStructureFlag(p.x, p.y, isStructure, symmetrical);
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
	
	public void excavateRoom(String patternString, boolean symmetrical) {
		Point randomEven;
		StructureSlot roomSlot = new StructureSlot(this, patternString);
		roomSlot.rotate(random.nextInt(4));
		
		do { 
			randomEven = randomEvenPoint(1, 1, width - 1 - roomSlot.getWidth(), height - 1 - roomSlot.getHeight());
			roomSlot.setLocation(randomEven);
		}
		while(doesStructureSlotCollide(roomSlot));
		
		roomSlot.markStructureTiles();
		roomSlot.drawStructureTiles();
		structures.add(roomSlot);
		
		if(symmetrical) {
			StructureSlot mirrorSlot = roomSlot.getMirrorSlot();
			
			mirrorSlot.markStructureTiles();
			mirrorSlot.drawStructureTiles();
			structures.add(mirrorSlot);
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
		centerStrucSlot.markStructureTiles();
	}
	
	public boolean placeStructure(String strucPattern, boolean symmetrical) {
		ArrayList<StructureSlot> matches = new ArrayList<StructureSlot>();
		
		for(int rotations = 0; rotations < 4; rotations++) {
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					StructureSlot s = new StructureSlot(this, strucPattern);
					s.rotate(rotations);
					s.setLocation(x, y);
					
					if(s.canPlace() && !doesStructureSlotCollide(s)) {
						matches.add(s);
					}
				}
			}
		}
		
		int nMatches = matches.size();
		if(nMatches == 0) {
			return false;
		}
		else {
			StructureSlot chosenSlot = matches.get(random.nextInt(nMatches));
			chosenSlot.markStructureTiles();
			structures.add(chosenSlot);
			
			if(symmetrical) {
				StructureSlot mirrorSlot = chosenSlot.getMirrorSlot();
				
				mirrorSlot.markStructureTiles();
				structures.add(mirrorSlot);
			}
			return true;
		}
		
	}
	
	public void test2() {
		String testString = "000.010";
		placeStructure(testString, true);
		placeStructure("111.101.111", true);
	}
}
