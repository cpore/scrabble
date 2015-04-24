package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.d09e.scrabble.exception.InvalidPlacementException;

public class SearchOld {
	private static final boolean DEBUG = false;

	private SearchOld(){ }

	@SuppressWarnings("unused")
	private static ArrayList<Move> getPossibleMoves(Set<Slot> possibleSlots, GameState gameState) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		String rackString = gameState.getCurrentPlayer().getRack().getRackString();


		for(Slot slot: possibleSlots){
			if(slot.isOpenNorth()){
				Board board = gameState.getBoard().copy();
				int rowPos = 0;
				Set<String> combos = generateAll(rackString, rackString.length());
				while(!board.hasNorthNeighbor(slot.getRow()-rowPos, slot.getCol()) && rowPos < rackString.length()){
					rowPos++;

					for(String word: combos){
						if(word.length() < rowPos) continue;
						Move move = getMove(gameState, D.VERTICAL, slot.getRow()-rowPos, slot.getCol(), word);

						if(null != move) possibleMoves.add(move);
					}
				}
			}

			if(slot.isOpenSouth()){
				Board board = gameState.getBoard().copy();
				int rowPos = 0;
				int occupied = 0;
				while(Board.inBoardBounds(slot.getRow()+rowPos+occupied, slot.getCol()) && rowPos < rackString.length()){
					while(board.hasSouthNeighbor(slot.getRow()+rowPos+occupied, slot.getCol())){
						occupied++;
					}
					rowPos++;
					Set<String> combos = generate(rackString, rowPos);
					for(String word: combos){
						Move move = getMove(gameState, D.VERTICAL, slot.getRow()+1, slot.getCol(), word);

						if(null != move) possibleMoves.add(move);
					}
				}
			}

			if(slot.isOpenWest()){
				Board board = gameState.getBoard().copy();
				int colPos = 0;
				Set<String> combos = generateAll(rackString, rackString.length());
				while(!board.hasWestNeighbor(slot.getRow(), slot.getCol()-colPos) && colPos < rackString.length()){
					colPos++;
					for(String word: combos){
						if(word.length() < colPos) continue;
						Move move = getMove(gameState, D.HORIZONTAL, slot.getRow(), slot.getCol()-colPos, word);

						if(null != move) possibleMoves.add(move);
					}
				}
			}

			if(slot.isOpenEast()){
				Board board = gameState.getBoard().copy();
				int colPos = 0;
				int occupied = 0;
				while(Board.inBoardBounds(slot.getRow(), slot.getCol()+colPos+occupied) && colPos < rackString.length()){
					while(board.hasEastNeighbor(slot.getRow(), slot.getCol()+colPos+occupied)){
						occupied++;
					}
					colPos++;
					Set<String> combos = generate(rackString, colPos);
					for(String word: combos){
						Move move = getMove(gameState, D.HORIZONTAL, slot.getRow(), slot.getCol()+1, word);

						if(null != move) possibleMoves.add(move);
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
		Rack rack = gameState.getCurrentPlayer().getRack();
		Move possibleMove = new Move(dir, row, col, rack.getWordTiles(word));
		Board board = gameState.getBoard();
		if(D.isValidMove(board, possibleMove)){
			try {
				board.copy().placeWord(possibleMove, false);
			} catch (InvalidPlacementException e) {
				e.printStackTrace();
			}
			if(DEBUG) System.out.println("WORD OK, Tiles: " + possibleMove.getTileString() /*+ " Word: " + possibleMove.getWord()*/);
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