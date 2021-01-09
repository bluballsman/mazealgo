package com.gmail.bluballsman.mazealgo.structure;

public class StructureBlueprint {
	private boolean[][] blueprint;
	private int width = 0;
	private int height = 0;
	private int rotations = 0;
	
	public StructureBlueprint(String strucString) {
		String[] blueprintString = strucString.split("[.]");
		width = blueprintString[0].length();
		height = blueprintString.length;
		blueprint = new boolean[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				blueprint[x][y] = blueprintString[y].charAt(x) == '1';
			}
		}
	}
	
	public StructureBlueprint(StructureBlueprint s) {
		width = s.getWidth();
		height = s.getHeight();
		rotations = s.getRotations();
		blueprint = new boolean[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				blueprint[x][y] = s.getBlueprint()[x][y];
			}
		}
	}
	
	public String getBlueprintString() {
		String s = "";
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				s += blueprint[x][y] ? "1" : "0";
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
	
	public boolean[][] getBlueprint() {
		return blueprint;
	}
	
	public void rotate(int addedRotations) {
		addedRotations = addedRotations < 0 ? 4 + (addedRotations % 4) : addedRotations % 4; 
		boolean[][] newBlueprint;
		
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
		width = blueprint.length;
		height = blueprint[0].length;
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
