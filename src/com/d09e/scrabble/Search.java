package com.d09e.scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.d09e.scrabble.exception.InvalidPlacementException;
import com.icantrap.collections.dawg.Dawg.Result;

public class Search {

	private Search(){ }

	public static Move findBestMove(GameState gameState){
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		if(gameState.getBoard().firstMove){
			possibleMoves.addAll(getFirstMoves(gameState));
		}else{
			HashSet<Slot> possibleSlots = gameState.getBoard().getPossibleSlots(gameState.getCurrentPlayer().getRack().copy());
			possibleMoves.addAll(getPossibleMoves(possibleSlots, gameState));
		}

		Collections.sort(possibleMoves);

		if(possibleMoves.size() <= 0){
			System.out.println("NO MOVES FOUND!!!!");
			return null;
		}
		return possibleMoves.get(possibleMoves.size()-1);
	}

	private static ArrayList<Move> getPossibleMoves(
			HashSet<Slot> possibleSlots, GameState gameState) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		Rack rackCopy = gameState.getCurrentPlayer().getRack().copy();
		
		for(Slot slot: possibleSlots){
			String pathString = slot.getPathLetters(rackCopy.copy());
			System.out.println("Path String: " + pathString);
			Result[] results = Scrabble.getSubwords(pathString);
		}

		/*long start = System.currentTimeMillis();
		long end = start + 6000l;

		//TODO this does not find words that have the tile on the board in them
		for(Slot slot: possibleSlots){
			for(Result r: words){
				//if(System.currentTimeMillis() > end) return possibleMoves;
				System.out.println("WORD PERMUTATION: " + r.word);
				Move possibleMove = new Move(slot, rackCopy.getWordTiles(r.wordWithWildcards, r.word));
				Board boardCopy = gameState.getBoard().copy();
				if(D.isValidMove(boardCopy, possibleMove)){
					System.out.println("WORD OK: " + possibleMove.getTileString());
					try {
						boardCopy.placeWord(possibleMove);
					} catch (InvalidPlacementException e) {
						e.printStackTrace();
					}
					possibleMove.getScore(boardCopy);
					possibleMoves.add(possibleMove);
				}
			}
		}*/


		return possibleMoves;
	}

	private static ArrayList<Move> getFirstMoves(GameState gameState){
		ArrayList<Move> firstMoves = new ArrayList<Move>();
		
		Rack rackCopy = gameState.getCurrentPlayer().getRack().copy();
		Result[] words = Scrabble.getSubwords(rackCopy.getRackString());
			
		for(int i=1; i<=7; i++){
			for(Result r: words){
				if(r.word.length() != (7-i)+1) continue;
				
				Move possibleHMove = new Move(D.HORIZONTAL, 7, i, rackCopy.copy().getWordTiles(r.wordWithWildcards, r.word));
				Board boardCopy = gameState.getBoard().copy();
				if(D.isValidMove(boardCopy, possibleHMove)){
					System.out.println("WORD OK: " + possibleHMove.getTileString());
					try {
						boardCopy.placeWord(possibleHMove);
					} catch (InvalidPlacementException e) {
						e.printStackTrace();
					}
					possibleHMove.getScore(boardCopy);
					firstMoves.add(possibleHMove);
				}
				
				Move possibleVMove = new Move(D.VERTICAL, i, 7, rackCopy.copy().getWordTiles(r.wordWithWildcards, r.word));
				boardCopy = gameState.getBoard().copy();
				if(D.isValidMove(boardCopy, possibleVMove)){
					System.out.println("WORD OK: " + possibleVMove.getTileString());
					try {
						boardCopy.placeWord(possibleVMove);
					} catch (InvalidPlacementException e) {
						e.printStackTrace();
					}
					possibleVMove.getScore(boardCopy);
					firstMoves.add(possibleVMove);
				}
				
				
			}
		}
		
		for(Result r: words){
			Move possibleMove = new Move(D.HORIZONTAL, 7, 7, rackCopy.copy().getWordTiles(r.wordWithWildcards, r.word));
			Board boardCopy = gameState.getBoard().copy();
			if(D.isValidMove(boardCopy, possibleMove)){
				System.out.println("WORD OK: " + possibleMove.getTileString());
				try {
					boardCopy.placeWord(possibleMove);
				} catch (InvalidPlacementException e) {
					e.printStackTrace();
				}
				possibleMove.getScore(boardCopy);
				firstMoves.add(possibleMove);
			}
			
			Move possibleVMove = new Move(D.VERTICAL, 7, 7, rackCopy.copy().getWordTiles(r.wordWithWildcards, r.word));
			boardCopy = gameState.getBoard().copy();
			if(D.isValidMove(boardCopy, possibleVMove)){
				System.out.println("WORD OK: " + possibleVMove.getTileString());
				try {
					boardCopy.placeWord(possibleVMove);
				} catch (InvalidPlacementException e) {
					e.printStackTrace();
				}
				possibleVMove.getScore(boardCopy);
				firstMoves.add(possibleVMove);
			}
		}
	
		return firstMoves;
	}
}
