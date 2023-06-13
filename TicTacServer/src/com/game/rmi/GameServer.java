package com.game.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
	  public static void main(String[] args) {
		  String localhost = "127.0.0.1";
	      String RMI_HOSTNAME = "java.rmi.server.hostname";
	      try {
	          // создаем переменную java.rmi.server.hostname
	          System.setProperty(RMI_HOSTNAME, localhost);
	           //создаем реестр для локальной сети
	          Registry reg = LocateRegistry.createRegistry(1099);
	          //регистрируем объект в реестре
	          reg.rebind(GameService.SERVICE_NAME, new GameServiceImpl());
	          System.out.println("Server is connected and ready for operation.");
	      } catch (RemoteException ex) {
	          Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
	      }

	  }
}
