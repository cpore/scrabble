package com.d09e.scrabble.player;

import org.json.JSONObject;

public class PlayerFactory {
	public static final int HUMAN = 1;
	public static final int ROBOT = 2;
	
	public static Player makePlayer(JSONObject jo){
		int type = jo.getInt(Player.TYPE);
		
		
		switch(type){
		case HUMAN:
			return new HumanPlayer(jo);
		case ROBOT:
			return new HumanPlayer(jo);
		}
		
		return new HumanPlayer(jo);
	}

}
