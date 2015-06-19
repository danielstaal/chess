/*
 * File: Main.java
 * -------------------
 * Name: Daniel Staal
 * 
 * This is the main file of the chess endgame learner.
 *
 *
 * Main Components:
 *		- Playing Games
 *		- Entering all other classes
 *		- Changing all playing parameters except chessBoard size(Chessboard.java)
 *		- Keeping track of terminal state scores
 *		- Printing the board on the ConsoleProgram canvas
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
	private boolean randomPositions = false;
	
	/* use earlier calculated parameter vector testData */
	private boolean useTestData = false;
	
	/* do you want to save the updated weights and overwrite the last ones? */
	private boolean savingWeights = true;	
	
	/* is this a testing round? */
	private boolean testing = false;
	
	/* 1 or 2 evaluation functions? */
	private boolean twoRewardFunctions = false;
	
	/* number of iterations */
	private double numberOfIterations = 10;
	
	/* number of games to be played by the agents */
	private double numberOfGames = 200;
	
	/* max number of moves */
	private int maxNumberOfMoves = 20;
	
	/* number of features used in the parameter vector*/
	private int numberOfFeatures = 5;
	
	/* number of games per which terminal states are written */
	private int writePlotPerNumber = 20;
	
	/* do you want a simple plot? */
	private boolean plotting = true;
	
	/* initial position */
	private GPoint blackKing = new GPoint(2,2);
	private GPoint whiteKing = new GPoint(0,3);
	private GPoint rook = new GPoint(0,0);
	
	/* terminal state rewards */
	private double checkMateReward = 2.0;
	private double staleMateReward = 1.0;
	private double rookLostReward = -2.0;
	
	private double kingOnEdgeReward = 2.0;
	
	private boolean alwaysTakingRookFlag = false;
	
/////////////////////////////////////////////////////////////////////////
// Do NOT change these variables
/////////////////////////////////////////////////////////////////////////

	/* initial BoardPosition */
	private BoardPosition initPos = new BoardPosition(blackKing, whiteKing, rook);
	/* Chessboard */
	private Chessboard chessBoard = new Chessboard(initPos);

	/* FEATURE: pos squares for k */
	private ArrayList<Integer> posSquaresk;
	
	/* Starting parametervector and feature values */
	private ArrayList<Double> parVector = new ArrayList<Double>(); 
	private ArrayList<Double> parVector2 = new ArrayList<Double>(); 
	
	/* initiate agent */
	private Agent agent = new Agent(chessBoard, parVector, parVector2, randomMoves);
	
	/* initiate Testing object */
	//private Tester tester = new Tester(chessBoard, agent, initPos);
	
	/* File writer */
	private WriteToFile fileWriter = new WriteToFile(parVector);
	private WriteToFile fileWriter2 = new WriteToFile(parVector2);
	
	
	/* did the game reach a terminal state */
	private boolean result = true;	

	/* checkmates,stalemates,rookLost */
	private int checkMates = 0;
	private int staleMates = 0;
	private int rookLost = 0;
	
	private int kingOnEdges = 0;
		
	/* Mean number of moves per game */
	private int numOfMoves;
	private double mean = 0.0; 
	
	/* to plot a graph */ 
	private Plotter plotter = new Plotter(numberOfGames, writePlotPerNumber);

	
/////////////////////////////////////////////////////////////////////////
// run method
//	- initiate the weights values
//	- read in saved weights if requested
//	- measuring time
//	- print and plot the results
/////////////////////////////////////////////////////////////////////////

	
	
	public void run()
	{
		// start timer
		long startTime = System.nanoTime();
		
		// to set whether black king should alwaysTakingRook if possible
		chessBoard.blackKing.setAlwaysTakingRook(alwaysTakingRookFlag);
		
		for(int i=0;i<numberOfIterations;i++)
		{
			playNumberOfIterations();
			
			setLastPlotterArrayValues();
		}
		
		plotter.printMeanArrays(numberOfIterations);
		
		// print timer
		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000000;
		print("Time:");print(duration);print("seconds");
		
		// plot values if requested
		if(plotting){plotter.plotValues();}
	}
	
