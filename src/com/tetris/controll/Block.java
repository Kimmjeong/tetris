package com.tetris.controll;

public class Block {

	private int x;
	private int y;
	
	public Block(){
		
	}
	
	public Block(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	// 해당 포인트만큼 계산
	public void move(int xPlus, int yPlus) {
		this.x += xPlus;
		this.y += yPlus;
	}
	
	public Block getBlock(){
		return this;
	}

	@Override
	public String toString() {
		return "Block [x=" + x + ", y=" + y + "]";
	}
	
	
}
