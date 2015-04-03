package com.d09e.scrabble;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.icantrap.collections.dawg.Dawg;
import com.icantrap.collections.dawg.DawgBuilder;

public class Main {

	public static void main(String[] args) {
		
		try {
			initDawg("./dict/sowpods.txt", "./dawg/sowpods.dawg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Game game = new Game();
		
		game.printBoard();
		

	}
	
	private static void initDawg(String dictFileName, String dawgFileName)throws IOException{
		if(dictFileName == null || dawgFileName == null){
			return;
		}
		if(new File(dawgFileName).exists()){
			return;
		}
		    
	    FileReader reader = new FileReader (dictFileName);
	    DawgBuilder builder = new DawgBuilder();
	    Dawg dawg = builder.add (reader).build ();
	    dawg.store(new FileOutputStream(dawgFileName));
	}
}
