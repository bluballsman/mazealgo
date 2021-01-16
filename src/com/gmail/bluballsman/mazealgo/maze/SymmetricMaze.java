package com.gmail.bluballsman.mazealgo.maze;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import com.gmail.bluballsman.mazealgo.loc.Direction;
import com.gmail.bluballsman.mazealgo.structure.StructureSlot;

public class SymmetricMaze extends Maze {

	public SymmetricMaze(int width, int height) {
		super(width, height);
	}
	
	public SymmetricMaze(int width, int height, long randomSeed) {
		super(width, height, randomSeed);
	}
	
	public void setGroundSymmetric(int x, int y, boolean isGround) {
		Point mirror = getMirrorPoint(x, y);
		setGround(x, y, isGround);
		setGround(mirror, isGround);
	}
	
	public void setGroundSymmetric(Point p, boolean isGround) {
		setGroundSymmetric(p.x, p.y, isGround);
	}
	
	public void setStructureFlagSymmetric(int x, int y, boolean structureFlag) {
		Point mirror = getMirrorPoint(x, y);
		setStructureFlag(x, y, structureFlag);
		setStructureFlag(mirror, structureFlag);
	}
	
	public void setStructureFlagSymmetric(Point p, boolean structureFlag) {
		setStructureFlagSymmetric(p.x, p.y, structureFlag);
	}
	
	@Override
	public ArrayList<StructureSlot> findMatches(String strucPattern) {
		ArrayList<StructureSlot> matches = new ArrayList<StructureSlot>();
		Point centerPoint = getCenterPoint();
		StructureSlot s = new StructureSlot(this, strucPattern);
		
		for(int rotations = 0; rotations < 4; rotations++) {
			s.rotate(1);
			for(int y = 0; y < centerPoint.y; y++) {
				for(int x = 0; x < width - s.getWidth(); x++) {
					s.setLocation(x, y);
					
					if(s.canPlace()) {
						matches.add(s.clone());
					}
				}
			}
			
			for(int x = 0; x <= centerPoint.x; x++) {
				s.setLocation(x, centerPoint.y);
				
				if(s.canPlace()) {
					matches.add(s.clone());
				}
			}
		}
		
		return matches;
	}
	
	
	@Override
	public StructureSlot placeStructure(String strucPattern) {
		StructureSlot s = super.placeStructure(strucPattern);
		
		if(s != null) {
			StructureSlot mirror = s.getMirrorSlot();
			
			mirror.markStructureTiles();
			structures.add(mirror);
		}
		return s;
	}
	
	@Override
	public StructureSlot excavateRoom(String roomPattern) {
		StructureSlot s = super.excavateRoom(roomPattern);
		
		if(s != null) {
			StructureSlot mirror = s.getMirrorSlot();
			
			mirror.drawStructureTiles();
			mirror.markStructureTiles();
			structures.add(mirror);
		}
		return s;
	}
	
	@Override
	public void fillMaze() {
		Point currentPoint = freeRandomOddPoint(1, 1, width - 1, height - 1);
		Stack<Point> path = new Stack<Point>();
		path.push(currentPoint);
		setGroundSymmetric(currentPoint, true);
		
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
				setGroundSymmetric(currentPoint, true);
				currentPoint.translate(chosenDirection.X_OFFSET, chosenDirection.Y_OFFSET);
				setGroundSymmetric(currentPoint, true);
				path.push(new Point(currentPoint));
			}
			else {
				currentPoint = path.pop();
			}
		}
	}
	
	@Override
	public void knockDownWalls(float openWallPercentage) {
		Point centerPoint = getCenterPoint();
		ArrayList<Point> availableWalls = new ArrayList<Point>();
		
		// Adding all the walls to knock down above the center line. This is because we are only counting half of the points
		// So we're going to only half of the center line on the next loop
		for(int y = 1; y < centerPoint.y; y++) {
			for(int x = 1 + y % 2; x < width - 1; x+=2) {
				if(!isGround(x, y) && !isStructure(x, y)) {
					availableWalls.add(new Point(x, y));
				}
			}
		}
		
		// Center line
		for(int x = 1 + centerPoint.y % 2; x <= centerPoint.x; x+=2) {
			if(!isGround(x, centerPoint.y) && !isStructure(x, centerPoint.y)) {
				availableWalls.add(new Point(x, centerPoint.y));
			}
		}
		
		int wallsToDestroy = Math.round(availableWalls.size() * openWallPercentage);
		for(int i = 0; i < wallsToDestroy; i++) {
			Point randomWall = availableWalls.get(random.nextInt(availableWalls.size()));
			Point mirrorWall = getMirrorPoint(randomWall);
			
			availableWalls.remove(randomWall);
			setGround(randomWall, true);
			setGround(mirrorWall, true);
		}
	}
	
	public void testRooms() {
		excavateRoom("0001000.0111110.0111110.0111110.0000000");
		excavateRoom("00000.01110.01110.01110.01000");
		excavateRoom("0000010.0111110.0111110.0111110.0100000");
	}
	
	public void testStructures() {
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("000.010");
		placeStructure("111.101.111");
		placeStructure("111.101.111");
		placeStructure("111.101.111");
		placeStructure("00.10.10.10.10.10");
		placeStructure("11111.10101.11111");
		placeStructure("X0X.101.101.101.101.101.X0X");
	}

	
}
