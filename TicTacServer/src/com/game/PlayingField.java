package com.game;

import java.io.Serializable;

public class PlayingField implements Serializable {
	private static final long serialVersionUID = 1112221L;

	private State[][] rows;
	private int size;

	public PlayingField(int size) {
		this.size = size;
		rows = new State[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				rows[i][j] = State.EMPTY;
	}

	public State[][] getRows() {
		// TODO Auto-generated method stub
		return rows;
	}

	public int getSize() {
		return size;

	}

	public boolean isFull() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (rows[i][j] == State.EMPTY)
					return false;
		return true;
	}

	public void setRow(int i, int j, State state) {
		// TODO Auto-generated method stub
		rows[i][j] = state;

	}

	public State getRow(int i, int j) {
		// TODO Auto-generated method stub
		return rows[i][j];
	}

	public boolean isFree(int i, int j) {
		// TODO Auto-generated method stub
		return rows[i][j] == State.EMPTY;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

}