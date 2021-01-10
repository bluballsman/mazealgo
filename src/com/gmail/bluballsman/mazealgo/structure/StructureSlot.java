package com.gmail.bluballsman.mazealgo.structure;

import java.awt.Point;
import java.awt.Rectangle;

import com.gmail.bluballsman.mazealgo.Maze;

public class StructureSlot {
	private Maze maze;
	private boolean[][] blueprint;
	private int rotations = 0;
	private Rectangle boundingBox = new Rectangle();
	
	public StructureSlot(Maze maze, String strucString) {
		this.maze = maze;
		String[] blueprintString = strucString.split("[.]");
		boundingBox.width = blueprintString[0].length();
		boundingBox.height = blueprintString.length;
		blueprint = new boolean[getWidth()][getHeight()];
		
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				blueprint[x][y] = blueprintString[y].charAt(x) == '1';
			}
		}
	}
	
	public StructureSlot(StructureSlot s) {
		maze = s.maze;
		rotations = s.getRotations();
		blueprint = new boolean[s.getWidth()][s.getHeight()];
		boundingBox.setSize(s.getWidth(), s.getHeight());
		boundingBox.x = s.getX();
		boundingBox.y = s.getY();
		
		System.out.println(" " + s.getWidth() + " " + s.getHeight() + " " + getWidth() + " " + getHeight());
		System.out.println(" " + s.getLocation() + " " + getLocation());
		
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
				s += blueprint[x][y] ? "1" : "0";
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
	
	public boolean[][] getBlueprint() {
		return blueprint;
	}
	
	public boolean canPlace() {
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				if(blueprint[x][y] != maze.isGround(x + getX(), y + getY())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean intersects(StructureSlot b) {
		return b.boundingBox.intersects(boundingBox);
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
		boolean[][] newBlueprint;
		addedRotations = addedRotations < 0 ? 4 + (addedRotations % 4) : addedRotations % 4; 
		
		switch(addedRotations) {
		case 1:
			newBlueprint = new boolean[height][width];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newBlueprint[y][x] = blueprint[x][height - y - 1];
				}
			}
			break;
		case 2:
			newBlueprint = new boolean[width][height];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newBlueprint[x][y] = blueprint[width - x - 1][height - y - 1];
				}
			}
			break;
		case 3:
			newBlueprint = new boolean[height][width];
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
				maze.setStructureFlag(x + getX(), y + getY(), true, false);
			}
		}
	}
	
	public void drawStructureTiles() {
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				maze.setGround(x + getX(), y + getY(), blueprint[x][y], false);
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
	
}
