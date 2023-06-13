package com.game;

import java.util.HashMap;
import java.util.Map;

public enum State {
   X(1),O(-1),EMPTY(0);
   public int value;
   State(int value){
	   this.value =value;
   }
   public int getValue() {
	   return this.value;
   }
   
   private static Map map = new HashMap<>();
   static {
       for (State cellState : State.values()) {
           map.put(cellState.value, cellState);
       }
   }

   public static State valueOf(int cellState) {
       return (State) map.get(cellState);
   }

}
