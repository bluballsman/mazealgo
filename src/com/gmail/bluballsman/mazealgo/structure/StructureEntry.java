package com.gmail.bluballsman.mazealgo.structure;

import java.awt.Point;
import java.awt.Rectangle;

public class StructureEntry {
	private StructureBlueprint blueprint;
	private Rectangle boundingBox = new Rectangle();
	private int x;
	private int y;

	public StructureEntry(int x, int y, StructureBlueprint blueprint) {
		this.x = x;
		this.y = y;
		this.blueprint = blueprint;
		boundingBox.setSize(blueprint.getWidth(), blueprint.getHeight());
	}
	
	public StructureEntry(Point p, StructureBlueprint blueprint) {
		this(p.x, p.y, blueprint);
	}
	
	public StructureBlueprint getBlueprint() {
		return blueprint;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean intersects(StructureEntry s) {
		return s.boundingBox.intersects(boundingBox);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		boundingBox.setLocation(x, y);
	}
	
	public void setLocation(Point p) {
		setLocation(p.x, p.y);
	}
	
}
