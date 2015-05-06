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
	
	/* Standard initial positions */
	private GPoint k = new GPoint(2,0);
	private GPoint K = new GPoint(0,2);
	private GPoint R = new GPoint(0,0);
	
	/* inition BoardPosition */
	BoardPosition initPos = new BoardPosition(k, K, R);
	
	/* Chessboard */
	Chessboard chessBoard = new Chessboard(initPos);

	/* FEATURE: pos squares for k */
	private ArrayList<Integer> posSquaresk;
	
	/* To distinguish datafiles */
	private int numberOfDataFile = 1;
	
	/* Do you want to print the board? */
	private boolean printing = false;
	
	/* number of games to be played by the agents */
	private double numberOfGames = 10;
	
	/* Mean number of moves per game */
	private int numOfMoves;
	private double mean = 0.0; 
	
	/* checkmates,stalemates,remis */
	private int checkMates = 0;
	private int staleMates = 0;
	private int remis = 0;
	
	/* Starting parametervector and feature values */
	ArrayList<Double> parVector = new ArrayList<Double>(); 
	ArrayList<Double> featureValues = new ArrayList<Double>(); 
	
	/* arraylist for all boardpositions in a game */
	ArrayList<BoardPosition> pastStates = new ArrayList<BoardPosition>();
	
	/* learning parameters */
	private double lambda = 0.7;

