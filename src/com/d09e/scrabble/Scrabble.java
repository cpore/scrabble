package com.d09e.scrabble;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;

import com.icantrap.collections.dawg.Dawg;
import com.icantrap.collections.dawg.Dawg.Result;
import com.icantrap.collections.dawg.DawgBuilder;

public class Scrabble {

	public static Dawg dawg;

	private static GameState gs;

	private static void initDawg()throws IOException{
		String dawgFileName = "./dawg/twl06.dat";
		if(new File(dawgFileName).exists()){
			loadDawg();
			return;
		}

		System.out.println("Building Dawg. Please Wait...");
		buildDawg(dawgFileName);
		loadDawg();
	}

	private static void buildDawg(String dawgFileName)
			throws FileNotFoundException, IOException {
		String dictFileName = "./dict/twl06.txt";
		FileReader reader = new FileReader (dictFileName);
		DawgBuilder builder = new DawgBuilder();
		Dawg dawg = builder.add (reader).build();
		dawg.store(new FileOutputStream(dawgFileName));
	}

	private static void loadDawg(){
		try {
			System.out.println("Loading DAWG");
			Scrabble.dawg = Dawg.load(new FileInputStream("dawg/twl06.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isValidWord(String word){
		if(word.equals("?")) return true;

		if(word.contains("?")){
			String pattern = word.replace("?", ".");
			for(Result r: Scrabble.dawg.subwords(word, word)){
				if(r.word.matches(pattern)){
					System.out.println("PATTERN: " + r.word + ":" + r.wordWithWildcards);
					return true;
				}
			}
			return false;
		}
		return Scrabble.dawg.contains(word);
	}

	public static Result[] getSubwords(String rack){
		return Scrabble.dawg.subwords(rack, "");
	}

	public static void main(String[] args) {

		try {
			initDawg();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("WELCOME TO SCRABBLE(TM)!");

		//loadGame();

		if(gs == null) gs = new GameState();


		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				String error = "savestates/error";

				gs.saveGameState(error);
				GameState.gameOver = true;
				System.out.flush();
				System.out.println("ABORTED");
				gs.printRacks();
				gs.printScores();
				gs.printBoard();

			}
		}));

		try{

			gs.start();

		}catch(Exception e){
			gs.printScores();
			gs.printBoard();
			e.printStackTrace();
		}

	}

	private static void loadGame() {
		try {
			gs = GameState.loadGameState("savestates/badwildcard.state");
			gs.setHuman();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
