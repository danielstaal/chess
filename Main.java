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
//		- change starting parameters
/////////////////////////////////////////////////////////////////////////
	
	/* Do you want to print the board? */
	private boolean printing = false;
	
	/* randommoves or not */
	private boolean randomMoves = false;
	
	/* random initial positions */
	private boolean randomPositions = true;
	
	/* use earlier calculated parameter vector testData */
	private boolean useTestData = false;	
	
	/* is this a testing round? */
	private boolean testing = false;
	
	/* number of games to be played by the agents */
	private double numberOfGames = 3;
	
	/* max number of moves */
	private int maxNumberOfMoves = 10;
	
	/* number of features used in the parameter vector*/
	private int numberOfFeatures = 4;
	
	/* Initial position */
	static private GPoint blackKing = new GPoint(0,0);
	static private GPoint whiteKing = new GPoint(0,2);
	static private GPoint rook = new GPoint(3,3);
	
	/* rewards */
	private double checkMateReward = 2000.0;
	private double staleMateReward = 2000.0;
	private double remisReward = -2000.0;
	
	
/////////////////////////////////////////////////////////////////////////
// Do NOT change these Public variables
/////////////////////////////////////////////////////////////////////////

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
	
	/* initiate Testing object */
	Tester tester = new Tester(chessBoard, agent, initPos);
	
	/* File writer */
	WriteToFile fileWriter = new WriteToFile(parVector);
	
	/* did the game reach a terminal state */
	private boolean result = true;	

	/* checkmates,stalemates,remis */
	private int checkMates = 0;
	private int staleMates = 0;
	private int remis = 0;
		
	/* Mean number of moves per game */
	private int numOfMoves;
	private double mean = 0.0; 
	
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
		
		if(useTestData)
		{
			fileWriter.readFile();
		}

		playGamesLearnOnData();
		
		printResult();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000000;
		print("Time:");print(duration);print("seconds");
	}
	
/////////////////////////////////////////////////////////////////////////
// Chess playing
/////////////////////////////////////////////////////////////////////////
	
	private void playGamesLearnOnData()
	{
	// play games and learn on data
	
		if(testing)
		{
			tester.test();
		}
		else
		{
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
			}
			
			fileWriter.writeFileParVector();
		}
	}
	
	private void playAGame()
	{
		if(printing)
		{
			println("");
			println("Initial Position:");
		}
		
		printChessboard();
		
		playMoves();

		mean += numOfMoves;
		
		checkWhatEnding();
		
		resetValues();
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
//  Rewards
/////////////////////////////////////////////////////////////////////////
	private void checkMate()
	{
		checkMates++;
		agent.stateValues.add(checkMateReward);
	}
	
	private void remis()
	{
		remis++;
		agent.stateValues.add(remisReward);
	}
	
	private void staleMate()
	{
		staleMates++;
		agent.stateValues.add(staleMateReward);
	}
/////////////////////////////////////////////////////////////////////////
//  Printing
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
	
	private void resetValues()
	{
		// clear all past positions
		agent.pastStates.clear();
		
		agent.rewardFunction.stateValues.clear();
			
		agent.rewardFunction.clearFeatureValues();
	
		resetBoard();
	}
	
	private void resetBoard()
	{
		if(randomPositions)
		{
			chessBoard.resetRandom();
		}
		else{chessBoard.resetPosition(initPos);}
	}
	
	private void printResult()
	{
		print((int)numberOfGames);println(" games played");
		print("checkmates:");println(checkMates);
		print("stalemates:");println(staleMates);
		print("remis:");println(remis);
		print("Mean number of moves per game:");println(mean/numberOfGames);
		println("Final weight values:");
		for(int i=0; i<parVector.size();i++)
		{
			print(agent.rewardFunction.featureNames.get(i));print(":");				  				println(parVector.get(i));
		}
	}
}























