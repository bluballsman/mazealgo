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
	
	@Override
	public String toString() {
		String s = "";
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				s += blueprint[x][y] ? "1" : "0";
			}
			s += "\n";
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
	
	public StructureBlueprint rotate(int addedRotations) {
		addedRotations = addedRotations < 0 ? 4 + (addedRotations % 4) : addedRotations % 4; 
		boolean[][] newBlueprint;
		
		if(addedRotations == 0) {
			newBlueprint = blueprint;
		}
		else if(addedRotations == 1) {
			newBlueprint = new boolean[height][width];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newBlueprint[y][x] = blueprint[x][height - y - 1];
				}
			}
		}
		else if(addedRotations == 2) {
			newBlueprint = new boolean[width][height];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newBlueprint[x][y] = blueprint[width - x - 1][height - y - 1];
				}
			}
		}
		else {
			newBlueprint = new boolean[height][width];
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newBlueprint[y][x] = blueprint[width - x - 1][y];
				}
			}
		}
		
		rotations = (rotations + addedRotations) % 4;
		blueprint = newBlueprint;
		width = blueprint.length;
		height = blueprint[0].length;
		
		return this;
	}
	
}
