package com.d09e.scrabble;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.icantrap.collections.dawg.Dawg;
import com.icantrap.collections.dawg.DawgBuilder;
import com.icantrap.collections.dawg.Dawg.Result;

public class Scrabble {

	public static Dawg dawg;


	private static void initDawg()throws IOException{
		String dawgFileName = "./dawg/sowpods.dat";
		if(new File(dawgFileName).exists()){
			loadDawg();
			return;
		}

		buildDawg(dawgFileName);
	}

	private static void buildDawg(String dawgFileName)
			throws FileNotFoundException, IOException {
		String dictFileName = "./dict/sowpods.txt";
		FileReader reader = new FileReader (dictFileName);
		DawgBuilder builder = new DawgBuilder();
		Dawg dawg = builder.add (reader).build();
		dawg.store(new FileOutputStream(dawgFileName));
	}

	private static void loadDawg(){
		try {
			System.out.println("Loading DAWG");
			Scrabble.dawg = Dawg.load(new FileInputStream("dawg/sowpods.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isValidWord(String word){

		if(word.contains("?")){
			String pattern = word.replace("?", ".");
			for(Result r: Scrabble.dawg.subwords(word, word)){
				if(r.word.matches(pattern)){
					System.out.println(r.word);
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

		Result[] results = Scrabble.dawg.subwords("DAIG", "");
		String pattern = ".{0}(D).{2}";
		for(Result r: results){
			if(r.word.matches(pattern)){
				System.out.println(r.word);
			}
		}
		
		System.out.println("WELCOME TO SCRABBLE(TM)!");
		final GameState gs = new GameState();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				GameState.gameOver = true;
				System.out.flush();
				System.out.println("ABORTED");
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

}