/////////////////////////////////////////////////////////////////////////
// Chess playing
/////////////////////////////////////////////////////////////////////////
	
	private void playNumberOfIterations()
	{
		// initiate weight vector
		agent.update.initiateParVector(numberOfFeatures);
		agent.update2.initiateParVector(numberOfFeatures);
		
		// read in weights if requested
		if(useTestData)
		{
			fileWriter.readFile(1);
			fileWriter2.readFile(2);
		}

		// play the games
		playGamesLearnOnData();
		
		// print the result
		//printResult();
	}
	
	private void setLastPlotterArrayValues()
	{
		plotter.setCheckMateArray(checkMates, (int)numberOfGames/writePlotPerNumber);
		plotter.setStaleMateArray(staleMates, (int)numberOfGames/writePlotPerNumber);
		plotter.setRemisArray(rookLost, (int)numberOfGames/writePlotPerNumber);
	
		// reset values, this is optional
		checkMates = 0;
		staleMates = 0;
		rookLost = 0;
		agent.numberOfRandomMoves = 0;
	}
	
	/*
	- test or play games
	- save weights if requested
	*/
	private void playGamesLearnOnData()
	{
		if(testing)
		{
		//	tester.test();
		}
		else
		{
			playGivenNumberOfGames();
			
			if(savingWeights)
			{
				fileWriter.writeFileParVector();
				fileWriter2.writeFileParVector();
			}
		}
	}
	
	/*
	- play a given number of games
	- learn on data if:
		- terminal state is achieved
		- the moves are not random
	- save and print terminal state values after each given number of games
	*/
	private void playGivenNumberOfGames()
	{
		for(int i=0; i<numberOfGames; i++)
		{
//			if(i == numberOfGames - 1)
//			{
//				printing = true;
//			}
		
			// see progress per writePlotPerNumber of games
			if(i % writePlotPerNumber == 0)
			{
				plotter.setCheckMateArray(checkMates, i/writePlotPerNumber);
				plotter.setStaleMateArray(staleMates, i/writePlotPerNumber);
				plotter.setRemisArray(rookLost, i/writePlotPerNumber);
	
				print("after ");print(i);println(" games:");
				print("checkmates:");println(checkMates);
				print("stalemates:");println(staleMates);
				print("rookLost:");println(rookLost);
				
				// reset values, this is optional
				checkMates = 0;
				staleMates = 0;
				rookLost = 0;
				agent.numberOfRandomMoves = 0;
			}
		
			// set a result for a game to true
			result = true;
							
			// play a single game
			playAGame();

			// if a terminal state is achieved, update parameters
			if(result && !randomMoves && numOfMoves>1)
			{
				if(twoRewardFunctions){agent.update.learnOnData();}
				agent.update2.learnOnData();
			}
			resetValues();
		}
	}
	
	/*
	- if printing print the chessboard
	- play the moves till terminal state or maxNumberOfMoves is reached
	- calculate new mean
	- check if and what terminal state is reached
	*/ 
	private void playAGame()
	{
		if(printing)
		{
			println("");
			println("Initial Position:");
			printChessboard();
		}
		
		// set num of moves to 0
		numOfMoves = 0;
	
		
		if(twoRewardFunctions)
		{
			playMoves();
			checkWhatEnding(1);
		}
		
		if(chessBoard.rook.getRookAlive())
		{
			playMoves2();
			checkWhatEnding(2);
		}		
		mean += numOfMoves;
		

		
	}

	private void playMoves()
	{	
		// play till max number of moves is reached
		while(numOfMoves < maxNumberOfMoves)
		{	
			// black player makes a move
			blackMove();
			
			ifPrinting("After black move");
			
			if(!chessBoard.rook.getRookAlive())
			{
				break;
			}
			
			// after black move add current state to states
			agent.pastStates.add(chessBoard.getBoardPosition());
			
			// white player makes a move
			agent.makeMove(1, checkMates, rookLost);

			ifPrinting("After white move");

			numOfMoves++;
			
			if(kingOnEdge())
			{
				break;
			}
		}
	}
	
	private boolean kingOnEdge()
	{
		if(chessBoard.blackKing.getx() == chessBoard.getCBSize()-1 || chessBoard.blackKing.getx() == 0 || chessBoard.blackKing.gety() == chessBoard.getCBSize()-1 || chessBoard.blackKing.gety() == 0)
		{
			return true;
		}
		return false;
	}
	
	/*
	- keep track of the numOfMoves in this game
	- moving the pieces
		- play black move
		- check if terminal state is reached, if so then break
		- else add this state to pastStates array
		- play white move
	*/	
	private void playMoves2()
	{
		
		// play till max number of moves is reached
		while(numOfMoves < maxNumberOfMoves)
		{	
			// black player makes a move
			blackMove();
			
			ifPrinting("After black move");
			
			if(chessBoard.getCheckMate() || chessBoard.getStaleMate() || !chessBoard.rook.getRookAlive())
			{
				break;
			}
			
			// after black move add current state to states
			agent.pastStates2.add(chessBoard.getBoardPosition());
			
			// white player makes a move
			agent.makeMove(2, checkMates, rookLost);

			ifPrinting("After white move");

			numOfMoves++;
		}
	}	
	
	private void blackMove()
	{
		chessBoard.blackKing.randomMovek();
	}
	
	private void ifPrinting(String player)
	{
		if(printing)
		{
			print(player);
			println();
			printChessboard();
		}
	}
	
	private void checkWhatEnding(int index)
	{
		if(chessBoard.getCheckMate())
		{
			checkMates++;
			agent.stateValues2.add(checkMateReward);
		}
		else if(!chessBoard.rook.getRookAlive())
		{
			rookLost++;
			if(index == 1){agent.stateValues.add(rookLostReward);}
			else{agent.stateValues2.add(rookLostReward);}
		}
		else if(chessBoard.getStaleMate())
		{
			staleMates++;
			agent.stateValues2.add(staleMateReward);
		}
		else if(kingOnEdge() && index == 1)
		{
			kingOnEdges++;
			agent.stateValues.add(kingOnEdgeReward);
		}
		else{result = false;}
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
		agent.pastStates2.clear();
		
		agent.rewardFunction.stateValues.clear();
		agent.rewardFunction2.stateValues.clear();
			
		agent.rewardFunction.clearFeatureValues();
		agent.rewardFunction2.clearFeatureValues();
	
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
		print("rookLost:");println(rookLost);
		print("kingOnEdge:");println(kingOnEdges);
		print("numberOfRandomMoves:");println(agent.numberOfRandomMoves);
		print("Mean number of moves per game:");println(mean/numberOfGames);
		println("Final weight values:");println("");
		println("Phase 1");
		
		for(int i=0; i<parVector.size();i++)
		{
			print(agent.rewardFunction.featureNames.get(i));print(":");				  				println(parVector.get(i));
		}
		println("");
		println("Phase 2");
		for(int i=0; i<parVector2.size();i++)
		{
			print(agent.rewardFunction2.featureNames.get(i));print(":");				  				println(parVector2.get(i));
		}
	}
}
