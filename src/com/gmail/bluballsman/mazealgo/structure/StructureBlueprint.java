package com.gmail.bluballsman.mazealgo.structure;

public class StructureBlueprint {
	private boolean[][] pattern;
	private int width = 0;
	private int height = 0;
	private int rotations = 0;
	
	public StructureBlueprint(String strucString) {
		String[] blueprintString = strucString.split("[.]");
		width = blueprintString[0].length();
		height = blueprintString.length;
		pattern = new boolean[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pattern[x][y] = blueprintString[y].charAt(x) == '1';
			}
		}
	}
	
	public StructureBlueprint(StructureBlueprint s) {
		width = s.getWidth();
		height = s.getHeight();
		rotations = s.getRotations();
		pattern = new boolean[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pattern[x][y] = s.getPattern()[x][y];
			}
		}
	}
	
	public String getBlueprintString() {
		String s = "";
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				s += pattern[x][y] ? "1" : "0";
			}
			s += ".";
		}
		
		return s;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getRotations() {
		return rotations;
	}
	
	public boolean[][] getPattern() {
		return pattern;
	}
	
	public void rotate(int addedRotations) {
		addedRotations = addedRotations < 0 ? 4 + (addedRotations % 4) : addedRotations % 4; 
		boolean[][] newPattern;
		
		switch(addedRotations) {
		case 1:
			newPattern = new boolean[height][width];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newPattern[y][x] = pattern[x][height - y - 1];
				}
			}
			break;
		case 2:
			newPattern = new boolean[width][height];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newPattern[x][y] = pattern[width - x - 1][height - y - 1];
				}
			}
			break;
		case 3:
			newPattern = new boolean[height][width];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newPattern[y][x] = pattern[width - x - 1][y];
				}
			}
			break;
		default:
			return;
		}
		
		rotations = (rotations + addedRotations) % 4;
		pattern = newPattern;
		width = pattern.length;
		height = pattern[0].length;
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
