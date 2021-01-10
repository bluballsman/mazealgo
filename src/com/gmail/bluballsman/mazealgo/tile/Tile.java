package com.gmail.bluballsman.mazealgo.tile;

import java.awt.Point;
import java.util.HashMap;

import com.gmail.bluballsman.mazealgo.Maze;

public class Tile {
	private final Maze maze;
	private final int x;
	private final int y;
	private boolean isGround = false;
	private boolean structureFlag = false;
	
	public Tile(Maze maze, int x, int y) {
		this.maze = maze;
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Point getLocation() {
		return new Point(x, y);
	}
	
	public boolean isGround() {
		return isGround;
	}
	
	public boolean isStructure() {
		return structureFlag;
	}
	
	public Type getType() {
		return Type.getType(getTypeCode());
	}
	
	public int getRotations() {
		return Type.getRotations(getTypeCode());
	}
	
	public Tile getMirrorTile() {
		return maze.getTile(maze.getMirrorPoint(x, y));
	}
	
	public void setGround(boolean isGround) {
		this.isGround = isGround;
	}
	
	public void setStructureFlag(boolean structureFlag) {
		this.structureFlag = structureFlag;
	}
	
	private int getTypeCode() {
		Tile north = maze.getTile(x, y + 1);
		Tile east = maze.getTile(x + 1, y);
		Tile south = maze.getTile(x, y - 1);
		Tile west = maze.getTile(x - 1, y);
		
		int typeCode = 0;
		typeCode += north == null || north.isGround ? 0b0001 : 0;
		typeCode += east == null || east.isGround ? 0b0010 : 0;
		typeCode += south == null || south.isGround ? 0b0100 : 0;
		typeCode += west == null || west.isGround ? 0b1000 : 0;
		
		return typeCode;
	}
	
	public static enum Type {
		T(0b0111),
		CORNER(0b0011),
		STRAIGHT(0b0101),
		END(0b0001),
		CROSS(0b1111),
		ALONE(0b0000);
		
		static HashMap<Integer, Type> codeMap = new HashMap<Integer, Type>();
		static HashMap<Integer, Integer> rotationMap = new HashMap<Integer, Integer>();
		
		static {
			for(Type type: values()) {
				int dummyCode = type.typeCode + (type.typeCode << 4);
				for(int rotations = 0; rotations < 4; rotations++) {
					int rotatedTypeCode = (dummyCode << rotations) & 0b00001111;
					codeMap.put(rotatedTypeCode, type);
					rotationMap.put(rotatedTypeCode, rotations);
				}
			}
		}
		
		private static Type getType(int typeCode) {
			return codeMap.get(typeCode);
		}
		
		private static int getRotations(int typeCode) {
			return rotationMap.get(typeCode);
		}
		
		private final int typeCode;
		
		Type(int typeCode) {
			this.typeCode = typeCode;
		}
	}
	
	
}
