package com.game;

import java.io.Serializable;

public class Cell implements Serializable 
{

	private static final long serialVersionUID = 1112221L;
    private int x;
    private int y;
    
    State state;
  
    public Cell(int x, int y) {
		this.x=x;
		this.y=y;
		this.state = State.O;
	}
    
    public Cell(int x, int y,State state) {
		this.x=x;
		this.y=y;
		this.state = state;
	}
	

	public int getX() {
		// TODO Auto-generated method stub
		return x;
	}

	
	public int getY() {
		// TODO Auto-generated method stub
		return y;
	}
	
	public State getState() {
		return state;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("CellP{%d,%d}",x,y); 
	}
	
	

}
