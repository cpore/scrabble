package com.d09e.scrabble.player;

import org.json.JSONObject;

public class PlayerFactory {
	public static final int HUMAN = 1;
	public static final int MAX_SCORE = 2;
	public static final int SAVE_COMMON = 3;
	public static final int SMART_S_MOVE = 4;
	public static final int USE_BONUS_SQUARES = 5;
	public static final int USE_Q = 6;
	
	public static Player makePlayer(JSONObject jo){
		int type = jo.getInt(Player.TYPE);
		
		
		switch(type){
		case HUMAN:
			return new HumanPlayer(jo);
		case MAX_SCORE:
			return new MaxScorePlayer(jo);
		case SAVE_COMMON:
			return new SaveCommonPlayer(jo);
		case SMART_S_MOVE:
			return new SmartSMovePlayer(jo);
		case USE_BONUS_SQUARES:
			return new UseBonusSquaresPlayer(jo);
		case USE_Q:
			return new UseQPlayer(jo);
		}
		
		return new HumanPlayer(jo);
	}

}
