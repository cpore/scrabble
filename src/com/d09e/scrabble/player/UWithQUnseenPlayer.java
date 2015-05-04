package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;

public class UWithQUnseenPlayer extends UseQPlayer{
	
	public UWithQUnseenPlayer(String name) {
		super(name);
	}
	
	public UWithQUnseenPlayer(JSONObject jo) {
		super(jo);
	}
	
	@Override
	protected int type() {
		return PlayerFactory.U_WITH_Q_UNSEEN;
	}
	
	@Override
	public float getUtility(GameState gameState, Move move) {
		return uWithQUnseen(gameState, move);
	}

}
