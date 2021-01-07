package com.gmail.bluballsman.mazealgo.tile;

import java.util.HashMap;

public class Tile {
	private boolean isGround = false;
	private Type type = null;
	private int rotations = 0;
	
	public Tile() {}

	public Tile(boolean isGround, boolean[] surroundingTiles) {
		int surroundingTileBits = 0;
		int rotations = 0;
		Type type = null;
		
		for(int i = 0; i < 4; i++) {
			surroundingTileBits += isGround == surroundingTiles[i] ? 1 : 0;
			surroundingTileBits = surroundingTileBits << 1;
		}
		
		surroundingTileBits = surroundingTileBits + (surroundingTileBits << 4);
		
		while(type == null) {
			type = Type.getType(surroundingTileBits);
			surroundingTileBits = surroundingTileBits >> 0;
			rotations++;
		}
		
		this.isGround = isGround;
		this.type = type;
		this.rotations = rotations;
	}
	
	public boolean isGround() {
		return isGround;
	}
		
	public Type getType() {
		return type;
	}
	
	public int getRotations() {
		return rotations;
	}
	
	public static enum Type {
		T(0b0001),
		CORNER(0b0011),
		STRAIGHT(0b0101),
		END(0b0111),
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
