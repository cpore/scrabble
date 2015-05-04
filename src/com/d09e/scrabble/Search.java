package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

public class Search {
	private static final boolean DEBUG = false;

	private Search(){ }

	public static Move findBestMove(GameState gameState){
		ArrayList<Move> possibleMoves = new ArrayList<Move>();

		/*if(gameState.getBoard().firstMove){
			possibleMoves = getFirstMoves(gameState);
		}else{
			
			possibleMoves = getPossibleMoves(possibleSlots, gameState);
		}*/
		
		Set<Slot> possibleSlots = gameState.getBoard().getPossibleSlots(gameState.getBoard().firstMove);
		possibleMoves = getPossibleMoves(possibleSlots, gameState);

		if(possibleMoves.size() <= 0){
			if(DEBUG) System.out.println("NO MOVES FOUND!!!!");
			return null;
		}
		Collections.sort(possibleMoves);
		return possibleMoves.get(0);
	}

	private static ArrayList<Move> getPossibleMoves(Set<Slot> possibleSlots, GameState gameState) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		String rackString = gameState.getCurrentPlayer().getRack().getRackString();

		//limit the search to only using one wildcard because things get crazy if there are two
		//int wildcardCount = rackString.length() - rackString.replace("?", "").length();
		//if(wildcardCount > 1) rackString.replaceFirst("\\?", "");
		
		Set<String> combos = generateAll(rackString, rackString.length());
		
		if(gameState.getBoard().firstMove) combos.remove("?");

		long start = System.currentTimeMillis();
		long end = start + 15000l;
		for(String word: combos){
			if(System.currentTimeMillis() > end && possibleMoves.size() > 0 && word.contains("?")) return possibleMoves;
			
			for(Slot slot: possibleSlots){

				// TODO is there a way to interleave these two?
				if(slot.isOpenNorth()){
					Board board = gameState.getBoard();
					int rowPos = 0;
					while(!board.hasNorthNeighbor(slot.getRow()-rowPos, slot.getCol()) && rowPos < word.length()){
						rowPos++;
						Move move = getMove(gameState, D.VERTICAL, slot.getRow()-rowPos, slot.getCol(), word);
						//if(word.contains("?")) System.out.println(word);
						if(null != move){
							float delta = gameState.getCurrentPlayer().getUtility(gameState, move);
							move.setUtility(delta);
							possibleMoves.add(move);
						}
					}
				}

				if(slot.isOpenSouth()){
					Move move = getMove(gameState, D.VERTICAL, slot.getRow()+1, slot.getCol(), word);
					//if(word.contains("?")) System.out.println(word);
					if(null != move){
						float delta = gameState.getCurrentPlayer().getUtility(gameState, move);
						move.setUtility(delta);
						possibleMoves.add(move);
					}

				}


				if(slot.isOpenEast()){
					Move move = getMove(gameState, D.HORIZONTAL, slot.getRow(), slot.getCol()+1, word);
					//if(word.contains("?")) System.out.println(word);
					if(null != move){
						float delta = gameState.getCurrentPlayer().getUtility(gameState, move);
						move.setUtility(delta);
						possibleMoves.add(move);
					}

				}

				if(slot.isOpenWest()){
					Board board = gameState.getBoard();
					int colPos = 0;
					while(!board.hasWestNeighbor(slot.getRow(), slot.getCol()-colPos) && colPos < word.length()){
						colPos++;
						Move move = getMove(gameState, D.HORIZONTAL, slot.getRow(), slot.getCol()-colPos, word);
						//if(word.contains("?")) System.out.println(word);
						if(null != move){
							float delta = gameState.getCurrentPlayer().getUtility(gameState, move);
							move.setUtility(delta);
							possibleMoves.add(move);
						}
					}
				}
			}
		}

		/*long start = System.currentTimeMillis();
		long end = start + 6000l;

		 */
		return possibleMoves;
	}

	private static Move getMove(GameState gameState, int dir, int row, int col, String word) {
		Rack rack = gameState.getCurrentPlayer().getRack().copy();
		Move possibleMove = new Move(dir, row, col, rack.getWordTiles(word));
		Board board = gameState.getBoard();
		
		if(D.isValidMove(board, possibleMove, gameState.getCurrentPlayer())){
			return possibleMove;
		}

		return null;
	}

	public static Set<String> generateAll(String input, int maxLen){
		LinkedHashSet<String> dup = new LinkedHashSet<String>();
		for(int i=1; i<=maxLen; i++){
			generate("".toCharArray(), input.toCharArray(), dup, i);
		}
		return dup;
	}

	public static Set<String> generate(String input, int n) {
		LinkedHashSet<String> dup = new LinkedHashSet<String>();
		generate("".toCharArray(), input.toCharArray(), dup, n);
		return dup;
	}

	private static void generate(char[] str, char[] input, Set<String> dup, int n) {
		if (str.length == n && dup.add(new String(str))){
			return;
			//System.out.println(str);
		}else{
			//remove a char form input and add it to str
			for (int i = 0; i < input.length; i++){
				char[] newStr = ArrayUtils.addAll(str, input[i]);
				char[] newInput = ArrayUtils.addAll(Arrays.copyOfRange(input, 0, i), Arrays.copyOfRange(input, i+1, input.length));
				generate(newStr, newInput, 	dup, n);
			}
		}
	}
}
