package com.d09e.scrabble;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.icantrap.collections.dawg.Dawg;
import com.icantrap.collections.dawg.DawgBuilder;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		try {
			initDawg();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GameState game = new GameState();
		
		System.out.println("WELCOME TO SCRABBLE(TM)!");

	}

}
