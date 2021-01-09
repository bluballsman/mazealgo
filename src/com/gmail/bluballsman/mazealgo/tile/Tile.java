package com.gmail.bluballsman.mazealgo.tile;

import java.util.HashMap;

public class Tile {
	private boolean isGround = false;
	private boolean structureFlag = false;
	private Type type = null;
	private int rotations = 0;
	
	public Tile(boolean isGround, boolean structureFlag, boolean[] surroundingTiles) {
		int surroundingTileBits = 0;
		int rotations = 0;
		
		for(int i = 0; i < 4; i++) {
			surroundingTileBits = surroundingTileBits << 1;
			surroundingTileBits += isGround == surroundingTiles[i] ? 1 : 0;
		}
		
		surroundingTileBits = surroundingTileBits | (surroundingTileBits << 4);
		while(Type.getType(surroundingTileBits) == null) {
			surroundingTileBits = surroundingTileBits >> 1;
			rotations++;
		}
		
		this.isGround = isGround;
		this.structureFlag = structureFlag;
		this.type = Type.getType(surroundingTileBits);
		this.rotations = rotations;
	}
	
	public boolean isGround() {
		return isGround;
	}
	
	public boolean isStructure() {
		return structureFlag;
	}
	
	public Type getType() {
		return type;
	}
	
	public int getRotations() {
		return rotations;
	}
	
	public static enum Type {
		T(0b0111),
		CORNER(0b0011),
		STRAIGHT(0b0101),
		END(0b0001),
		CROSS(0b1111),
		ALONE(0b0000);
		
		static HashMap<Integer, Type> codeMap = new HashMap<Integer, Type>();
		
		static {
			for(Type type: values()) {
				codeMap.put(type.typeCode, type);
			}
		}
		
		public static Type getType(int typeCode) {
			typeCode = typeCode & 0b00001111;
			return codeMap.get(typeCode);
		}
		
		private final int typeCode;
		
		Type(int typeCode) {
			this.typeCode = typeCode;
		}
	}
	
	
}
