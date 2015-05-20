/*
 * File: Main.java
 * -------------------
 * Name: Daniel Staal
 * 
 * This file will eventually implement the game of Chess.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class Main extends ConsoleProgram
{

/////////////////////////////////////////////////////////////////////////
// Public variables
//
// Actions to do here:
//		- set printing on/off
//		- declare number of games to be played
/////////////////////////////////////////////////////////////////////////

	/* To distinguish datafiles */
	private int numberOfDataFile = 1;
	
	/* Do you want to print the board? */
	private boolean printing = true;
	
	/* randommoves or not */
	boolean randomMoves = false;
	
	/* number of games to be played by the agents */
	private double numberOfGames = 1;
	
	/* Mean number of moves per game */
	private int numOfMoves;
	private double mean = 0.0; 
	
	/* max number of moves */
	private int maxNumberOfMoves = 5;
	
	/* did the game reach a terminal state */
	private boolean result = true;
	
	/* number of features */
	private int numberOfFeatures = 3;
	
	/* checkmates,stalemates,remis */
	private int checkMates = 0;
	private int staleMates = 0;
	private int remis = 0;
	
	/* Standard initial positions */
	static private GPoint blackKing = new GPoint(0,0);
	static private GPoint whiteKing = new GPoint(0,2);
	static private GPoint rook = new GPoint(2,2);
	
	/* initial BoardPosition */
	private BoardPosition initPos = new BoardPosition(blackKing, whiteKing, rook);
	/* Chessboard */
	private Chessboard chessBoard = new Chessboard(initPos);

	/* FEATURE: pos squares for k */
	private ArrayList<Integer> posSquaresk;
	
	/* Starting parametervector and feature values */
	static ArrayList<Double> parVector = new ArrayList<Double>(); 
	
	/* initiate agent */
	Agent agent = new Agent(chessBoard, parVector, randomMoves);
	
/////////////////////////////////////////////////////////////////////////
// Main function
//	- playing the game
//	- measuring time
/////////////////////////////////////////////////////////////////////////

	public void run()
	{
		// to time the learning process
		long startTime = System.nanoTime();
		
		agent.update.initiateParVector(numberOfFeatures);

		// play games and learn on data
		for(int i=0; i<numberOfGames; i++)
		{
			if(i == numberOfGames - 1)
			{
				printing = true;
			}
			
			// see progress per 100 games
			if(i % 100 == 0)
			{
				print("after ");print(i);println(" games:");
				print("checkmates:");println(checkMates);
				print("stalemates:");println(staleMates);
				print("remis:");println(remis);
			}
			
			// set a result for a game to true
			result = true;
			
			// play a single game
			playAGame();

			// if a terminal state is achieved, update parameters
			if(result && !randomMoves && numOfMoves>1)
			{
				agent.update.learnOnData();
			}
			resetBoard();
		}
		printResult();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000000;
		print("Time:");print(duration);print("seconds");
	}
	
/////////////////////////////////////////////////////////////////////////
// Chess playing
/////////////////////////////////////////////////////////////////////////
	
	private void playAGame()
	{
		if(printing)
		{
			printChessboard();
		}
		
		// clear all past positions
		agent.pastStates.clear();
		agent.rewardFunction.stateValues.clear();
		
		playMoves();

		mean += numOfMoves;
		
		checkWhatEnding();
	}
	
	private void checkWhatEnding()
	{
		if(chessBoard.getCheckMate())
		{
			checkMate();
		}
		else if(!chessBoard.rook.getRookAlive())
		{
			remis();
		}
		else if(chessBoard.getStaleMate())
		{
			staleMate();
		}
		else{result = false;}
	}
		
	private void checkMate()
	{
		checkMates++;
		agent.stateValues.add(2000.0);
	}
	
	private void remis()
	{
		remis++;
		agent.stateValues.add(-2000.0);
	}
	
	private void staleMate()
	{
		staleMates++;
		agent.stateValues.add(2000.0);
	}
		
	private void playMoves()
	{
		numOfMoves = 0;
		
		while(numOfMoves < maxNumberOfMoves)
		{
			blackMove();
			
			if(printing)
			{
				print("After black move");
				println();
				printChessboard();
			}
			
			if(chessBoard.getCheckMate() || chessBoard.getStaleMate() || !chessBoard.rook.getRookAlive())
			{
				break;
			}
			
			// after black move add current state to states
			agent.pastStates.add(chessBoard.getBoardPosition());

			agent.makeMove();

			//print("  reward:");println(agent.stateValues.get(numOfMoves));

			if(printing)
			{
				print("After white move");
				println();
				printChessboard();
			}
			numOfMoves++;
		}
	}	
	
	private void blackMove()
	{
		chessBoard.blackKing.randomMovek();
	}
	
	

/////////////////////////////////////////////////////////////////////////
//  Printing results and chessboard
//  
//	File writing and File reading
// 
// - the learning process will be computed below using Bufferedwriters
//   and bufferedreaders
/////////////////////////////////////////////////////////////////////////

	private void printChessboard()
	{
		for(int j=0; j<chessBoard.getCBSize(); j++)
		{
			for(int i=0; i<chessBoard.getCBSize(); i++)
			{
				print(chessBoard.getCBarray()[i][j]);
				print(" ");
			}
			println();
		}
	}	
	
	private void resetBoard()
	{
		chessBoard.resetPosition(initPos);
		//chessBoard.resetRandom();
	}
	
	private void printResult()
	{
		print((int)numberOfGames);println(" games played");
		print("checkmates:");println(checkMates);
		print("stalemates:");println(staleMates);
		print("remis:");println(remis);
		print("Mean number of moves per game:");println(mean/numberOfGames);
		print("Final weight value:");
		for(int i=0; i<parVector.size();i++)
		{
			println(parVector.get(i));
		}
	}
	
//	private void writeFilePosSquaresk()
//	{
//		// FEATURE: num pos squares
//		String stringPosSquaresk = "";
//		for(int i=0; i<posSquaresk.size();i++)
//		{
//			stringPosSquaresk += posSquaresk.get(i);
//		}
//		fileWriting(stringPosSquaresk);
//	}
	
//	private void fileWriting(String content)
//	{
//		String fileName = "testData" + numberOfDataFile + ".txt";
//		// to increment the name of the datafile
//		numberOfDataFile++;
//		
//		try {
// 
//			File file = new File("/home/jharvard/java/chess/data/" + fileName);
// 
//			// if file doesnt exists, then create it
//			if (!file.exists()) {
//				file.createNewFile();
//			}
// 
//			FileWriter fw = new FileWriter(file.getAbsoluteFile());
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(content);
//			bw.close();
// 
//			System.out.println("Done");
// 
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}























