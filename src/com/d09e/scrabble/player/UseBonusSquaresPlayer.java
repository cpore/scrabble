package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;

public class UseBonusSquaresPlayer extends MaxScorePlayer{

	public UseBonusSquaresPlayer(String name) {
		super(name);
	}
	
	public UseBonusSquaresPlayer(JSONObject jo) {
		super(jo);
	}
	
	@Override
	protected int type() {
		return PlayerFactory.USE_BONUS_SQUARES;
	}
	
	@Override
	public float getUtility(GameState gameState, Move move) {
		// TODO Auto-generated method stub
		return move.getUtility();
	}

}
