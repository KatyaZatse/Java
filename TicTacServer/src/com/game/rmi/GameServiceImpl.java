package com.game.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import com.game.Cell;

import com.game.PlayingField;
import com.game.State;

public class GameServiceImpl extends UnicastRemoteObject implements GameService {

	protected GameServiceImpl() throws RemoteException {
		super();

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 20212021L;

	@Override
	public PlayingField newGame(int n) {
		// TODO Auto-generated method stub
		return new PlayingField(n);
	}

	@Override
	public Cell nextMove(PlayingField play) {

		if (play.isFull())
			return null;
		boolean isRun = true;
		return getFirstFree(play);

	}

	// вернет первую доступную ячейку
	public Cell getFirstFree(PlayingField play) {
		// ячейки с крестиком
		List<Cell> listX = getXRow(play);
		Collections.shuffle(listX);
		// свободные ячейки
		List<Cell> free = getFree(play);
		Collections.shuffle(free);
		Integer[] arr = {1,2,3,4,5,6,7,8,9,10,11,12};
		List<Integer> moves = Arrays.asList(arr);
		for (Cell cell : listX) {
			Collections.shuffle(moves);
			Cell newCell = null;
			// поищем ближайшую свободную случайным образом
			for (int m:moves){
				switch (m){
					case 1: newCell =  getLeft(cell, play);break;
					case 2: newCell =  getLeftTop(cell, play);break;
					case 3: newCell =  getLeftBottom(cell, play);break;
					case 4: newCell =  getRight(cell, play);break;
					case 5: newCell =  getRightTop(cell, play);break;
					case 6: newCell =  getRightBottom(cell, play);break;
					case 7: newCell =  getTop(cell, play);break;
					case 8: newCell =  getBottom(cell, play);break;
					case 9: newCell =  getLeftTop(cell, play);break;
					case 10: newCell =  getLeftBottom(cell, play);break;
					case 11: newCell =  getRightTop(cell, play);break;
					case 12: newCell =  getRightBottom(cell, play);break;
				}
				if (newCell!=null) return newCell;
			}

		}
		// если не нашли - вернем случайную  свободную
		if (free.size()>0) return  free.get(0);
		return null;
	}

	// ячейка слева
	private Cell getLeft(Cell cell, PlayingField play) {
		int x = cell.getX();
		int y = cell.getY();
		x = x - 1;
		if (x < 0)
			return null;
		if (play.isFree(x, y))
			return new Cell(x, y);
		return null;
	}
	// ячейка слева вверху
	private Cell getLeftTop(Cell cell, PlayingField play) {
		int x = cell.getX();
		int y = cell.getY();
		x = x - 1;
		y = y- 1;
		if (x < 0)
			return null;
		if (y < 0)
			return null;
		if (play.isFree(x, y))
			return new Cell(x, y);
		return null;
	}
	// слева ввнизу
	private Cell getLeftBottom(Cell cell, PlayingField play) {
		int x = cell.getX();
		int y = cell.getY();
		x = x - 1;
		y = y + 1;
		if (x < 0)
			return null;
		if (y >= play.getSize())
			return null;
		if (play.isFree(x, y))
			return new Cell(x, y);
		return null;
	}
    // справа
	private Cell getRight(Cell cell, PlayingField play) {
		int x = cell.getX();
		int y = cell.getY();
		x = x +1;
		if (x>=play.getSize())
			return null;
		if (play.isFree(x, y))
			return new Cell(x, y);
		return null;
	}
	// cправа вверху
	private Cell getRightTop(Cell cell, PlayingField play) {
		int x = cell.getX();
		int y = cell.getY();
		x = x +1;
		if (x>=play.getSize())
			return null;
		y = y - 1;
		if (y<0)
			return null;
		if (play.isFree(x, y))
			return new Cell(x, y);
		return null;
	}

	// справа внизу
	private Cell getRightBottom(Cell cell, PlayingField play) {
		int x = cell.getX();
		int y = cell.getY();
		x = x +1;
		if (x>=play.getSize())
			return null;
		y = y +1;
		if (y>=play.getSize())
			return null;
		if (play.isFree(x, y))
			return new Cell(x, y);
		return null;
	}

	// сверху
	private Cell getTop(Cell cell, PlayingField play) {
		int x = cell.getX();
		int y = cell.getY();
		y = y - 1;
		if (y < 0)
			return null;
		if (play.isFree(x, y))
			return new Cell(x, y);
		return null;
	}
   // снизу
	private Cell getBottom(Cell cell, PlayingField play) {
		int x = cell.getX();
		int y = cell.getY();
		y = y + 1;
		if (y>=play.getSize())		return null;
		if (play.isFree(x, y))
			return new Cell(x, y);
		return null;
		}
	

	/**
	 * 
	 * @param play
	 * @return список клеток игрока
	 */
	public List<Cell> getXRow(PlayingField play) {
		List<Cell> list = new ArrayList<Cell>();
		for (int i = 0; i < play.getSize(); i++)
			for (int j = 0; j < play.getSize(); j++) {
				if (play.getRow(i, j) == State.X) {
					list.add(new Cell(i, j));
				}
			}
		return list;
	}
	// список свободных клеток
	public List<Cell> getFree(PlayingField play) {
		List<Cell> list = new ArrayList<Cell>();
		for (int i = 0; i < play.getSize(); i++)
			for (int j = 0; j < play.getSize(); j++) {
				if (play.getRow(i, j) == State.EMPTY) {
					list.add(new Cell(i, j));
				}
			}
		return list;
	}

}
