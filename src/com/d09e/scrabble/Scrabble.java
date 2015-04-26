package com.d09e.scrabble;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import com.d09e.scrabble.stats.Automator;
import com.d09e.scrabble.stats.StatsCollector;
import com.icantrap.collections.dawg.Dawg;
import com.icantrap.collections.dawg.Dawg.Result;
import com.icantrap.collections.dawg.DawgBuilder;

public class Scrabble {
	private static final boolean DEBUG = false;

	public static Dawg dawg;

	private static GameState gs;
	
	public static ArrayList<StatsCollector> stats;
	private static final boolean automate = true;
	private static String p1Name = "ROBOT 1";
	private static String p2Name = "ROBOT 2";
	

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
		if(word.equals("?")){
			System.out.println("FOUND SINGLE ?");
			return true;
		}

		if(word.contains("?")){
			String pattern = word.replace("?", ".");
			for(Result r: Scrabble.dawg.subwords(word, word)){
				//System.out.println(word + " Result: " + r.word);
				if(r.word.matches(pattern) && r.word.length() == word.length()){
					if(!DEBUG) System.out.println("PATTERN: " + pattern + ":" + r.word + ":" + r.wordWithWildcards);
					return true;
				}else{
					if(DEBUG) System.out.println("BAD PATTERN: " + pattern + ":" + r.word + ":" + r.wordWithWildcards);
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
		if(automate)
			Automator.go();
		else
			play(p1Name, p2Name);

	}

	public static void play(String p1Name, String p2Name) {
		try {
			initDawg();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*	Result[] results = Scrabble.dawg.subwords("D?N?", "D?N?");
		for(Result r: results){
			System.out.println("PATTERN: " + r.word);
			//return;
		}
		System.exit(0);*/

		System.out.println("WELCOME TO SCRABBLE(TM)!");

		//loadGame();
		stats = new ArrayList<StatsCollector>();
		if(gs == null || automate) gs = new GameState(p1Name, p2Name);


		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				//String error = "savestates/error";

				//gs.saveGameState(error);
				GameState.gameOver = true;
				System.out.flush();
				gs.printRacks();
				gs.printScores();
				gs.printBoard();

			}
		}));

		try{
			gs.start();

			gs.printScores();
			gs.printBoard();
			System.out.println("GAME OVER! THANKS FOR PLAYING SCRABBLE(TM)!");

		}catch(Exception e){
			gs.printRacks();
			gs.printScores();
			gs.printBoard();
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private static void loadGame() {
		try {
			gs = GameState.loadGameState("savestates/doublewildcard.state");
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