/////////////////////////////////////////////////////////////////////////
// Main function
//	- playing the game
//	- measuring time
/////////////////////////////////////////////////////////////////////////

	public void run()
	{
		// to time the learning process
		long startTime = System.nanoTime();
		
		initiateParVector();

		// play games and learn on data
		for(int i=0; i<numberOfGames; i++)
		{
			playAGame();
		
			// FEATURE: write to file pos squares k
			//writeFilePosSquaresk();
			learnOnData();
			
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
		pastStates.clear();
		
		playMoves();

		mean += numOfMoves;
		checkWhatEnding();
	}
	
	private void checkWhatEnding()
	{
		if(chessBoard.checkMate)
		{
			checkMate();
		}
		else if(!chessBoard.rookAlive)
		{
			remis();
		}
		else{staleMate();}
	}
		
	private void checkMate()
	{
		checkMates++;
	}
	
	private void remis()
	{
		remis++;
	}
	
	private void staleMate()
	{
		staleMates++;
	}
		
	private void playMoves()
	{
		numOfMoves = 0;
		posSquaresk = new ArrayList<Integer>();
		
		while(true)
		{
			blackMove();
			
			if(printing)
			{
				println();
				printChessboard();
			}
			
			if(chessBoard.checkMate || chessBoard.staleMate || !chessBoard.rookAlive)
			{
				break;
			}
			
			pastStates.add(chessBoard.getBoardPosition());

			//randomWhiteMove();
			whiteMove();
			
			// FEATURE: number of pos squares for k
			//posSquaresk.add(chessBoard.noOfPosSquaresk());
			
			if(printing)
			{
				println();
				printChessboard();
			}
			numOfMoves++;
		}
	}	
	
	private void blackMove()
	{
		chessBoard.randomMovek();
	}
	
	// NOTE: is te veranderen in random uit findAllPosNextStates
	private void randomWhiteMove()
	{
		// choose random piece
		int rand = chessBoard.randInt(0,1);
		boolean legalMove = false;
		
		// maybe not most efficient with while loop
		while(!legalMove)
		{
			if(rand == 0)
			{
				legalMove = chessBoard.randomMoveR();
			}else
			{
				legalMove = chessBoard.randomMoveK();
			}
			rand = chessBoard.randInt(0,1);
		}
	}
		
	private void whiteMove()
	{
		findBestMove();
	}	



/////////////////////////////////////////////////////////////////////////
// Reward function
/////////////////////////////////////////////////////////////////////////
	
	private void findBestMove()
	{
		ArrayList<BoardPosition> allPosNextMoves;
		allPosNextMoves = chessBoard.findAllPosNextStates();
		
		BoardPosition bestMove = findHighestReward(allPosNextMoves);
		//if(RewardIsAboveThreshold());
		
		// NOTE: the random black king move that was used to calculate the 
		// reward is not the final choice of the opponent agent
		chessBoard = new Chessboard(bestMove);
	}
	
	public BoardPosition findHighestReward(ArrayList<BoardPosition> allPosNextMoves)
	{
		double reward = 0.0;
		double thisReward;
		BoardPosition bestBoardPosition = null;
		
		for(int i=0;i<allPosNextMoves.size();i++)
		{
			thisReward = calcReward(allPosNextMoves.get(i));
			if(thisReward > reward)
			{
				reward = thisReward;
				bestBoardPosition = allPosNextMoves.get(i);
			}
		}
		
		return bestBoardPosition;
	}
		
	private double calcReward(BoardPosition pos)
	{
		double reward = 0.0;
		
		Chessboard thisBoard = new Chessboard(pos);
		
		// make a random move with the black king
		thisBoard.randomMovek();
		
		// Feature no.1
		reward += parVector.get(0) * thisBoard.noOfPosSquaresk();
		// Feature no.2
		
		return reward;
	}
	

/////////////////////////////////////////////////////////////////////////
// Data Learning
/////////////////////////////////////////////////////////////////////////
	
	private void initiateParVector()
	{
//			FEATURE Weights:
//				no.1: legal squares for black king		
		parVector.add(0.5);
//				no.2: 	
	}
	
	private void learnOnData()
	{
		println("finished");
			// updating the parameters	
		double learningRate = 0.7;

		double sumGradJ = sumGradJ();
		
		updateParameters(learningRate, sumGradJ);
	}

	private double sumGradJ()
	{
		double sumGradients = 0;
	
		for(int i=0; i<pastStates.size();i++)
		{
			sumGradients += calcGradJ(pastStates.get(i), i);
		}
		
		return sumGradients;
	}
	
	private double calcGradJ(BoardPosition pos, int t)
	{
		double gradJ = 0;
		
		Chessboard thisBoard = new Chessboard(pos);

		double correctiondt = calcCorrectiondt(pos, t);
		
		// Feature no.1
		gradJ += thisBoard.noOfPosSquaresk() * correctiondt;
		
		return gradJ;
	}
	
	private double calcCorrectiondt(BoardPosition pos, int t)
	{
		double correctiondt = 0;
		
		for(int j=t;j<pastStates.size();j++)
		{
			correctiondt += Math.pow(lambda, j-t) * tempDif(pos); 
		}
		
		return correctiondt;
	}
	
	
	// TODO: can be optemize, double code, double code everywhere...
	private double tempDif(BoardPosition pos)
	{
		double tempDif = 0;
		
		ArrayList<BoardPosition> allPosNextMoves;
		
		Chessboard tempChessBoard = new Chessboard(pos);
		
		allPosNextMoves = tempChessBoard.findAllPosNextStates();
		
		BoardPosition bestMove = findHighestReward(allPosNextMoves);

		tempDif = calcReward(bestMove) - calcReward(pos);	
			
		println(tempDif);
			
		return tempDif;
	}
	
	// TODO: make this vector multiplication
	private void updateParameters(double learningRate, double sumGradJ)
	{
		double feature = parVector.get(0);
		feature += learningRate*sumGradJ;
		parVector.set(0, feature);
	}
	
/////////////////////////////////////////////////////////////////////////
// Feature calculation
/////////////////////////////////////////////////////////////////////////

	
	
	
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
		chessBoard = new Chessboard(initPos);
	}
	
	private void printResult()
	{
		println("all games played");
		print("checkmates:");println(checkMates);
		print("stalemates:");println(staleMates);
		print("remis:");println(remis);
		print("Mean number of moves per game:");println(mean/numberOfGames);
		print("Final weight value:");println(parVector.get(0));
	}
	
	private void writeFilePosSquaresk()
	{
		// FEATURE: num pos squares
		String stringPosSquaresk = "";
		for(int i=0; i<posSquaresk.size();i++)
		{
			stringPosSquaresk += posSquaresk.get(i);
		}
		fileWriting(stringPosSquaresk);
	}
	
	private void fileWriting(String content)
	{
		String fileName = "testData" + numberOfDataFile + ".txt";
		// to increment the name of the datafile
		numberOfDataFile++;
		
		try {
 
			File file = new File("/home/jharvard/java/chess/data/" + fileName);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}























