package com.gmail.bluballsman.mazealgo.loc;

public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		this(p.x, p.y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Point translate(int dx, int dy) {
		x = x + dx;
		y = y + dy;
		return this;
	}
	
	public Point translate(Direction d) {
		return translate(d.X_OFFSET, d.Y_OFFSET);
	}
	
	
}
