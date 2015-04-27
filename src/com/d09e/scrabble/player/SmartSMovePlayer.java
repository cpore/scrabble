package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;

public class SmartSMovePlayer extends MaxScorePlayer{

	public SmartSMovePlayer(String name) {
		super(name);
	}
	
	public SmartSMovePlayer(JSONObject jo) {
		super(jo);
	}
	
	@Override
	protected int type() {
		return PlayerFactory.SMART_S_MOVE;
	}
	
	@Override
	public float getUtility(GameState gameState, Move move) {
		// TODO Auto-generated method stub
		return 0;
	}

}
