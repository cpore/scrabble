package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;

public class UseQPlayer extends MaxScorePlayer{
	
	public boolean usedQ = false;

	public UseQPlayer(String name) {
		super(name);
	}
	
	public UseQPlayer(JSONObject jo) {
		super(jo);
	}
	
	@Override
	protected int type() {
		return PlayerFactory.USE_Q;
	}
	
	@Override
	public float getUtility(GameState gameState, Move move) {
		if(move.getTileString().contains("Q")){
			usedQ = true;
			move.setUtility(move.getScore() + 200);
		}
		return move.getUtility();
	}

}
