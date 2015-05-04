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
	private GPoint k = new GPoint(0,0);
	private GPoint K = new GPoint(3,3);
	private GPoint R = new GPoint(5,5);
	
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
	private int numberOfGames = 10;
	
	/* checkmates,stalemates,remis */
	private int checkMates = 0;
	private int staleMates = 0;
	private int remis = 0;
	
	/* Starting parametervector and feature values */
	ArrayList<Double> parVector = new ArrayList<Double>(); 
	ArrayList<Double> featureValues = new ArrayList<Double>(); 

/////////////////////////////////////////////////////////////////////////
// Random chess playing
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
	
	private void initiateParVector()
	{
//			FEATURE Weights:
//				no.1: legal squares for black king		
		parVector.add(0.5);
//				no.2: 	
	}
	
	private void playAGame()
	{
		chessBoard.fillEmptyChessboard();
		chessBoard.addKRKtoChessboard();
		if(printing)
		{
			printChessboard();
		}
		playMoves();
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
		int moves;
		posSquaresk = new ArrayList<Integer>();
		
		while(true)
		{
			//readLine("?");
			blackMove();
			
			if(chessBoard.checkMate)
			{
				//println("Checkmate!");
				break;
			}
			else if(chessBoard.staleMate)
			{
				//println("Stalemate!");
				break;
			}
			else if(!chessBoard.rookAlive)
			{
				//println("Remis!");
				break;
			}
			if(printing)
			{
				println();
				printChessboard();
			}
			//readLine("?");
			randomWhiteMove();
			// whiteMove();
			
			
			// FEATURE: number of pos squares for k
			//posSquaresk.add(chessBoard.noOfPosSquaresk());
			
			if(printing)
			{
				println();
				printChessboard();
			}
		}
	}	
	
	private void blackMove()
	{
		chessBoard.movek();
	}
	
	private void randomWhiteMove()
	{
		// choose random piece
		int rand = chessBoard.randInt(0,1);
		boolean legalMove = false;
		
		while(!legalMove)
		{
			if(rand == 0)
			{
				legalMove = chessBoard.moveR();
			}else
			{
				legalMove = chessBoard.moveK();
			}
			rand = chessBoard.randInt(0,1);
		}
	}
		
	private void whiteMove()
	{
		findBestMove();
	}	
	
	private void findBestMove()
	{
		//findAllPosNextStates();
		//calcAllRewards();
		//pickBestMove();
		//if(RewardIsAboveThreshold());
	}
		
	private void printChessboard()
	{
		for(int j=0; j<chessBoard.size; j++)
		{
			for(int i=0; i<chessBoard.size; i++)
			{
				print(chessBoard.CBarray[i][j]);
				print(" ");
			}
			println();
		}
	}	
	
		
	private void resetBoard()
	{
		chessBoard = new Chessboard();
	}
	
	private void printResult()
	{
		println("all games played");
		print("checkmates:");println(checkMates);
		print("stalemates:");println(staleMates);
		print("remis:");println(remis);
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
	
	
/////////////////////////////////////////////////////////////////////////
// File writing and File reading
// 
// - the learning process will be computed below using Bufferedwriters
//   and bufferedreaders
/////////////////////////////////////////////////////////////////////////
	
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
	

/////////////////////////////////////////////////////////////////////////
// Data Learning
/////////////////////////////////////////////////////////////////////////

	private void learnOnData()
	{
			// updating the parameters	
		// For this we need:
		double learningRate = 0.7;
//		double tempDif = calcTempDif();
//		double correctiondt = calcCorrectiondt();
//		double gradJ = calcGradJ();	
		double lambda = 0.7;
		
		//updateParameters(learningRate, tempDif, correctiondt, gradJ, lambda);
	}
	
	private double calcRewardFunction(Chessboard board)
	{
		double reward = 0;
		
		for(int i=0; i<parVector.size(); i++)
		{
			// Feature no.1
			reward += parVector.get(i) * board.noOfPosSquaresk();
			// Feature no.2
		}

		return reward;
	}
	
	private double calcTempDif()
	{
		double tempDif = 0;
	
		
		
		return tempDif;
	}
//	
//	private double calcCorrectiondt()
//	{
//		double correctiondt;
//		
//		return correctiondt;
//	}
//	
	private double calcGradJ()
	{
		double gradJ = 0;
		
		for(int i=0; i<parVector.size();i++)
		{
			gradJ += parVector.get(i);
		}
		
		return gradJ;
	}
	
	private void updateParameters()
	{
	}
	
/////////////////////////////////////////////////////////////////////////
// Feature calculation
/////////////////////////////////////////////////////////////////////////


}























