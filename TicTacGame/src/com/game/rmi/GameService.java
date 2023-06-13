package com.game.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.game.Cell;
import com.game.PlayingField;


public interface GameService  extends Remote {
	static final String SERVICE_NAME = "GameService";
   public PlayingField newGame(int n) throws RemoteException;;
   public Cell nextMove(PlayingField play) throws RemoteException;;
}
