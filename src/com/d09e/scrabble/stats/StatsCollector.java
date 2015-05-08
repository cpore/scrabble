package com.d09e.scrabble.stats;

public class StatsCollector {

	private String agent;

	private float minMoveTime, maxMoveTime, avgMoveTime;
	private int maxWordScore, avgMoveScore;
	private int avgWordLength;
	
	
	private long startTime;
	private float gameTime;
	private int numberOfMoves, numWildcardMoves;
	private int score;
	private int win;
	
	public static final String header = "agent,min-move-time,max-move-time,"
			+ "avg-move-time,max-move-score,avg-move-score,avg-word-length,"
			+ "num-moves,game-time,score,win";
	
	public StatsCollector(String agent){
		this.agent = agent;
		avgMoveTime = 0l;
		avgWordLength = 0;
		avgMoveScore = 0;
		gameTime = 0l;
		score = 0;
		numberOfMoves = 0;
		maxMoveTime = 0l;
		minMoveTime = Float.MAX_VALUE;
		
		this.startTime = 0l;
	}

	public void addMove(long moveTime, int moveScore, int wordLength){
		numberOfMoves++;
		// don't add moves to the average move times that contain "?"
		// parameter moveTime should be 0 if the word contains "?"
		if(moveTime > 0){
			moveTime(moveTime);
		}else{
			numWildcardMoves++;
		}
		
		moveScore(moveScore);
		wordLength(wordLength);
	}
	
	public void start(long startTime){
		this.startTime = startTime;
	}
	
	public void finish(long endtime, int score, int win){
		gameTime = (float)(endtime-startTime)/1000f;
		this.score = score;
		this.win = win;
		
		avgMoveTime = avgMoveTime/(numberOfMoves-numWildcardMoves);
		
		avgMoveScore = avgMoveScore/numberOfMoves;
		
		avgWordLength = Math.round((float)avgWordLength/(float)numberOfMoves);
		
	}
	
	private void moveTime(long totalTime){
		float time = (float)totalTime/1000f;
		
		if(time < minMoveTime) minMoveTime = time;
		if(time > maxMoveTime) maxMoveTime = time;
		avgMoveTime += time;
	}
	
	private void moveScore(int score){
		if(score > maxWordScore) maxWordScore = score;
		avgMoveScore += score;
	}
	
	private  void wordLength(int len){
		avgWordLength += len;
	}
	
	public String getStats(){
		final String COMMA = ",";
		return agent + COMMA + minMoveTime + COMMA
				+ maxMoveTime + COMMA + String.format("%.1f", avgMoveTime) + COMMA + maxWordScore
				+ COMMA + avgMoveScore + COMMA + avgWordLength + COMMA
				+ numberOfMoves + COMMA + String.format("%.1f", gameTime) + COMMA + score + COMMA
				+ win;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agent == null) ? 0 : agent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatsCollector other = (StatsCollector) obj;
		if (agent == null) {
			if (other.agent != null)
				return false;
		} else if (!agent.equals(other.agent))
			return false;
		return true;
	}
	
}
