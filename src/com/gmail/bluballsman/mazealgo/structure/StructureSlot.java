package com.gmail.bluballsman.mazealgo.structure;

import java.awt.Point;
import java.awt.Rectangle;

import com.gmail.bluballsman.mazealgo.maze.Maze;
import com.gmail.bluballsman.mazealgo.tile.Tile;

public class StructureSlot {
	private Maze maze;
	private BlueprintCharacter[][] blueprint;
	private int rotations = 0;
	private Rectangle boundingBox = new Rectangle();
	
	public StructureSlot(Maze maze, String strucString) {
		this.maze = maze;
		String[] blueprintString = strucString.split("[.]");
		boundingBox.width = blueprintString[0].length();
		boundingBox.height = blueprintString.length;
		blueprint = new BlueprintCharacter[getWidth()][getHeight()];
		
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				char c = blueprintString[y].charAt(x);
				blueprint[x][y] = BlueprintCharacter.getByCharacter(c);
			}
		}
	}
	
	public StructureSlot(Maze maze, String strucString, int rotations) {
		this(maze, strucString);
		rotate(rotations);
	}
	
	public StructureSlot(StructureSlot s) {
		maze = s.maze;
		rotations = s.getRotations();
		blueprint = new BlueprintCharacter[s.getWidth()][s.getHeight()];
		boundingBox.setSize(s.getWidth(), s.getHeight());
		boundingBox.x = s.getX();
		boundingBox.y = s.getY();
		
		for(int y = 0; y < s.getHeight(); y++) {
			for(int x = 0; x < s.getWidth(); x++) {
				blueprint[x][y] = s.getBlueprint()[x][y];
			}
		}
	}
	
	public String getBlueprintString() {
		String s = "\n";
		
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				s += blueprint[x][y].getCharacter();
			}
			s += ".";
		}
		
		return s;
	}
	
	public int getX() {
		return boundingBox.x;
	}
	
	public int getY() {
		return boundingBox.y;
	}
	
	public int getWidth() {
		return boundingBox.width;
	}
	
	public int getHeight() {
		return boundingBox.height;
	}
	
	public int getRotations() {
		return rotations;
	}
	
	public Point getLocation() {
		return boundingBox.getLocation();
	}
	
	public StructureSlot getMirrorSlot() {
		StructureSlot mirrorSlot = new StructureSlot(this);
		Point oppositeEnd = new Point(getX() + getWidth() - 1, getY() + getHeight() - 1);
		Point mirrorPoint = maze.getMirrorPoint(oppositeEnd);
		mirrorSlot.setLocation(mirrorPoint);
		mirrorSlot.rotate(2);
		
		return mirrorSlot;
	}
	
	public BlueprintCharacter[][] getBlueprint() {
		return blueprint;
	}
	
	public boolean canPlace() {
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				Tile t = maze.getTile(x + getX(), y + getY());
				
				if(!blueprint[x][y].matchesTile(t)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean intersects(StructureSlot b) {
		return b.boundingBox.intersects(boundingBox);
	}
	
	public boolean isPointInSlot(Point p) {
		return boundingBox.contains(p);
	}
	
	public void setLocation(int x, int y) {
		boundingBox.x = x;
		boundingBox.y = y;
	}
	
	public void setLocation(Point p) {
		setLocation(p.x, p.y);
	}
	
	public void setX(int x) {
		boundingBox.x = x;
	}
	
	public void setY(int y) {
		boundingBox.y = y;
	}
	
	public void rotate(int addedRotations) {
		int height = getHeight();
		int width = getWidth();
		BlueprintCharacter[][] newBlueprint;
		addedRotations = addedRotations < 0 ? 4 + (addedRotations % 4) : addedRotations % 4; 
		
		switch(addedRotations) {
		case 1:
			newBlueprint = new BlueprintCharacter[height][width];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newBlueprint[y][x] = blueprint[x][height - y - 1];
				}
			}
			break;
		case 2:
			newBlueprint = new BlueprintCharacter[width][height];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newBlueprint[x][y] = blueprint[width - x - 1][height - y - 1];
				}
			}
			break;
		case 3:
			newBlueprint = new BlueprintCharacter[height][width];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newBlueprint[y][x] = blueprint[width - x - 1][y];
				}
			}
			break;
		default:
			return;
		}
		
		rotations = (rotations + addedRotations) % 4;
		blueprint = newBlueprint;
		boundingBox.width = blueprint.length;
		boundingBox.height = blueprint[0].length;
	}
	
	public void markStructureTiles() {
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				maze.setStructureFlag(x + getX(), y + getY(), true);
			}
		}
	}
	
	public void drawStructureTiles() {
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				BlueprintCharacter bc = blueprint[x][y];
				Point p = new Point(x + getX(), y + getY());
				
				if(bc == BlueprintCharacter.WALL) {
					maze.setGround(p, false);
				}
				else if(bc == BlueprintCharacter.GROUND) {
					maze.setGround(p, true);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return getBlueprintString().replace('.', '\n');
	}
	
	@Override
	public int hashCode() {
		return getBlueprintString().hashCode() + rotations;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(o == null || o.getClass() != getClass()) {
			return false;
		}
		
		StructureSlot otherSlot = (StructureSlot) o;
		
		return otherSlot.maze == maze && otherSlot.blueprint.equals(blueprint);
	}
	
}
