package com.d09e.scrabble.player;

import org.json.JSONObject;

public class PlayerFactory {
	public static final int HUMAN = 1;
	public static final int MAX_SCORE = 2;
	public static final int SAVE_COMMON = 3;
	public static final int SMART_S_MOVE = 4;
	public static final int USE_BONUS_SQUARES = 5;
	public static final int USE_Q = 6;
	public static final int U_WITH_Q_UNSEEN = 7;
	public static final int TILE_TURNOVER = 8;
	public static final int VOWEL_CONSONANT = 9;
	public static final int MULTI1 = 10;
	public static final int MULTI2 = 11;
	public static final int MULTI3 = 12;
	public static final int MULTI4 = 13;
	
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
		case U_WITH_Q_UNSEEN:
			return new UWithQUnseenPlayer(jo);
		case TILE_TURNOVER:
			return new TileTurnoverPlayer(jo);
		case VOWEL_CONSONANT:
			return new VowelConsonantPlayer(jo);
		case MULTI1:
			return new MultiPlayer1(jo);
		case MULTI2:
			return new MultiPlayer2(jo);
		case MULTI3:
			return new MultiPlayer3(jo);
		case MULTI4:
			return new MultiPlayer4(jo);
		}
		
		return new HumanPlayer(jo);
	}
	
	public static Player makePlayer(int type){
		
		switch(type){
		case HUMAN:
			return new HumanPlayer();
		case MAX_SCORE:
			return new MaxScorePlayer();
		case SAVE_COMMON:
			return new SaveCommonPlayer();
		case SMART_S_MOVE:
			return new SmartSMovePlayer();
		case USE_BONUS_SQUARES:
			return new UseBonusSquaresPlayer();
		case USE_Q:
			return new UseQPlayer();
		case U_WITH_Q_UNSEEN:
			return new UWithQUnseenPlayer();
		case TILE_TURNOVER:
			return new TileTurnoverPlayer();
		case VOWEL_CONSONANT:
			return new VowelConsonantPlayer();
		case MULTI1:
			return new MultiPlayer1();
		case MULTI2:
			return new MultiPlayer2();
		case MULTI3:
			return new MultiPlayer3();
		case MULTI4:
			return new MultiPlayer4();
		}
		
		return new HumanPlayer();
	}

}
