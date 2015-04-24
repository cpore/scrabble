package com.d09e.scrabble;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;

import com.d09e.scrabble.exception.InvalidPlacementException;
import com.icantrap.collections.dawg.Dawg.Result;

public class Search {
	private static final boolean DEBUG = false;

	private Search(){ }

	public static Move findBestMove(GameState gameState){
		TreeSet<Move> possibleMoves = new TreeSet<Move>();

		if(gameState.getBoard().firstMove){
			possibleMoves = getFirstMoves(gameState);
		}else{
			Set<Slot> possibleSlots = gameState.getBoard().getPossibleSlots();
			possibleMoves = getPossibleMoves(possibleSlots, gameState);
		}

		//Collections.sort(possibleMoves);

		if(possibleMoves.size() <= 0){
			if(DEBUG) System.out.println("NO MOVES FOUND!!!!");
			return null;
		}
		return possibleMoves.first();//get(possibleMoves.size()-1);
	}

	private static TreeSet<Move> getPossibleMoves(Set<Slot> possibleSlots, GameState gameState) {
		TreeSet<Move> possibleMoves = new TreeSet<Move>();
		String rackString = gameState.getCurrentPlayer().getRack().getRackString();

		Set<String> combos = generateAll(rackString, rackString.length());

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
						if(word.contains("?")) System.out.println(word);
						if(null != move) possibleMoves.add(move);
					}
				}

				if(slot.isOpenSouth()){
					Move move = getMove(gameState, D.VERTICAL, slot.getRow()+1, slot.getCol(), word);
					if(word.contains("?")) System.out.println(word);
					if(null != move) possibleMoves.add(move);

				}


				if(slot.isOpenEast()){
					Move move = getMove(gameState, D.HORIZONTAL, slot.getRow(), slot.getCol()+1, word);
					if(word.contains("?")) System.out.println(word);
					if(null != move) possibleMoves.add(move);

				}

				if(slot.isOpenWest()){
					Board board = gameState.getBoard();
					int colPos = 0;
					while(!board.hasWestNeighbor(slot.getRow(), slot.getCol()-colPos) && colPos < word.length()){
						colPos++;
						Move move = getMove(gameState, D.HORIZONTAL, slot.getRow(), slot.getCol()-colPos, word);
						if(word.contains("?")) System.out.println(word);
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

	private static TreeSet<Move> getFirstMoves(GameState gameState){
		TreeSet<Move> firstMoves = new TreeSet<Move>();

		Rack rack = gameState.getCurrentPlayer().getRack();
		Result[] words = Scrabble.getSubwords(rack.getRackString());

		Board board = gameState.getBoard();


		for(Result r: words){
			Tile[] wordTiles = rack.getWordTiles(r.wordWithWildcards);

			for(int i=1; i<7; i++){
				if(r.word.length() < 7-(i-1)) continue;
				if(DEBUG) System.out.println("TRying word: " + r.word);

				Move possibleHMove = new Move(D.HORIZONTAL, 7, i, wordTiles);

				if(D.isValidMove(board, possibleHMove)){
					if(DEBUG){
						for(Tile t: wordTiles)
							System.out.println("WORD OK1: " + t.toString());
					}
					Board boardCopy = board.copy();
					try {
						boardCopy.placeWord(possibleHMove, false);
					} catch (InvalidPlacementException e) {
						e.printStackTrace();
					}
					firstMoves.add(possibleHMove);
				}

				Move possibleVMove = new Move(D.VERTICAL, i, 7, wordTiles);

				if(D.isValidMove(board, possibleVMove)){
					if(DEBUG){
						for(Tile t: wordTiles)
							System.out.println("WORD OK2: " + t.toString());
					}
					Board boardCopy = board.copy();
					try {
						boardCopy.placeWord(possibleVMove, false);
					} catch (InvalidPlacementException e) {
						e.printStackTrace();
					}
					firstMoves.add(possibleVMove);
				}
			}

			Move possibleMove = new Move(D.HORIZONTAL, 7, 7, wordTiles);

			if(D.isValidMove(board, possibleMove)){
				if(DEBUG){
					for(Tile t: wordTiles)
						System.out.println("WORD OK3: " + t.toString());
				}
				Board boardCopy = board.copy();
				try {
					boardCopy.placeWord(possibleMove, false);
				} catch (InvalidPlacementException e) {
					e.printStackTrace();
				}
				firstMoves.add(possibleMove);
			}

			Move possibleVMove = new Move(D.VERTICAL, 7, 7, wordTiles);

			if(D.isValidMove(board, possibleVMove)){
				if(DEBUG){
					for(Tile t: wordTiles)
						System.out.println("WORD OK4: " + t.toString());
				}
				Board boardCopy = board.copy();
				try {
					boardCopy.placeWord(possibleVMove, false);
				} catch (InvalidPlacementException e) {
					e.printStackTrace();
				}
				firstMoves.add(possibleVMove);
			}
		}

		if(DEBUG){
			for(Move m: firstMoves){
				System.out.println(m.getScore());
			}
		}

		return firstMoves;
	}
}
