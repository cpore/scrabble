package com.d09e.scrabble;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.icantrap.collections.dawg.Dawg;

public class D {
	
	public static Dawg dawg;
	
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	
	public D(){
		try {
			dawg = Dawg.load(new FileInputStream("dawg/sowpods.dat"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean isValidWord(String word){
		return dawg.contains(word);
	}
	
	public static boolean inBoardBounds(int i, int j){
		return i >=0 && i < Board.ROWS && j >=0 && j < Board.COLS;
	}

}
