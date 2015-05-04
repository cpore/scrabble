package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;

public class SaveCommonPlayer extends MaxScorePlayer{

	public SaveCommonPlayer(String name) {
		super(name);
	}
	
	public SaveCommonPlayer(JSONObject jo) {
		super(jo);
	}
	
	@Override
	protected int type() {
		return PlayerFactory.SAVE_COMMON;
	}
	
	@Override
	public float getUtility(GameState gameState, Move move) {
		return saveCommon(gameState, move);
	}

}
