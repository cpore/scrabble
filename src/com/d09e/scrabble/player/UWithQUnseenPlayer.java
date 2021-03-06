package com.d09e.scrabble.player;

import org.json.JSONObject;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;
import com.d09e.scrabble.Rack;

public class UWithQUnseenPlayer extends UseQPlayer{

	public UWithQUnseenPlayer(String name) {
		super(name);
	}

	public UWithQUnseenPlayer(JSONObject jo) {
		super(jo);
	}

	// copy ctor
	public UWithQUnseenPlayer(String name, int score, Rack rack){
		super(name, score, rack);
	}

	public UWithQUnseenPlayer() {
		super("UWithQUnseenPlayer");
	}

	@Override
	public Player copy(){
		return new UWithQUnseenPlayer(name, score, rack.copy());
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
