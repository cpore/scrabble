package com.d09e.scrabble.player;

import com.d09e.scrabble.GameState;
import com.d09e.scrabble.Move;

public interface Evaluator {
	public float getUtility(GameState gameState, Move move);
}
