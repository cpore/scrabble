package com.d09e.scrabble.board;

public class Square {

	private char letter = '0';
	
	private final Premium premium;
	
	public Square(Premium premium){
		this.premium = premium; 
	}
	
	public void setLetter(char ltr){
		letter = ltr;
	}
}
